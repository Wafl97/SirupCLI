package sirup.cli.base;

import sirup.cli.annotations.Command;
import sirup.cli.annotations.SecureActionsClass;

@SecureActionsClass
public class DefaultSecureActions extends CommandClass {

    @Command(command = "logout", description = "Logout the current user")
    public static void logout() {
        SirupCli.logout();
        SirupCli.clearScreen();
    }

}
