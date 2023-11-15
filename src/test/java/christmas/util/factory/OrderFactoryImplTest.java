package christmas.util.factory;

import christmas.enums.menu.Menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderFactoryImplTest {
    private static final Class<?> EXCEPTION_TYPE = IllegalArgumentException.class;

    OrderFactoryImpl orderFactory = new OrderFactoryImpl();

    static Stream<Arguments> createOrderTestCases() {
        return Stream.of(
                Arguments.of("정상적인 주문은 예외가 발생하지 않음",
                        Map.of(Menu.양송이수프, 19, Menu.바비큐립, 1),
                        null),
                Arguments.of("수량 1개 미만의 메뉴 주문 시 예외 발생",
                        Map.of(Menu.해산물파스타, 2, Menu.크리스마스파스타, 0),
                        EXCEPTION_TYPE),
                Arguments.of("모든 메뉴의 수량 총 합이 20개를 초과하는 경우 예외 발생",
                        Map.of(Menu.해산물파스타, 19, Menu.바비큐립, 2),
                        EXCEPTION_TYPE),
                Arguments.of("음료만 주문한 경우 예외 발생",
                        Map.of(Menu.샴페인, 2, Menu.레드와인, 3),
                        EXCEPTION_TYPE)
        );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @DisplayName("createOrder()")
    @MethodSource("createOrderTestCases")
    void createOrder(
            String testCaseName,
            Map<Menu, Integer> orderedMenus,
            Class<?> expectedException
    ) {
        // given
        int day = 25;

        // when & then
        if (expectedException != null) {
            assertThatThrownBy(() -> orderFactory.createOrder(day, orderedMenus))
                    .isInstanceOf(EXCEPTION_TYPE);
            return;
        }
        assertThat(orderFactory.createOrder(day, orderedMenus)).isNotNull();
    }
}