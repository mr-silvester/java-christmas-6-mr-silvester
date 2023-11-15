package christmas.view.expressionBuilder;

import christmas.view.outputView.OutputView;

public class PriceExpressionBuilder implements ExpressionBuilder {
    public PriceExpressionBuilder(OutputView outputView) {
        this.outputView = outputView;
        this.output = new StringBuilder();
    }

    private final OutputView outputView;
    private final StringBuilder output;

    public void beforeDiscount(int price) {
        this.output.append(asMoney(price)).append(MONEY_UNIT);
        build();
    }

    public void ofTotalBenefits(int price) {
        String sign = "";
        if (price > 0) {
            sign = SIGN_OF_NEGATIVE;
        }
        this.output.append(sign).append(asMoney(price)).append(MONEY_UNIT);
        build();
    }

    public void ofTotal(int price) {
        this.output.append(asMoney(price)).append(MONEY_UNIT);
        build();
    }

    public void build() {
        this.outputView.merge(this.output).build();
    }
}
