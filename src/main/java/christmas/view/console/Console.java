package christmas.view.console;

import christmas.view.inputView.InputView;
import christmas.view.outputView.OutputView;

public interface Console {
    OutputView print();

    InputView read();

    void close();
}
