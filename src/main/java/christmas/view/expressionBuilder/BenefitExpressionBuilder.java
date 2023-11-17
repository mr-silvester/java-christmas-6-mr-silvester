package christmas.view.expressionBuilder;

import christmas.view.outputView.OutputView;

import java.util.List;
import java.util.Map;

public class BenefitExpressionBuilder implements ExpressionBuilder {
    private static final String UNDER_BAR = "_";
    private static final String MENU_AND_DISCOUNT_DELIMITER = ": ";

    public BenefitExpressionBuilder(OutputView outputView) {
        this.outputView = outputView;
    }

    private final OutputView outputView;

    private BenefitExpressionBuilder appendBenefit(String benefitName, int discountAmount) {
        if (discountAmount != 0) {
            System.out.println(benefitName.replace(UNDER_BAR, " ")
                    + MENU_AND_DISCOUNT_DELIMITER
                    + SIGN_OF_NEGATIVE
                    + asMoney(discountAmount)
                    + MONEY_UNIT);
        }
        return this;
    }

    public BenefitExpressionBuilder of(List<Map.Entry<String, Integer>> benefits) {
        System.out.println("<혜택 내역>");

        if (benefits.size() == 0) {
            System.out.println(NOTHING_TO_PRINT);
            return this;
        }

        for (Map.Entry<String, Integer> benefit : benefits) {
            appendBenefit(benefit.getKey(), benefit.getValue());
        }

        return this;
    }

    public BenefitExpressionBuilder of(String freebieName, boolean isEligible) {
        System.out.println("<증정 메뉴>");

        if (isEligible) {
            System.out.println(freebieName + " " + DEFAULT_QUANTITY_OF_FREEBIE + QUANTITY_UNIT);
            return this;
        }

        System.out.println(NOTHING_TO_PRINT);

        return this;
    }

    public BenefitExpressionBuilder of(String freebieName) {
        System.out.println("<12월 이벤트 배지>");
        System.out.println(freebieName);
        return this;
    }

    public OutputView and() {
        System.out.println();
        return this.outputView;
    }
}
