package christmas.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderServiceImplTest {
    private static final Class<?> EXCEPTION_TYPE = IllegalArgumentException.class;

    OrderServiceImpl orderService = new OrderServiceImpl(null);

    @Test
    @Disabled("Order 객체 생성은 OrderFactory 에게 위임합니다.")
    void createOrder() {
    }

    static Stream<Arguments> parseMenusTestCases() {
        return Stream.of(
                Arguments.of("정상적인 주문은 예외가 발생하지 않음", "해산물파스타-1,초코케이크-1", null),
                Arguments.of("메뉴를 입력하지 않는 경우 예외 발생", "", EXCEPTION_TYPE),
                Arguments.of("메뉴판에 없는 메뉴를 입력하면 예외 발생", "없는메뉴-1,해산물파스타-2", EXCEPTION_TYPE),
                Arguments.of("중복된 메뉴를 입력하면 예외 발생", "초코케이크-1,레드와인-2,초코케이크-2", EXCEPTION_TYPE),
                Arguments.of("규칙에 맞지 않는 하이픈(-) 사용 시 예외 발생 (1)", "해산물파스타-1-1,레드와인-3", EXCEPTION_TYPE),
                Arguments.of("규칙에 맞지 않는 하이픈(-) 사용 시 예외 발생 (2)", "해산물-파스타1,레드와인-1", EXCEPTION_TYPE),
                Arguments.of("규칙에 맞지 않는 하이픈(-) 사용 시 예외 발생 (3)", "해산물파스타-1,-,", EXCEPTION_TYPE),
                Arguments.of("규칙에 맞지 않는 하이픈(-) 사용 시 예외 발생 (4)", "해산물파스타1", EXCEPTION_TYPE),
                Arguments.of("규칙에 맞지 않는 쉼표(,) 사용 시 예외 발생 (1)", "해산물파스타,1", EXCEPTION_TYPE),
                Arguments.of("규칙에 맞지 않는 쉼표(,) 사용 시 예외 발생 (2)", "해산물파스타-1,,", EXCEPTION_TYPE)
        );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @DisplayName("parseMenus()")
    @MethodSource("parseMenusTestCases")
    void parseMenus(
            String testCaseName,
            String menusInput,
            Class<?> expectedException
    ) {
        // given

        // when & then
        if (expectedException != null) {
            assertThatThrownBy(() -> orderService.parseMenus(menusInput)).isInstanceOf(EXCEPTION_TYPE);
            return;
        }
        assertThat(orderService.parseMenus(menusInput).size()).isEqualTo(2);
    }
}