package christmas.view.expressionBuilder;

import christmas.view.outputView.OutputView;

import java.util.List;
import java.util.Map;

public class MenuExpressionBuilder implements ExpressionBuilder {
    public MenuExpressionBuilder(OutputView outputView, StringBuilder output) {
        this.outputView = outputView;
        this.output = output;
    }

    private final OutputView outputView;

    private final StringBuilder output;

    private MenuExpressionBuilder menu(String menuName, int quantity) {
        if (quantity != 0) {
            this.output.append(menuName).append(" ").append(quantity).append(QUANTITY_UNIT);
        }
        return this;
    }

    public MenuExpressionBuilder menus(List<Map.Entry<String, Integer>> menus) {
        if (menus.size() == 0) {
            this.output.append(NOTHING_TO_PRINT);
            return this;
        }

        for (Map.Entry<String, Integer> menu : menus) {
            menu(menu.getKey(), menu.getValue()).and();
        }

        return this;
    }

    public MenuExpressionBuilder freebie(String menuName, boolean isEligible) {
        if (isEligible) {
            menu(menuName, 1);
            this.output.append(NEWLINE);
            return this;
        }

        this.output.append(NOTHING_TO_PRINT).append(NEWLINE);
        return this;
    }

    public OutputView and() {
        System.out.print(this.output);
        this.output.setLength(0);
        System.out.println();
        return this.outputView;
    }
}
