package christmas.enums.benefit;

import christmas.enums.benefit.PromotionFunction;
import christmas.enums.menu.Menu;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public enum Benefit {
    크리스마스_디데이_할인(1000, PromotionFunction.christmasDayPromotion()),
    평일_할인(2023, PromotionFunction.weekdayPromotion()),
    주말_할인(2023, PromotionFunction.weekendPromotion()),
    특별_할인(1000, PromotionFunction.specialDiscount()),
    증정_이벤트(Menu.샴페인.price(), PromotionFunction.freebieEvent());

    public static final int MINIMUM_PRICE_FOR_FREEBIE = 120000;

    public static final int MINIMUM_PRICE_FOR_PROMOTIONS = 10000;

    public static final int CHRISTMAS_D_DAY_DISCOUNT_PER_DAY = 100;

    public static final Set<DayOfWeek> DAYS_OF_WEEKDAY_PROMOTION = new HashSet<>(
            List.of(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY)
    );

    public static final Set<DayOfWeek> DAYS_OF_WEEKEND_PROMOTION = new HashSet<>(
            List.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
    );

    public static final Menu FREEBIE = Menu.샴페인;

    Benefit(int discount, BiFunction<Integer, Map<Menu, Integer>, Integer> promotion) {
        this.discount = discount;
        this.promotion = promotion;
    }

    private final int discount;

    public int discount() {
        return discount;
    }

    public BiFunction<Integer, Map<Menu, Integer>, Integer> promotion() {
        return promotion;
    }

    public BiFunction<Integer, Map<Menu, Integer>, Integer> promotion;
}
