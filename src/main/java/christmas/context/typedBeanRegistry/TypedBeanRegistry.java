package christmas.context.typedBeanRegistry;

import java.util.List;

public interface TypedBeanRegistry {
    List<Class<?>> getDefinedInterfaces();

    List<Class<?>> getDefinedSuperClasses();
}