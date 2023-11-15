package christmas.view.expressionBuilder;

import christmas.view.outputView.OutputView;

public class PriceExpressionBuilder implements ExpressionBuilder {
    public PriceExpressionBuilder(OutputView outputView, StringBuilder output) {
        this.outputView = outputView;
        this.output = output;
    }

    private final OutputView outputView;
    private final StringBuilder output;

    public PriceExpressionBuilder beforeDiscount(int price) {
        this.output.append(asMoney(price)).append(MONEY_UNIT).append(NEWLINE);
        return this;
    }

    public PriceExpressionBuilder ofTotalBenefits(int price) {
        String sign = "";
        if (price > 0) {
            sign = SIGN_OF_NEGATIVE;
        }
        this.output.append(sign).append(asMoney(price)).append(MONEY_UNIT).append(NEWLINE);
        return this;
    }

    public PriceExpressionBuilder ofTotal(int price) {
        this.output.append(asMoney(price)).append(MONEY_UNIT).append(NEWLINE);
        return this;
    }

    public OutputView and() {
        System.out.print(this.output);
        this.output.setLength(0);
        System.out.println();
        return this.outputView;
    }
}
