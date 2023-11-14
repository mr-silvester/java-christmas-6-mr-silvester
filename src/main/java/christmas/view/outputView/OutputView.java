package christmas.view.outputView;

import christmas.context.annotation.Component;
import christmas.view.expressionBuilder.BenefitExpressionBuilder;
import christmas.view.expressionBuilder.MenuExpressionBuilder;
import christmas.view.expressionBuilder.MessageExpressionBuilder;
import christmas.view.expressionBuilder.PriceExpressionBuilder;

@Component
public class OutputView {
    private final StringBuilder output;

    public OutputView() {
        this.output = new StringBuilder();
    }

    public MessageExpressionBuilder messageOf() {
        return new MessageExpressionBuilder(this);
    }

    public MenuExpressionBuilder menuListOf() {
        return new MenuExpressionBuilder(this);
    }

    public BenefitExpressionBuilder benefitListOf() {
        return new BenefitExpressionBuilder(this);
    }

    public PriceExpressionBuilder price() {
        return new PriceExpressionBuilder(this);
    }

    public BenefitExpressionBuilder nameOf() {
        return new BenefitExpressionBuilder(this);
    }

    public OutputView merge(StringBuilder output) {
        if (output.toString().endsWith("\n")) {
            output.delete(output.length() - 1, output.length());
        }
        this.output.append(output);
        System.out.print(this.output);
        this.output.setLength(0);
        System.out.println();
        return this;
    }

    public void build() {
        System.out.print(this.output);
        output.setLength(0);
        System.out.println();
    }
}
