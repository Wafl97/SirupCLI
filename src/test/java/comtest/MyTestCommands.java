package comtest;

import sirup.cli.annotations.Arg;
import sirup.cli.annotations.Args;
import sirup.cli.annotations.Command;
import sirup.cli.annotations.Commands;
import sirup.cli.base.CommandClass;

import java.io.File;

@Commands
public class MyTestCommands extends CommandClass {

    @Command(command = "com1", description = "This is command 1")
    @Args(value = {
            @Arg(flag = "i", arg = "file", description = "The input file"),
            @Arg(flag = "o", arg = "file", description = "The output file")
    })
    public void command1() {


        with("i", i -> {
        with("o", o -> {
            writeFile(processFile(readFile(i)),o);
        }).elseDo(() -> System.out.println("Missing output file"));
        }).elseDo(() -> System.out.println("Missing input file"));
    }

    private File readFile(String fileName) {
        System.out.println("Reading " + fileName);
        return new File(fileName);
    }

    private File processFile(File file) {
        System.out.println("Processing file...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void writeFile(File file, String fileName) {
        System.out.println("Saving to " + fileName);
    }
}
