package org.example;

import sirup.cli.annotations.*;
import sirup.cli.base.CommandClass;

@SecureActionsClass
public class TestSecure /*extends CommandClass*/ {

    @Command(command = "secure_test", alias = "st", description = "This is a secure test")
    @Args(value = {
            @Arg(flag = "a", arg = "amount", description = "The amount of a thing... idk"),
            @Arg(flag = "v", arg = "value", description = "Some value"),
            @Arg(flag = "x", arg = "extra", description = "Some extra thing")
    })
    public void SecureTest1() {
        /*use("a", a -> {
            System.out.println("-a is: " + a);
        }).elseUse("x", x -> {
            System.out.println("getting file from " + x);
        }).elseDo(() -> {
            System.out.println("-a was not set");
        });*/
    }
}
