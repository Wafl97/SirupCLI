package org.example;

import org.example.experimental.NewTestCommands;
import sirup.cli.base.SirupCli;
import sirup.cli.inputs.Input;

public class Main {

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        new SirupCli(Main.class.getPackageName())
                .addLoginHandler(Main::loginHandler)
                .addWelcomeMessage(() -> System.out.println("Hello " + username))
                .addGoodbyeMessage(() -> System.out.println("Soo long " + username))
                //.skipHeader()
                .start();
    }

    private static String username;
    private static boolean loginHandler(Input input) {
        System.out.print("Username: ");
        username = input.readLine();
        System.out.print("Password: ");
        String p = input.readPassword();
        return username.equals("admin") && p.equals("1234");
    }
}
