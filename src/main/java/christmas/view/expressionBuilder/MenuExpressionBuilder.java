package christmas.view.expressionBuilder;

import christmas.view.outputView.OutputView;

import java.util.List;
import java.util.Map;

public class MenuExpressionBuilder implements ExpressionBuilder {
    public MenuExpressionBuilder(OutputView outputView) {
        this.outputView = outputView;
    }

    private final OutputView outputView;

    private MenuExpressionBuilder appendMenu(String menuName, int quantity) {
        if (quantity != 0) {
            System.out.println(menuName + " " + quantity + QUANTITY_UNIT);
        }
        return this;
    }

    public MenuExpressionBuilder of(List<Map.Entry<String, Integer>> menus) {
        System.out.println("<주문 메뉴>");

        if (menus.size() == 0) {
            System.out.println(NOTHING_TO_PRINT);
            return this;
        }

        for (Map.Entry<String, Integer> menu : menus) {
            appendMenu(menu.getKey(), menu.getValue());
        }

        return this;
    }

    public OutputView and() {
        System.out.println();
        return this.outputView;
    }
}
