package org.example;

import sirup.cli.base.Arguments;
import sirup.cli.annotations.*;
import sirup.cli.inputs.Input;

@SecureActionsClass
public class TestSecure {

    @RequiresLogin
    @Command(command = "secure_test", alias = "st", description = "This is a secure test")
    @Args(value = {
            @Arg(flag = "a", arg = "amount", description = "The amount of a thing... idk"),
            @Arg(flag = "v", arg = "value", description = "Some value"),
            @Arg(flag = "x", arg = "extra", description = "Some extra thing")
    })
    public static void SecureTest1(Input input, Arguments arguments) {
        arguments.ifContainsGet("-a", a -> {
            System.out.println("-a is: " + a);
        }).elseIfContainsGet("-x", x -> {
            System.out.println("getting file from " + x);
        }).elseDo(() -> {
            System.out.println("-a was not set");
        });

        arguments.ifContainsAll(new String[]{"-a","-x"}, () -> {
            System.out.println("Contains both");
        });

        arguments.ifContainsAllGet(new String[]{"-a","-v"}, (v) -> {
            System.out.println("-a : " + v[0]);
            System.out.println("-v : " + v[1]);
        });
    }
}
