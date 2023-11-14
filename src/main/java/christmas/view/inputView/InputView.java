package christmas.view.inputView;

import camp.nextstep.edu.missionutils.Console;
import christmas.context.annotation.Component;
import christmas.enums.error.Error;

@Component
public class InputView {
    private static final String DAY_PATTERN = "^(0?[1-9]|1\\d|2[0-9]|3[0-1])$";
    private static final String MENU_PATTERN = "^[ㄱ-힣0-9,-]+$";

    public Integer day() {
        System.out.println("12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)");
        String input = Console.readLine();
        if (!input.matches(DAY_PATTERN)) {
            throw new IllegalArgumentException(Error.INVALID_DATE.getMessage());
        }
        return Integer.parseInt(input);
    }

    public String menus() {
        System.out.println("주문하실 메뉴를 메뉴와 개수를 알려 주세요. (e.g. 해산물파스타-2,레드와인-1,초코케이크-1)");
        String input = Console.readLine();
        if (!input.matches(MENU_PATTERN)) {
            throw new IllegalArgumentException(Error.INVALID_ORDER.getMessage());
        }
        return input;
    }

    public void close() {
        Console.close();
    }
}