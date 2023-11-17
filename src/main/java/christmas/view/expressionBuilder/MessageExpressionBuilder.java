package christmas.view.expressionBuilder;

import christmas.view.outputView.OutputView;

public class MessageExpressionBuilder implements ExpressionBuilder {
    public MessageExpressionBuilder(OutputView outputView) {
        this.outputView = outputView;
    }

    private final OutputView outputView;

    public void initial() {
        System.out.println("안녕하세요! 우테코 식당 12월 이벤트 플래너입니다.");
    }

    public MessageExpressionBuilder previewEventBenefits(int date) {
        System.out.println("12월 " + date + "일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!");
        return this;
    }

    public OutputView and() {
        System.out.println();
        return this.outputView;
    }
}
