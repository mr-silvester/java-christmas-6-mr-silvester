package christmas.view.outputView;

import christmas.context.annotation.Component;
import christmas.view.expressionBuilder.BenefitExpressionBuilder;
import christmas.view.expressionBuilder.MenuExpressionBuilder;
import christmas.view.expressionBuilder.MessageExpressionBuilder;
import christmas.view.expressionBuilder.PriceExpressionBuilder;

@Component
public class OutputView {
    public MenuExpressionBuilder menuList() {
        return new MenuExpressionBuilder(this);
    }

    public BenefitExpressionBuilder benefitList() {
        return new BenefitExpressionBuilder(this);
    }

    public MessageExpressionBuilder messageOf() {
        return new MessageExpressionBuilder(this);
    }

    public PriceExpressionBuilder price() {
        return new PriceExpressionBuilder(this);
    }

    public BenefitExpressionBuilder badge() {
        return new BenefitExpressionBuilder(this);
    }

    public BenefitExpressionBuilder freebie() {
        return new BenefitExpressionBuilder(this);
    }
}
