package christmas.domain;

import christmas.enums.benefit.Badge;
import christmas.enums.benefit.Benefit;
import christmas.enums.menu.Menu;

import java.util.*;
import java.util.stream.Collectors;

public class Order {
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

    public List<Map.Entry<String, Integer>> getOrderedMenus() {
        return toEntryList(orderedMenus);
    }

    // 할인 전 총 주문 금액
    public int getPriceBeforeDiscount() {
        int totalPrice = 0;

        for (Menu menu : orderedMenus.keySet()) {
            totalPrice = totalPrice + menu.price() * orderedMenus.get(menu);
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
            freebiePrice = Benefit.FREEBIE.price();
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

    public boolean isEligibleForFreebie() {
        return getPriceBeforeDiscount() >= Benefit.MINIMUM_PRICE_FOR_FREEBIE;
    }

    private void init() {
        if (getPriceOfTotal() < Benefit.MINIMUM_PRICE_FOR_PROMOTIONS) {
            return;
        }

        applyPromotions();
    }

    private void applyPromotions() {
        for (Benefit benefit : Benefit.values()) {
            int amountOfBenefit = benefit.promotion().apply(today, orderedMenus);

            if (amountOfBenefit != 0) {
                benefits.putIfAbsent(benefit, amountOfBenefit);
            }
        }
    }

    private <T extends Enum<?>> List<Map.Entry<String, Integer>> toEntryList(Map<T, Integer> targetMap) {
        return targetMap.entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey().name(), e.getValue()))
                .collect(Collectors.toList());
    }
}
