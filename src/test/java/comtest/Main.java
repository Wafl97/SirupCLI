package comtest;

import comtest.handler.LoginHandler;
import sirup.cli.annotations.Command;
import sirup.cli.annotations.Commands;
import sirup.cli.annotations.Example;
import sirup.cli.annotations.Examples;
import sirup.cli.base.CommandClass;
import sirup.cli.base.SirupCli;

@Commands
public class Main extends CommandClass {
    public static void main(String[] args) {
        new SirupCli(Main.class.getPackageName()).addLoginHandler(new LoginHandler()).skipHeader().start();
    }

    @Command(command = "com2", description = "Command 2")
    @Examples(value = {
        @Example("com2 -sdfsdf"),
        @Example("com2 -sdfsdfsdf")
    })
    public void command1() {

    }
}
