package christmas.context.beanFactory;

import christmas.context.exception.BeansException;

import java.util.List;

public interface BeanFactory {
    /**
     * 이름이 일치하는 빈을 찾아 가져옵니다.
     */
    Object getBean(String name) throws BeansException;

    /**
     * 이름이 일치하는 빈이 존재하는지 확인합니다.
     */
    boolean containsBean(String name);

    /**
     * 이름과 인스턴스를 빈으로 등록합니다.
     */
    void registerBean(String name, Object instance) throws BeansException;

    /**
     * 등록된 빈 이름의 목록을 반환합니다.
     */
    List<String> getGeneratedBeanNames();

    /**
     * 등록된 빈 타입의 목록을 반환합니다.
     */
    List<Class<?>> getGeneratedBeans();

    /**
     * 등록된 빈의 갯수를 반환합니다.
     */
    int getBeanCount();
}
