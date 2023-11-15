package christmas.view.expressionBuilder;

import christmas.view.outputView.OutputView;

import java.util.List;
import java.util.Map;

public class BenefitExpressionBuilder implements ExpressionBuilder {
    private static final String UNDER_BAR = "_";
    private static final String MENU_AND_DISCOUNT_DELIMITER = ": ";

    public BenefitExpressionBuilder(OutputView outputView) {
        this.outputView = outputView;
        this.output = new StringBuilder();
    }

    private final OutputView outputView;
    private final StringBuilder output;

    private BenefitExpressionBuilder benefit(String benefitName, int discountAmount) {
        if (discountAmount != 0) {
            this.output.append(benefitName.replace(UNDER_BAR, " "))
                    .append(MENU_AND_DISCOUNT_DELIMITER)
                    .append(SIGN_OF_NEGATIVE)
                    .append(asMoney(discountAmount))
                    .append(MONEY_UNIT);
        }
        return this;
    }

    public void benefits(List<Map.Entry<String, Integer>> benefits) {
        for (Map.Entry<String, Integer> benefit : benefits) {
            benefit(benefit.getKey(), benefit.getValue()).and();
        }

        orNone().build();
    }

    public void badge(String badgeName) {
        this.output.append(badgeName);
        build();
    }

    public BenefitExpressionBuilder and() {
        if (this.output.isEmpty()) {
            return this;
        }
        if (!this.output.toString().endsWith(NEWLINE)) {
            this.output.append(NEWLINE);
        }
        return this;
    }

    public BenefitExpressionBuilder orNone() {
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
