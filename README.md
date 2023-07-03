# Sirup CLI

## Usage

### Start CLI

````java
public static void main(String[]args){
    String pack = "org.example"; //Your project groupId
    SirupCli cli = new SirupCli(pack);
    cli.start();
}
````

### Add login

````java
public static void main(String[]args){
    String pack = "org.example"; //Your project groupId
    new SirupCli(pack)
        .addLoginHandler(Main::loginHandler)
        .start();
}
````

````java
public static boolean loginHandler(Concole console) {
    //Your login logic
    return loginSuccess;
}
````

### Add CLI command

````java
import sirup.cli.annotations.CliAction;
import sirup.cli.annotations.CliActionsClass;

import java.io.Console;

@CliActionsClass
public class MyCommandClass {
    @CliAction(command = "command", alias = "c", description = "This is my command")
    public static void myCommand(Console console, Map<String, String> argsMap) {
        //Command logic
    }
}
````

### Add command with args

````java
import sirup.cli.annotations.CliAction;
import sirup.cli.annotations.CliActionsClass;
import sirup.cli.annotations.CliArgs;

import java.io.Console;

@CliActionsClass
public class MyCommandClass {
    @CliAction(command = "command", alias = "c", description = "This is my command")
    @Args(value = {
            @CliArgs.CliArg(flag = "a", arg = "Name or type", description = "This will look for '-a' and whatever follows"),
            @CliArgs.CliArg(flag = "b")
    })
    public static void myCommand(Console console, Map<String, String> argsMap) {
        //Command logic
    }
}
````

### Add secure CLI command

Secure commands will only be parsed if a login-handler has been added.

````java
import sirup.cli.annotations.CliAction;
import sirup.cli.annotations.CliSecureActionsClass;

import java.io.Console;

@CliSecureActionsClass
public class MySecureCommandClass {
    @CliAction(command = "secure_command", alias = "sc", description = "This is my secure command")
    public static void mySecureCommand(Console console) {
        //Secure command logic
    }
}
````