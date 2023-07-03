package sirup.cli.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Args.class)
public @interface Arg {
    String flag();
    String arg() default "";
    String description() default "";
}