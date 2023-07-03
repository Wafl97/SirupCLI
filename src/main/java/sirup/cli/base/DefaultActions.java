package sirup.cli.base;

import sirup.cli.annotations.Command;
import sirup.cli.annotations.ActionsClass;
import sirup.cli.inputs.Input;

import java.util.List;

@ActionsClass
public class DefaultActions {

    private static List<SirupCli.CliObject> cliObjects;

    public static void setCliObjects(List<SirupCli.CliObject> cliObjects) {
        DefaultActions.cliObjects = cliObjects;
    }

    @Command(command = "help", alias = "?", description = "Get a list of all the commands")
    public static void printHelp(Input input, Arguments arguments) {
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
            if (cliObject.args().size() > 0) {
                System.out.println("\tcommand options:");
                cliObject.args().forEach(cliArg -> {
                    System.out.print("\t-" + cliArg.flag());
                    if (!cliArg.arg().isEmpty()) {
                        System.out.print(" <" + cliArg.arg() + ">");
                    }
                    if (!cliArg.description().isEmpty()) {
                        System.out.println(" -> " + cliArg.description());
                    }
                });
            }
        });
    }

    @Command(command = "quit", alias = "q", description = "Closes the program")
    public static void quit(Input input, Arguments arguments) {
        SirupCli.stop();
    }

    @Command(command = "clear", alias = "c", description = "Clears the console window")
    public static void clearScreen(Input input, Arguments arguments) {
        SirupCli.clearScreen();
    }
}
