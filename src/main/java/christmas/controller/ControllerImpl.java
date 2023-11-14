package christmas.controller;

import christmas.context.annotation.Component;
import christmas.domain.Order;
import christmas.enums.Benefit;
import christmas.enums.menu.Menu;
import christmas.service.OrderService;
import christmas.view.console.Console;
import christmas.view.expressionBuilder.BenefitExpressionBuilder;
import christmas.view.expressionBuilder.MenuExpressionBuilder;

import java.util.function.Supplier;

@Component
public class ControllerImpl implements Controller {
    public ControllerImpl(Console console, OrderService orderService) {
        this.console = console;
        this.orderService = orderService;
    }

    private final Console console;
    private final OrderService orderService;

    @Override
    public void run() {
        console.print().messageOf().beginning();
        Order order = createOrder();
        console.print().messageOf().previewEvent(order.getDay());
        printOrderedMenus(order);
        console.print().messageOf().originalTotalPrice().and().price().beforeDiscount(order.getOriginalPrice());
        printBenefits(order);
        console.print().messageOf().freebie().and().menuListOf().freebie(Benefit.FREEBIE.name(), order.isEligibleForFreebie());
        console.print().messageOf().totalBenefits().and().price().ofTotalBenefits(order.getTotalBenefits());
        console.print().messageOf().totalPrice().and().price().ofTotal(order.getTotalPrice());
        console.print().messageOf().badge().and().nameOf().badge(order.getBadge().name()).orNone().build();
        console.close();
    }

    private Order createOrder() {
        Integer day = retryOnFailure(() -> console.read().day());

        return retryOnFailure(
                () -> orderService.createOrder(
                        day, retryOnFailure(() -> console.read().menus())
                )
        );
    }

    private <T> T retryOnFailure(Supplier<T> supplier) {
        T target = null;

        while (target == null) {
            try {
                target = supplier.get();
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }

        return target;
    }

    private void printOrderedMenus(Order order) {
        MenuExpressionBuilder menuExpressionBuilder =
                console.print().messageOf().orderedMenus().and().menuListOf();

        for (Menu menu : Menu.values()) {
            menuExpressionBuilder.menu(menu.name(), order.getQuantityOf(menu)).and();
        }

        menuExpressionBuilder.build();
    }


    private void printBenefits(Order order) {
        BenefitExpressionBuilder benefitExpressionBuilder =
                console.print().messageOf().benefits().and().benefitListOf();

        for (Benefit benefit : Benefit.values()) {
            benefitExpressionBuilder.benefit(benefit.name(), order.getBenefit(benefit)).and();
        }

        benefitExpressionBuilder.orNone().build();
    }
}
