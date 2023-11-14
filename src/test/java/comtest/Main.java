package comtest;

import sirup.cli.base.SirupCli;

public class Main {
    public static void main(String[] args) {
        new SirupCli(Main.class.getPackageName()).start();
    }
}
