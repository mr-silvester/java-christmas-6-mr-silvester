package christmas.service;

import christmas.context.annotation.Component;
import christmas.domain.Order;
import christmas.enums.error.Error;
import christmas.enums.menu.Menu;
import christmas.util.factory.OrderFactory;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderServiceImpl implements OrderService {
    public OrderServiceImpl(OrderFactory orderFactory) {
        this.orderFactory = orderFactory;
    }

    private final OrderFactory orderFactory;

    @Override
    public Order createOrder(int day, String menusInput) {
        return orderFactory.createOrder(day, parseMenus(menusInput));
    }

    @Override
    public Map<Menu, Integer> parseMenus(String menusInput) {
        Map<Menu, Integer> orderedMenus = new HashMap<>();
        String[] menusWithNumber = splitMenusInput(menusInput);

        for (String menuWithNumber : menusWithNumber) {
            processMenuAndQuantity(menuWithNumber, orderedMenus);
        }

        return orderedMenus;
    }

    private String[] splitMenusInput(String menusInput) {
        return menusInput.split(",", -1);
    }

    private void processMenuAndQuantity(String input, Map<Menu, Integer> orderedMenus) {
        String[] menuAndQuantity = splitMenuAndQuantityPair(input);
        Menu menu = getMenu(menuAndQuantity[0]);
        int quantity = getQuantity(menuAndQuantity[1]);

        if (orderedMenus.putIfAbsent(menu, quantity) != null) {
            throw new IllegalArgumentException(Error.INVALID_ORDER.getMessage());
        }
    }

    private String[] splitMenuAndQuantityPair(String input) {
        String[] menuAndQuantity = input.split("-");

        if (menuAndQuantity.length != 2) {
            throw new IllegalArgumentException(Error.INVALID_ORDER.getMessage());
        }

        return menuAndQuantity;
    }

    private Menu getMenu(String input) {
        Menu menu;

        try {
            menu = Menu.valueOf(input);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(Error.INVALID_ORDER.getMessage());
        }

        return menu;
    }

    private int getQuantity(String input) {
        int quantity;

        try {
            quantity = Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(Error.INVALID_ORDER.getMessage());
        }

        return quantity;
    }
}
