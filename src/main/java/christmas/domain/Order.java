package christmas.domain;

import christmas.enums.Badge;
import christmas.enums.Benefit;
import christmas.enums.menu.Category;
import christmas.enums.menu.Menu;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class Order {
    private static final int THIS_YEAR = 2023;
    private static final Month THIS_MONTH = Month.DECEMBER;
    private static final int DATE_OF_CHRISTMAS = 25;

    public Order(int today, Map<Menu, Integer> orderedMenus) {
        this.today = today;
        this.orderedMenus = Map.copyOf(orderedMenus);
        init();
    }

    private final int today;
    private final Map<Menu, Integer> orderedMenus;
    private final Map<Benefit, Integer> benefits = new HashMap<>();

    public int getDay() {
        return this.today;
    }

    @Deprecated
    public int getQuantityOf(Menu menu) {
        if (orderedMenus.containsKey(menu)) {
            return orderedMenus.get(menu);
        }

        return 0;
    }

    public List<Map.Entry<String, Integer>> getOrderedMenus() {
        return toEntryList(orderedMenus);
    }

    // 할인 전 총 주문 금액
    public int getPriceBeforeDiscount() {
        int totalPrice = 0;

        for (Menu menu : orderedMenus.keySet()) {
            totalPrice = totalPrice + menu.getPrice() * orderedMenus.get(menu);
        }

        return totalPrice;
    }

    // 할인 금액
    public int getTotalDiscount() {
        int totalDiscount = 0;

        for (Benefit benefit : benefits.keySet()) {
            if (benefit.equals(Benefit.증정_이벤트)) {
                continue;
            }
            totalDiscount = totalDiscount + benefits.get(benefit);
        }

        return totalDiscount;
    }

    // 할인 후 주문금액
    public int getPriceOfTotal() {
        return getPriceBeforeDiscount() - getTotalDiscount();
    }

    // 총혜택 금액 = 할인 금액의 합계 + 증정 메뉴의 가격
    public int getPriceOfTotalBenefits() {
        int freebiePrice = 0;

        if (isEligibleForFreebie()) {
            freebiePrice = Benefit.FREEBIE.getPrice();
        }

        return getTotalDiscount() + freebiePrice;
    }

    public Badge getBadge() {
        int totalBenefit = getPriceOfTotalBenefits();

        for (Badge badge : Badge.values()) {
            if (totalBenefit >= badge.getStandardPrice()) {
                return badge;
            }
        }

        return null;
    }

    public List<Map.Entry<String, Integer>> getBenefits() {
        return toEntryList(benefits);
    }

    @Deprecated
    public int getAmountOfBenefit(Benefit benefit) {
        if (benefit.equals(Benefit.증정_이벤트) && isEligibleForFreebie()) {
            return Benefit.FREEBIE.getPrice();
        }

        if (benefits.containsKey(benefit)) {
            return benefits.get(benefit);
        }

        return 0;
    }

    public boolean isEligibleForFreebie() {
        return getPriceBeforeDiscount() >= Benefit.MINIMUM_PRICE_FOR_FREEBIE;
    }

    private void init() {
        if (getPriceOfTotal() < Benefit.MINIMUM_PRICE_FOR_PROMOTIONS) {
            return;
        }
        if (this.today <= DATE_OF_CHRISTMAS) {
            christmasDayPromotion();
        }
        weekdayPromotion();
        weekendPromotion();
        specialDiscount();
        freebieEvent();
    }

    private DayOfWeek dayOfWeek() {
        return LocalDateTime.of(THIS_YEAR, THIS_MONTH, today, 0, 0).getDayOfWeek();
    }

    private void christmasDayPromotion() {
        int discount = Benefit.크리스마스_디데이_할인.getDiscount() + Benefit.CHRISTMAS_D_DAY_DISCOUNT_PER_DAY * (today - 1);
        benefits.putIfAbsent(Benefit.크리스마스_디데이_할인, discount);
    }

    private void weekdayPromotion() {
        if (!Benefit.DAYS_OF_WEEKDAY_PROMOTION.contains(dayOfWeek())) {
            return;
        }
        int discount = Benefit.평일_할인.getDiscount() * orderedMenus.keySet().stream()
                .filter(m -> m.getCategory().equals(Category.디저트))
                .mapToInt(orderedMenus::get)
                .sum();
        benefits.putIfAbsent(Benefit.평일_할인, discount);
    }

    private void weekendPromotion() {
        if (!Benefit.DAYS_OF_WEEKEND_PROMOTION.contains(dayOfWeek())) {
            return;
        }
        int discount = Benefit.주말_할인.getDiscount() * orderedMenus.keySet().stream()
                .filter(m -> m.getCategory().equals(Category.메인))
                .mapToInt(orderedMenus::get)
                .sum();

        benefits.putIfAbsent(Benefit.주말_할인, discount);
    }

    private void specialDiscount() {
        if (!dayOfWeek().equals(DayOfWeek.SUNDAY) && today != DATE_OF_CHRISTMAS) {
            return;
        }
        benefits.putIfAbsent(Benefit.특별_할인, Benefit.특별_할인.getDiscount());
    }

    private void freebieEvent() {
        if (isEligibleForFreebie()) {
            benefits.putIfAbsent(Benefit.증정_이벤트, Benefit.FREEBIE.getPrice());
        }
    }

    private <T extends Enum<?>> List<Map.Entry<String, Integer>> toEntryList(Map<T, Integer> targetMap) {
        return targetMap.entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey().name(), e.getValue()))
                .collect(Collectors.toList());
    }
}
