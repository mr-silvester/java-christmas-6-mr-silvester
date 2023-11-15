package christmas.view.expressionBuilder;

import christmas.view.outputView.OutputView;

import java.util.List;
import java.util.Map;

public class MenuExpressionBuilder implements ExpressionBuilder {
    public MenuExpressionBuilder(OutputView outputView) {
        this.outputView = outputView;
        this.output = new StringBuilder();
    }

    private final OutputView outputView;

    private final StringBuilder output;

    private MenuExpressionBuilder menu(String menuName, int quantity) {
        if (quantity != 0) {
            this.output.append(menuName).append(" ").append(quantity).append(QUANTITY_UNIT);
        }
        return this;
    }

    public void menus(List<Map.Entry<String, Integer>> menus) {
        for (Map.Entry<String, Integer> menu : menus) {
            menu(menu.getKey(), menu.getValue()).and();
        }

        build();
    }

    public void freebie(String menuName, boolean isEligible) {
        int quantity = 0;
        if (isEligible) {
            quantity = 1;
        }
        menu(menuName, quantity).orNone().build();
    }

    public MenuExpressionBuilder and() {
        if (this.output.isEmpty()) {
            return this;
        }
        if (!this.output.toString().endsWith(NEWLINE)) {
            this.output.append(NEWLINE);
        }
        return this;
    }

    public MenuExpressionBuilder orNone() {
        if (this.output.toString().replace(NEWLINE, "").isEmpty()) {
            this.output.setLength(0);
            this.output.append(NOTHING_TO_PRINT);
        }
        return this;
    }

    public void build() {
        this.outputView.merge(this.output).build();
    }
}
