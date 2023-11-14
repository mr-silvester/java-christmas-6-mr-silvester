package christmas.view.expressionBuilder;

import christmas.view.outputView.OutputView;

public class MessageExpressionBuilder implements ExpressionBuilder {
    public MessageExpressionBuilder(OutputView outputView) {
        this.outputView = outputView;
        this.output = new StringBuilder();
    }

    private final OutputView outputView;
    private final StringBuilder output;

    public void beginning() {
        System.out.println(this.output.append("안녕하세요! 우테코 식당 12월 이벤트 플래너입니다."));
    }

    public void previewEvent(int date) {
        this.output.append("12월 ").append(date).append("일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!");
        build();
    }

    public MessageExpressionBuilder orderedMenus() {
        output.append("<주문 메뉴>");
        return this;
    }

    public MessageExpressionBuilder originalTotalPrice() {
        output.append("<할인 전 총주문 금액>");
        return this;
    }

    public MessageExpressionBuilder freebie() {
        output.append("<증정 메뉴>");
        return this;
    }

    public MessageExpressionBuilder benefits() {
        output.append("<혜택 내역>");
        return this;
    }

    public MessageExpressionBuilder totalBenefits() {
        output.append("<총혜택 금액>");
        return this;
    }

    public MessageExpressionBuilder totalPrice() {
        output.append("<할인 후 예상 결제 금액>");
        return this;
    }

    public MessageExpressionBuilder badge() {
        output.append("<12월 이벤트 배지>");
        return this;
    }

    public OutputView and() {
        return this.outputView.merge(this.output);
    }

    public void build() {
        this.outputView.merge(this.output).build();
    }
}
