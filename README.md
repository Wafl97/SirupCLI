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
        .skipHeader() // but you would never do this :^)
        .start();
}
````

````java
public static boolean loginHandler(Input input) {
    //Your login logic
    return loginSuccess;
}
````

### Add CLI command

````java
import sirup.cli.annotations.Commands;
import sirup.cli.base.CommandClass;

@Commands
public class MyCommandClass extends CommandClass {
    @CliAction(command = "command", alias = "c", description = "This is my command")
    public static void myCommand() {
        //Command logic
    }
}
````

### Add command with args

````java

import sirup.cli.annotations.Arg;
import sirup.cli.annotations.Args;
import sirup.cli.annotations.Command;
import sirup.cli.annotations.Commands;
import sirup.cli.base.CommandClass;

@Commands
public class MyCommandClass extends CommandClass {
    @Command(command = "command", alias = "c", description = "This is my command")
    @Args(value = {
            @Arg(flag = "a", arg = "Name or type", description = "This will look for '-a' and whatever follows"),
            @Arg(flag = "b")
    })
    public static void myCommand() {
        //Command logic
    }
}
````

### Add secure CLI command

Secure commands will only be parsed if a login-handler has been added.

````java


import sirup.cli.annotations.SecureActionsClass;
import sirup.cli.base.CommandClass;

@SecureActionsClass
public class MySecureCommandClass extends CommandClass {
    @CliAction(command = "secure_command", alias = "sc", description = "This is my secure command")
    public static void mySecureCommand() {
        //Secure command logic
    }
}
````

## CommandClass

The CommandClass provides method for checking if different arguments are present for the different commands.

````java
import sirup.cli.annotations.Commands;
import sirup.cli.base.CommandClass;

@Commands
public class MyCommandClass extends CommandClass {
    @CliAction(command = "command", alias = "c", description = "This is my command")
    @Args(value = {
            @Arg(flag = "a", arg = "Name or type", description = "This will look for '-a' and whatever follows"),
            @Arg(flag = "b")
    })
    public static void myCommand() {
        with("a", a -> {
            // 'a' is the value of the argument
        });

        when("b" () -> {
            // 'b' will never have a value
        });
    }
}
````

These can be chained to check for the presents of different argument in sequence

````java
use("a", a -> {})
.elseUse("x", x -> {})
.elseWhen("c", () -> {})
.elseDo(() -> {});

when("b", () -> {})
.elseUse("z", z -> {})
.elseWhen("y", () -> {})
.elseDo(() -> {});
````

In addition to these argument checking method, there are also methods for reading inputs during the command, both for normal text and for passwords

````java
String line = readLine();
String secret = readPassword();
````