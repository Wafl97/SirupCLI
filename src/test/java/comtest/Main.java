package comtest;

import sirup.cli.annotations.Command;
import sirup.cli.annotations.Commands;
import sirup.cli.base.CommandClass;
import sirup.cli.base.SirupCli;

@Commands
public class Main extends CommandClass {
    public static void main(String[] args) {
        new SirupCli(Main.class.getPackageName()).start();
    }

    @Command(command = "com2", description = "Command 2")
    public void command1() {

    }
}
