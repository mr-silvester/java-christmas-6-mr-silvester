package christmas.context.implementation;

import christmas.context.applicationContext.ApplicationContext;
import christmas.context.annotation.Component;
import christmas.context.annotation.Scan;
import christmas.context.annotationScanner.AnnotatedClassScanner;
import christmas.context.exception.BeansException;
import christmas.context.exception.NoSuchBeanDefinitionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DefaultApplicationContext implements ApplicationContext {
    public DefaultApplicationContext(String basePackage, LoadingStrategy loadingStrategy) {
        this.loadingStrategy = loadingStrategy;
        this.scannedClasses = AnnotatedClassScanner.scan(basePackage, Scan.class);
        init();
    }

    public DefaultApplicationContext(String basePackage) {
        this.loadingStrategy = LoadingStrategy.EAGER;
        this.scannedClasses = AnnotatedClassScanner.scan(basePackage, Scan.class);
        init();
    }

    private final LoadingStrategy loadingStrategy;

    private final Map<Class<?>, Class<?>> interfaceRegistry = new HashMap<>();

    private final Map<Class<?>, Class<?>> superClassRegistry = new HashMap<>();

    private final Map<Class<?>, String> beanNameRegistry = new HashMap<>();

    private final Map<String, Object> beans = new HashMap<>();

    private final List<Class<?>> scannedClasses;

    @Override
    public Object getBean(String beanName) {
        if (!beans.containsKey(beanName)) {
            throw new BeansException(BeansException.NO_SUCH_NAMED_BEAN, beanName);
        }

        return beans.get(beanName);
    }

    @Override
    public List<Class<?>> getDefinedInterfaces() {
        return new ArrayList<>(interfaceRegistry.keySet());
    }

    @Override
    public List<Class<?>> getDefinedSuperClasses() {
        return new ArrayList<>(superClassRegistry.keySet());
    }

    @Override
    public List<Class<?>> getGeneratedBeans() {
        return new ArrayList<>(beanNameRegistry.keySet());
    }

    @Override
    public List<String> getGeneratedBeanNames() {
        return new ArrayList<>(beans.keySet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<?> requiredType) throws NoSuchBeanDefinitionException {
        if (requiredType.isInterface()) {
            return (T) findByInterface(requiredType);
        }
        if (beanNameRegistry.containsKey(requiredType)) {
            return (T) getOrRegisterBean(requiredType);
        }
        if (superClassRegistry.containsKey(requiredType)) {
            return (T) findBySuperClass(requiredType);
        }
        if (getScannedClassesWithAnnotation(Component.class).contains(requiredType)) {
            return (T) getOrRegisterBean(requiredType);
        }
        throw new NoSuchBeanDefinitionException(requiredType);
    }

    @Override
    public String registerBean(Class<?> instanceType) throws BeansException {
        String name = getBeanName(instanceType);

        if (beanNameRegistry.containsKey(instanceType)) {
            throw new BeansException(BeansException.ALREADY_REGISTERED_BEAN_TYPE, instanceType);
        }

        Constructor<?>[] constructors = instanceType.getConstructors();
        Object instance  = createInstance(constructors);

        beanNameRegistry.put(instanceType, name);
        beans.put(name, instance);

        return name;
    }

    @Override
    public boolean containsBean(String name) {
        return beans.containsKey(name);
    }

    @Override
    @Deprecated
    public void registerBean(String name, Object instance) throws BeansException {
        throw new BeansException("ApplicationContext 에서는 지원하지 않는 기능입니다.");
    }

    @Override
    public int getBeanCount() {
        return beans.size();
    }

    private Object findByInterface(Class<?> requiredType) {
        if (!interfaceRegistry.containsKey(requiredType)) {
            throw new NoSuchBeanDefinitionException(requiredType);
        }

        Class<?> beanType = interfaceRegistry.get(requiredType);
        return getOrRegisterBean(beanType);
    }

    private Object findBySuperClass(Class<?> requiredType) {
        Class<?> beanType = superClassRegistry.get(requiredType);
        return getOrRegisterBean(beanType);
    }

    private Object getOrRegisterBean(Class<?> requiredType) {
        if (beanNameRegistry.containsKey(requiredType)) {
            String beanName = beanNameRegistry.get(requiredType);
            return beans.get(beanName);
        }

        String name = registerBean(requiredType);

        return beans.get(name);
    }

    private String getBeanName(Class<?> instanceType) {
        String name = instanceType.getAnnotation(Component.class).name();

        if (name.isEmpty()) {
            name = prefix(instanceType);
        }

        return name;
    }

    private Object[] resolveConstructorArguments(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            arguments[i] = getBean(parameterType);
        }

        return arguments;
    }

    private Object createInstance(Constructor<?>[] constructors) {
        Object instance;

        for (Constructor<?> constructor : constructors) {
            instance = createInstance(constructor);
            if (instance != null) {
                return instance;
            }
        }

        throw new NoSuchBeanDefinitionException(constructors.getClass().getDeclaringClass());
    }

    private Object createInstance(Constructor<?> constructor) {
        Object[] arguments = resolveConstructorArguments(constructor);

        try {
            return constructor.newInstance(arguments);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException exception) {
            return null;
        }
    }

    private List<Class<?>> getScannedClassesWithAnnotation(Class<? extends Annotation> annotation) {
        return scannedClasses.stream().filter(c -> c.isAnnotationPresent(annotation)).toList();
    }

    private void registerInterface(Class<?> target) {
        Class<?>[] interfaces = target.getInterfaces();

        if (interfaces.length != 0) {
            if (interfaceRegistry.containsKey(interfaces[0])) {
                throw new BeansException(BeansException.ALREADY_REGISTERED_INTERFACE, interfaces[0]);
            }

            interfaceRegistry.put(interfaces[0], target);
        }
    }

    private void registerSuperClass(Class<?> target) {
        Class<?> superClass = target.getSuperclass();
        if (superClass != Object.class) {
            if (superClassRegistry.containsKey(superClass)) {
                throw new BeansException(BeansException.ALREADY_REGISTERED_SUPERCLASS, superClass);
            }
            superClassRegistry.put(superClass, target);
        }
    }

    private void init() {
        getScannedClassesWithAnnotation(Component.class).forEach(
                c -> {
                    registerInterface(c);
                    registerSuperClass(c);
                }
        );
        if (loadingStrategy.equals(LoadingStrategy.LAZY)) {
            return;
        }
        getScannedClassesWithAnnotation(Component.class).forEach(c -> {
            if (!beanNameRegistry.containsKey(c)) {
                registerBean(c);
            }
        });
    }

    private String prefix(Class<?> clazz) {
        String simpleName = clazz.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}
