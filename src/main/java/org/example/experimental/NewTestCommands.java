package org.example.experimental;

import sirup.cli.annotations.*;
import sirup.cli.base.CommandClass;

@ActionsClass
public class NewTestCommands /*extends CommandClass*/ {

    @Command(command = "new_test", alias = "nt", description = "The new way of using the CLI tool")
    @Args(value = {
        @Arg(flag = "a", description = "You enter a thing"),
        @Arg(flag = "x", arg = "extra", description = "The extra thing"),
        @Arg(flag = "d", arg = "destination", description = "The output destination"),
        @Arg(flag = "c", description = "Copy the output to clipboard")
    })
    public void newTestCommand() {
        /*when("a", () -> {
            System.out.print("Enter thing: ");
            String in = readLine();
            System.out.println("You entered: <" + in + ">");
        });

        use("x", x -> {
            System.out.println(x);
        })
        .elseUse("d", d -> {
            System.out.println("sdfdsf " + d);
        })
        .elseWhen("c", () -> {
            System.out.println("copy");
        })
        .elseDo(() -> {
            System.out.println("none of the things");
        });*/
    }
}
