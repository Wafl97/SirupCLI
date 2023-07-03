package org.example;

import sirup.cli.annotations.*;
import sirup.cli.base.CommandClass;

@ActionsClass
public class TestCommands /*extends CommandClass*/ {

    @Command(command = "test1", alias = "t1", description = "This is test 1")
    @Args(value = {
        @Arg(flag = "a", arg = "name", description = "The name"),
        @Arg(flag = "b", arg = "amount", description = "The amount"),
        @Arg(flag = "c", description = "Copy output to clipboard"),
        @Arg(flag = "d", description = "Do a thing")
    })
    public void Test1() {
        /*use("a", a -> {
            System.out.println("Name <" + a + ">");
        });
        use("b", b -> {
            System.out.println("Amount <" + b + ">");
        });
        when("c", () -> System.out.println("Copy"))
            .elseWhen("d", () -> System.out.println("Do thing"))
            .elseDo(() -> System.out.println("None"));*/
    }
}
