package christmas.util.factory;

import christmas.context.annotation.Component;
import christmas.domain.Order;
import christmas.enums.error.Error;
import christmas.enums.menu.Category;
import christmas.enums.menu.Menu;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderFactoryImpl implements OrderFactory {
    private static final int MINIMUM_QUANTITY_AT_ONCE = 1;
    private static final int MAXIMUM_QUANTITY_AT_ONCE = 20;

    @Override
    public Order createOrder(int date, Map<Menu, Integer> orderedMenus) {
        if (!validate(orderedMenus)) {
            throw new IllegalArgumentException(Error.INVALID_ORDER.getMessage());
        }
        return new Order(date, orderedMenus);
    }

    private boolean validate(Map<Menu, Integer> orderedMenus) {
        if (!isInRangeOfQuantityLimit(orderedMenus)) {
            return false;
        }

        if (hasOnlyBeverage(orderedMenus)) {
            return false;
        }

        return true;
    }

    private boolean isInRangeOfQuantityLimit(Map<Menu, Integer> orderedMenus) {
        int totalQuantity = 0;

        for (int quantity : orderedMenus.values()) {
            if (quantity > MAXIMUM_QUANTITY_AT_ONCE || quantity < MINIMUM_QUANTITY_AT_ONCE) {
                return false;
            }
            totalQuantity = totalQuantity + quantity;
        }

        return totalQuantity <= MAXIMUM_QUANTITY_AT_ONCE && totalQuantity >= MINIMUM_QUANTITY_AT_ONCE;
    }

    private boolean hasOnlyBeverage(Map<Menu, Integer> orderedMenus) {
        Set<Category> categories = orderedMenus.keySet().stream().map(Menu::category).collect(Collectors.toSet());
        return categories.size() == 1 && categories.contains(Category.음료);
    }
}
