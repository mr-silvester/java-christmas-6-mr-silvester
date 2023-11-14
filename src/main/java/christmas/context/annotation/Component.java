package christmas.context.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Scan
public @interface Component {
    String name() default "";
}
