package christmas.controller;

import christmas.context.annotation.Component;
import christmas.domain.Order;
import christmas.enums.Benefit;
import christmas.service.OrderService;
import christmas.view.console.Console;

import java.util.function.Supplier;

@Component
public class ControllerImpl implements Controller {
    public ControllerImpl(Console console, OrderService orderService) {
        this.console = console;
        this.orderService = orderService;
    }

    private final Console console;
    private final OrderService orderService;

    public void run() {
        console.print().messageOf().beginning();
        Order order = createOrder();
        console.print().messageOf().previewEventBenefits(order.getDay())
                .and().messageOf().orderedMenus().and().menuListOf().menus(order.getOrderedMenus())
                .and().messageOf().priceBeforeDiscount().and().price().beforeDiscount(order.getPriceBeforeDiscount())
                .and().messageOf().benefits().and().benefitListOf().benefits(order.getBenefits())
                .and().messageOf().freebie().and().menuListOf().freebie(Benefit.FREEBIE.name(), order.isEligibleForFreebie())
                .and().messageOf().priceOfTotalBenefits().and().price().ofTotalBenefits(order.getPriceOfTotalBenefits())
                .and().messageOf().priceOfTotal().and().price().ofTotal(order.getPriceOfTotal())
                .and().messageOf().badge().and().nameOf().badge(order.getBadge().name()).build();
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
}
