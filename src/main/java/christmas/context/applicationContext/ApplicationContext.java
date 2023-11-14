package christmas.context.applicationContext;

import christmas.context.beanFactory.BeanFactory;
import christmas.context.exception.BeansException;
import christmas.context.typedBeanRegistry.TypedBeanRegistry;

public interface ApplicationContext extends BeanFactory, TypedBeanRegistry {
    String registerBean(Class<?> instanceType) throws BeansException;

    <T> T getBean(Class<?> requiredType);
}
