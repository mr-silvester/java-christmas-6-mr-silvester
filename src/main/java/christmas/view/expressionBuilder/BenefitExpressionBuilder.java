package christmas.view.expressionBuilder;

import christmas.view.outputView.OutputView;

import java.util.List;
import java.util.Map;

public class BenefitExpressionBuilder implements ExpressionBuilder {
    private static final String UNDER_BAR = "_";
    private static final String MENU_AND_DISCOUNT_DELIMITER = ": ";

    public BenefitExpressionBuilder(OutputView outputView, StringBuilder output) {
        this.outputView = outputView;
        this.output = output;
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

    public BenefitExpressionBuilder benefits(List<Map.Entry<String, Integer>> benefits) {
        if (benefits.size() == 0) {
            this.output.append(NOTHING_TO_PRINT).append(NEWLINE);
            return this;
        }

        for (Map.Entry<String, Integer> benefit : benefits) {
            benefit(benefit.getKey(), benefit.getValue()).and();
        }
        return this;
    }

    public BenefitExpressionBuilder badge(String badgeName) {
        this.output.append(badgeName);
        return this;
    }

    public OutputView and() {
        System.out.print(this.output);
        this.output.setLength(0);
        System.out.println();
        return this.outputView;
    }

    public void build() {
        System.out.println(this.output);
        this.output.setLength(0);
    }
}
