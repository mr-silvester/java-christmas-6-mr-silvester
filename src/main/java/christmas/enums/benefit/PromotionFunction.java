package christmas.enums.benefit;

import christmas.enums.menu.Category;
import christmas.enums.menu.Menu;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;
import java.util.function.BiFunction;

public class PromotionFunction {
    private static final int THIS_YEAR = 2023;
    private static final Month THIS_MONTH = Month.DECEMBER;
    private static final int DATE_OF_CHRISTMAS = 25;

    public static BiFunction<Integer, Map<Menu, Integer>, Integer> christmasDayPromotion() {
        return (day, orderedMenus) -> {
            if (day > DATE_OF_CHRISTMAS) {
                return 0;
            }

            return Benefit.크리스마스_디데이_할인.discount() + Benefit.CHRISTMAS_D_DAY_DISCOUNT_PER_DAY * (day - 1);
        };
    }

    public static BiFunction<Integer, Map<Menu, Integer>, Integer> weekdayPromotion() {
        return (day, orderedMenus) -> {
            if (!Benefit.DAYS_OF_WEEKDAY_PROMOTION.contains(dayOfWeek(day))) {
                return 0;
            }

            return Benefit.평일_할인.discount() * orderedMenus.keySet().stream()
                    .filter(m -> m.category().equals(Category.디저트))
                    .mapToInt(orderedMenus::get)
                    .sum();
        };
    }

    public static BiFunction<Integer, Map<Menu, Integer>, Integer> weekendPromotion() {
        return (day, orderedMenus) -> {
            if (!Benefit.DAYS_OF_WEEKEND_PROMOTION.contains(dayOfWeek(day))) {
                return 0;
            }
            return Benefit.주말_할인.discount() * orderedMenus.keySet().stream()
                    .filter(m -> m.category().equals(Category.메인))
                    .mapToInt(orderedMenus::get)
                    .sum();
        };
    }

    public static BiFunction<Integer, Map<Menu, Integer>, Integer> specialDiscount() {
        return (day, orderedMenus) -> {
            if (!dayOfWeek(day).equals(DayOfWeek.SUNDAY) && day != DATE_OF_CHRISTMAS) {
                return 0;
            }
            return Benefit.특별_할인.discount();
        };
    }

    public static BiFunction<Integer, Map<Menu, Integer>, Integer> freebieEvent() {
        return (day, orderedMenus) -> {
            if (orderedMenus.entrySet().stream().mapToInt(m -> m.getKey().price() * m.getValue()).sum() >= Benefit.MINIMUM_PRICE_FOR_PROMOTIONS) {
                return Benefit.FREEBIE.price();
            }
            return 0;
        };
    }

    private static DayOfWeek dayOfWeek(int day) {
        return LocalDateTime.of(THIS_YEAR, THIS_MONTH, day, 0, 0).getDayOfWeek();
    }
}
