package org.example.experimental;

import sirup.cli.annotations.Command;
import sirup.cli.experimental.CommandClass;

public class NewTestCommands extends CommandClass {

    @Command(command = "new_test", alias = "nt", description = "The new way of using the CLI tool")
    public void newTestCommand() {
        when("-a", () -> {
            String in = readLine();
        });

        use("-x", x -> {})
            .elseUse("-d",d -> {})
            .elseWhen("-c", () -> {})
            .elseUse("-y", y -> {});

    }
}
