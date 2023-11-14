package sirup.cli.base;

import sirup.cli.annotations.Command;
import sirup.cli.annotations.Commands;

import java.util.Set;

@Commands
public class DefaultActions extends CommandClass {

    public DefaultActions() {}

    private static Set<SirupCli.CliObject> cliObjects;

    public static void setCliObjects(Set<SirupCli.CliObject> cliObjects) {
        DefaultActions.cliObjects = cliObjects;
    }

    @Command(command = "help", alias = "?", description = "Get a list of all the commands")
    public static void printHelp() {
        System.out.println("Commands:");
        DefaultActions.cliObjects.forEach(cliObject -> {
            if (cliObject.alias() != null && !cliObject.alias().isEmpty()) {
                System.out.print(String.join(", ", cliObject.command(), cliObject.alias()));
            }
            else {
                System.out.print(cliObject.command());
            }
            if (cliObject.description() != null && !cliObject.description().isEmpty()) {
                System.out.println(" -> " + cliObject.description());
            }
            if (!cliObject.args().isEmpty()) {
                System.out.println("\tcommand options:");
                cliObject.args().forEach(cliArg -> {
                    System.out.print("\t-" + cliArg.flag());
                    if (!cliArg.arg().isEmpty()) {
                        System.out.print(" <" + cliArg.arg() + ">");
                    }
                    if (!cliArg.description().isEmpty()) {
                        System.out.print(" -> " + cliArg.description());
                    }
                    System.out.println();
                });
            }
            if (!cliObject.example().isEmpty()) {
                System.out.println("\texample(s): [");
                cliObject.example().forEach(example -> System.out.println("\t\t" + example));
                System.out.println("\t]");
            }
        });
    }

    @Command(command = "quit", alias = "q", description = "Closes the program")
    public static void quit() {
        SirupCli.stop();
    }

    @Command(command = "clear", alias = "c", description = "Clears the console window")
    public static void clearScreen() {
        SirupCli.clearScreen();
    }
}
