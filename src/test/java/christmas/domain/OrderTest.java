package christmas.domain;

import christmas.enums.benefit.Badge;
import christmas.enums.menu.Menu;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class OrderTest {
    @Test
    @DisplayName("getDay() : 주문 객체 생성 시 입력한 값과 동일한 값을 반환합니다.")
    void getDay() {
        // given
        int day = 25;
        Order order = new Order(day, Map.of(Menu.샴페인, 1, Menu.해산물파스타, 3));

        // when
        int result = order.getDay();

        // then
        assertThat(result).isEqualTo(day);
    }

    @Test
    @DisplayName("getOrderedMenus() : 주문한 메뉴의 이름과 수량을 리스트로 반환합니다.")
    void getOrderedMenus() {
        // given
        Order order = new Order(25, Map.of(Menu.크리스마스파스타, 3, Menu.레드와인, 1));

        // when
        List<Map.Entry<String, Integer>> result = order.getOrderedMenus();
        List<Map.Entry<String, Integer>> expectedResult =
                Arrays.asList(new AbstractMap.SimpleEntry<>("크리스마스파스타", 3), new AbstractMap.SimpleEntry<>("레드와인", 1));

        result.sort(Map.Entry.comparingByKey());
        expectedResult.sort(Map.Entry.comparingByKey());

        // then
        assertArrayEquals(result.toArray(), expectedResult.toArray());
    }

    @Test
    @DisplayName("getOriginalPrice()")
    void getOriginalPrice() {
        // given
        Map<Menu, Integer> orderedMenus = new HashMap<>(Map.of(Menu.크리스마스파스타, 2, Menu.레드와인, 1));
        Order order = new Order(25, orderedMenus);

        // when
        int result = order.getPriceBeforeDiscount();
        int expectedResult = Menu.크리스마스파스타.price() * 2 + Menu.레드와인.price();

        // then
        assertThat(result).isEqualTo(expectedResult);
    }


    static Stream<Arguments> getTotalDiscountTestCases() {
        return Stream.of(
                Arguments.of(
                        "크리스마스 디데이 할인 (1일) : 1,000원 할인",
                        1,
                        Map.of(Menu.양송이수프, 1, Menu.샴페인, 1),
                        1000
                ),
                Arguments.of(
                        "크리스마스 디데이 할인 (25일), 특별할인 (크리스마스 당일) : 4,400원 할인",
                        25,
                        Map.of(Menu.양송이수프, 1, Menu.크리스마스파스타, 1),
                        4400
                ),
                Arguments.of(
                        "크리스마스 디데이 할인 (26일) : 0원 할인",
                        26,
                        Map.of(Menu.양송이수프, 1, Menu.크리스마스파스타, 1),
                        0
                ),
                Arguments.of(
                        "평일할인 (디저트 2개, 미적용 1개) : 4,046원 할인",
                        26,
                        Map.of(Menu.초코케이크, 2, Menu.크리스마스파스타, 1),
                        4046
                ),
                Arguments.of(
                        "주말할인 (메인 3개, 미적용 1개) : 4,046원 할인",
                        29,
                        Map.of(Menu.해산물파스타, 2, Menu.크리스마스파스타, 1, Menu.샴페인, 1),
                        6069
                ),
                Arguments.of(
                        "특별할인 (일요일), 크리스마스 디데이 할인 (24일) : 4,300원 할인",
                        24,
                        Map.of(Menu.샴페인, 2, Menu.양송이수프, 2),
                        4300
                ),
                Arguments.of(
                        "특별할인 (크리스마스 당일), 크리스마스 디데이 할인 (25일) : 4,400원 할인",
                        25,
                        Map.of(Menu.양송이수프, 8, Menu.크리스마스파스타, 2),
                        4400
                ),
                Arguments.of(
                        "1만원 미만 주문 : 0원 할인",
                        25,
                        Map.of(Menu.아이스크림, 1),
                        0
                )
        );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @DisplayName("getTotalDiscount()")
    @MethodSource("getTotalDiscountTestCases")
    void getTotalDiscount(
            String testCaseName,
            int day,
            Map<Menu, Integer> orderedMenus,
            int expectedResult
    ) {
        // given
        Order order = new Order(day, orderedMenus);

        // when
        int result = order.getTotalDiscount();

        // then
        assertThat(result).isEqualTo(expectedResult);
    }


    @Test
    @Disabled("getTotalDiscount() 와 getOriginalPrice 에 의해 결정되므로 생략")
    @DisplayName("getTotalPrice()")
    void getTotalPrice() {
    }

    @Test
    @DisplayName("getTotalBenefits()")
    @Disabled("isEligibleForFreebie() 와 getTotalDiscount() 에 의해 결정되므로 생략")
    void getTotalBenefits() {
    }

    static Stream<Arguments> getBadgeTestCases() {
        return Stream.of(
                Arguments.of("총혜택 금액이 20,000원 이상인 경우 '산타'를 반환합니다.",
                        Map.of(Menu.해산물파스타, 3, Menu.크리스마스파스타, 2, Menu.바비큐립, 3),
                        Badge.산타
                ),
                Arguments.of("총혜택 금액이 10,000원 이상인 경우 '트리'를 반환합니다.",
                        Map.of(Menu.초코케이크, 3),
                        Badge.트리
                ),
                Arguments.of("총혜택 금액이 5,000원 이상인 경우 '별'을 반환합니다.",
                        Map.of(Menu.해산물파스타, 1, Menu.초코케이크, 1),
                        Badge.별
                ),
                Arguments.of("총혜택 금액이 5,000원 미만인 경우 '없음'을 반환합니다.",
                        Map.of(Menu.해산물파스타, 1),
                        Badge.없음
                )
        );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @DisplayName("getBadge()")
    @MethodSource("getBadgeTestCases")
    void getBadge(
            String testCaseName,
            Map<Menu, Integer> orderedMenus,
            Badge expectedResult

    ) {
        // given
        Order order = new Order(25, orderedMenus);

        // when
        Badge result = order.getBadge();

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("getBenefits() : 적용되는 혜택의 이름과 금액을 리스트로 반환합니다.")
    void getBenefits() {
        // given
        Order order = new Order(25, Map.of(Menu.크리스마스파스타, 3, Menu.레드와인, 3));

        // when
        List<Map.Entry<String, Integer>> result = order.getBenefits();
        List<Map.Entry<String, Integer>> expectedResult =
                Arrays.asList(
                        new AbstractMap.SimpleEntry<>("특별_할인", 1000),
                        new AbstractMap.SimpleEntry<>("증정_이벤트", 25000),
                        new AbstractMap.SimpleEntry<>("크리스마스_디데이_할인", 3400)
                );

        result.sort(Map.Entry.comparingByKey());
        expectedResult.sort(Map.Entry.comparingByKey());

        // then
        assertIterableEquals(result, expectedResult);
    }

    static Stream<Arguments> isEligibleForFreebieTestCases() {
        return Stream.of(
                Arguments.of("주문 금액이 120,000원 이상인 경우 true 반환",
                        Map.of(Menu.해산물파스타, 3, Menu.크리스마스파스타, 2, Menu.바비큐립, 3),
                        true
                ),
                Arguments.of("주문 금액이 120,000원 미만인 경우 false 반환",
                        Map.of(Menu.해산물파스타, 1),
                        false
                )
        );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @DisplayName("isEligibleForFreebie()")
    @MethodSource("isEligibleForFreebieTestCases")
    void isEligibleForFreebie(
            String testCaseName,
            Map<Menu, Integer> orderedMenus,
            boolean expectedResult
    ) {
        // given
        Order order = new Order(25, orderedMenus);

        // when
        boolean result = order.isEligibleForFreebie();

        // then
        assertThat(result).isEqualTo(expectedResult);
    }
}