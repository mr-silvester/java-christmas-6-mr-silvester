package christmas.util.factory;

import christmas.domain.Order;
import christmas.enums.menu.Menu;

import java.util.Map;

public interface OrderFactory {
    Order createOrder(int date, Map<Menu, Integer> orderedMenus);
}
