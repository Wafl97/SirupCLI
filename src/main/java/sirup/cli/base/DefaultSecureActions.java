package sirup.cli.base;

import sirup.cli.annotations.Command;
import sirup.cli.annotations.SecureActionsClass;
import sirup.cli.inputs.Input;

@SecureActionsClass
public class DefaultSecureActions {

    @Command(command = "logout", description = "Logout the current user")
    public static void logout(Input input, Arguments arguments) {
        SirupCli.logout();
        SirupCli.clearScreen();
    }

}
