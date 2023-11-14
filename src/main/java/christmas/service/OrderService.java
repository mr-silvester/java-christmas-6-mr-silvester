package christmas.service;

import christmas.domain.Order;
import christmas.enums.menu.Menu;

import java.util.Map;

public interface OrderService {
    Order createOrder(int date, String menusInput);

    Map<Menu, Integer> parseMenus(String menusInput);
}
