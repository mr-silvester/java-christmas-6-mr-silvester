package christmas.view.expressionBuilder;

import java.text.NumberFormat;
import java.util.Locale;

public interface ExpressionBuilder {
    String QUANTITY_UNIT = "개";
    String MONEY_UNIT = "원";
    String NOTHING_TO_PRINT = "없음";

    default String asMoney(int input) {
        return NumberFormat.getInstance(Locale.US).format(input);
    }
}
