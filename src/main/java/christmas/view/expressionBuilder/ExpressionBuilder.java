package christmas.view.expressionBuilder;

import java.text.NumberFormat;
import java.util.Locale;

public interface ExpressionBuilder {
    int DEFAULT_QUANTITY_OF_FREEBIE = 1;
    String QUANTITY_UNIT = "개";
    String MONEY_UNIT = "원";
    String NOTHING_TO_PRINT = "없음";
    String NEWLINE = "\n";
    String SIGN_OF_NEGATIVE = "-";

    default String asMoney(int input) {
        return NumberFormat.getInstance(Locale.US).format(input);
    }
}
