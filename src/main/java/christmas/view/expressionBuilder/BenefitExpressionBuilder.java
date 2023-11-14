package christmas.view.expressionBuilder;

import christmas.view.outputView.OutputView;

public class BenefitExpressionBuilder implements ExpressionBuilder {
    public BenefitExpressionBuilder(OutputView outputView) {
        this.outputView = outputView;
        this.output = new StringBuilder();
    }

    private final OutputView outputView;
    private final StringBuilder output;

    public BenefitExpressionBuilder benefit(String benefitName, int discountAmount) {
        if (discountAmount != 0) {
            this.output.append(benefitName.replace("_", " ")).append(": -").append(asMoney(discountAmount)).append(MONEY_UNIT);
        }
        return this;
    }

    public BenefitExpressionBuilder badge(String badgeName) {
        this.output.append(badgeName);
        return this;
    }

    public BenefitExpressionBuilder and() {
        if (this.output.isEmpty()) {
            return this;
        }
        if (!this.output.toString().endsWith("\n")) {
            this.output.append("\n");
        }
        return this;
    }

    public BenefitExpressionBuilder orNone() {
        if (this.output.toString().replace("\n", "").isEmpty()) {
            this.output.setLength(0);
            this.output.append(NOTHING_TO_PRINT);
        }
        return this;
    }

    public void build() {
        this.outputView.merge(this.output).build();
    }
}
