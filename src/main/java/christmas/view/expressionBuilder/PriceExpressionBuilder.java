package christmas.view.expressionBuilder;

import christmas.view.outputView.OutputView;

public class PriceExpressionBuilder implements ExpressionBuilder {
    public PriceExpressionBuilder(OutputView outputView) {
        this.outputView = outputView;
    }

    private final OutputView outputView;

    public PriceExpressionBuilder beforeDiscount(int price) {
        System.out.println("<할인 전 총주문 금액>");
        System.out.println(asMoney(price) + MONEY_UNIT);
        return this;
    }

    public PriceExpressionBuilder ofTotalBenefits(int price) {
        System.out.println("<총혜택 금액>");
        String sign = "";

        if (price > 0) {
            sign = SIGN_OF_NEGATIVE;
        }

        System.out.println(sign + asMoney(price) + MONEY_UNIT);
        return this;
    }

    public PriceExpressionBuilder ofTotal(int price) {
        System.out.println("<할인 후 예상 결제 금액>");
        System.out.println(asMoney(price) + MONEY_UNIT);
        return this;
    }

    public OutputView and() {
        System.out.println();
        return this.outputView;
    }
}
