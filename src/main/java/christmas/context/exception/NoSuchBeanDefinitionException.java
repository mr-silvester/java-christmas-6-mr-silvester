package christmas.context.exception;

public class NoSuchBeanDefinitionException extends BeansException{
    public NoSuchBeanDefinitionException(String beanName) {
        super("undefined creation of bean name : " + beanName);
    }

    public NoSuchBeanDefinitionException(Class<?> beanType) {
        super("undefined creation of bean type : " + beanType);
    }
}
