package comtest;

import sirup.cli.annotations.*;
import sirup.cli.base.CommandClass;

@Commands
public class UserCommands extends CommandClass {

    @Command(command = "user_com_1", alias = "uc1", description = "This is user command 1")
    @Args(value = {
            @Arg(flag = "i", arg = "input file"),
            @Arg(flag = "o", arg = "output file")
    })
    @Example("uc1 -i file.txt -o file.json")
    public void userCommand1() {
        with("i", i -> {
           System.out.println("In: " + i);
           with("o", o -> {
               System.out.println("Out: " + o);
           }).elseDo(() -> System.out.println("Missing output"));
        }).elseDo(() -> System.out.println("Missing input"));
    }

    @Command(command = "user_com_2", alias = "uc2", description = "This is user command 2")
    public void userCommand2() {
        System.out.println("user:");
        System.out.println("Loading...");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("{\nusername: Wafl\ncreated: 01/01/2000\n}");
    }
}
