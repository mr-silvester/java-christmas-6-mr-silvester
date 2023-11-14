package christmas.context.exception;

public class BeansException extends RuntimeException {
    public static final String ALREADY_REGISTERED_BEAN_NAME = "같은 이름의 빈이 존재합니다.";
    public static final String ALREADY_REGISTERED_BEAN_TYPE = "같은 타입의 빈이 존재합니다.";
    public static final String ALREADY_REGISTERED_INTERFACE = "같은 인터페이스 타입으로 등록된 빈이 존재합니다.";
    public static final String ALREADY_REGISTERED_SUPERCLASS = "같은 상위 클래스 타입으로 등록된 빈이 존재합니다.";
    public static final String NO_SUCH_NAMED_BEAN = "해당 이름의 빈이 존재하지 않습니다.";
    public static final String NO_SUCH_TYPED_BEAN = "해당 타입의 빈이 존재하지 않습니다.";

    public BeansException(String message) {
        super(message);
    }

    public BeansException(String message, String beanName) {
        super(message + " : " + beanName);
    }

    public BeansException(String message, Class<?> beanType) {
        super(message + " : " + beanType);
    }
}
