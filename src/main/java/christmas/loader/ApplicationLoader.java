package christmas.loader;

import christmas.context.applicationContext.ApplicationContext;
import christmas.context.implementation.DefaultApplicationContext;
import christmas.controller.Controller;

public class ApplicationLoader {
    private static final String BASE_PACKAGE = "christmas";
    private static final ApplicationContext context = new DefaultApplicationContext(BASE_PACKAGE);

    public static void run() {
        Controller controller = context.getBean(Controller.class);
        controller.run();
    }
}
