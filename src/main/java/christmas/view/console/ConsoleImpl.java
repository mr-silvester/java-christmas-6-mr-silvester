package christmas.view.console;

import christmas.context.annotation.Component;
import christmas.view.inputView.InputView;
import christmas.view.outputView.OutputView;

@Component
public class ConsoleImpl implements Console {
    public ConsoleImpl(OutputView outputView, InputView inputView) {
        this.outputView = outputView;
        this.inputView = inputView;
    }

    private final OutputView outputView;
    private final InputView inputView;

    @Override
    public OutputView print() {
        return outputView;
    }

    @Override
    public InputView read() {
        return inputView;
    }

    @Override
    public void close() {
        inputView.close();
    }
}
