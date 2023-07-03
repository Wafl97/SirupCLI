package org.example;

import sirup.cli.base.Arguments;
import sirup.cli.annotations.Arg;
import sirup.cli.annotations.Command;
import sirup.cli.annotations.ActionsClass;
import sirup.cli.annotations.Args;
import sirup.cli.inputs.Input;

@ActionsClass
public class TestCommands {

    @Command(command = "test1", alias = "t1", description = "This is test 1")
    @Args(value = {
        @Arg(flag = "a", arg = "name", description = "The name"),
        @Arg(flag = "b", arg = "amount", description = "The amount"),
        @Arg(flag = "c", description = "Copy output to clipboard"),
        @Arg(flag = "d", description = "Do a thing")
    })
    public static void Test1(Input input, Arguments arguments) {
        arguments.printArgs();
        arguments.ifContainsGet("-a", System.out::println);
        arguments.ifContainsGet("-b", System.out::println);
        arguments.ifContains("-c",
                    () -> System.out.println("Copy to clipboard"))
                .elseIfContains("-d",
                    () -> System.out.println("Do thing"))
                .elseDo(
                    () -> System.out.println("None of the above"));
    }
}
