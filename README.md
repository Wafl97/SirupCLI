# Sirup CLI

## Usage

### Start CLI

````java
package org.example;

import sirup.cli.base.SirupCli;

public class Main {
    public static void main(String[]args){
        String pack = "org.example"; //Your project groupId
        SirupCli cli = new SirupCli(pack);
        cli.start();
    }
}
````

### Add login

````java
package org.example;

import sirup.cli.base.SirupCli;

public class Main {
    public static void main(String[] args) {
        String pack = "org.example"; //Your project groupId
        new SirupCli(pack)
                .addLoginHandler(Main::loginHandler)
                .skipHeader() // but you would never do this :^)
                .start();
    }

    public static boolean loginHandler(Input input) {
        //Your login logic
        return false;
    }
}
````

Alternative way to create login handler via class

````java
package org.example.handler;

import sirup.cli.base.SirupCli;
import sirup.cli.inputs.Input;

public class LoginHandler implements SirupCli.LoginHandler {
    @Override
    public boolean login(Input input) {
        //Your login logic
        return false;
    }
}
````

````java
package org.example;

import sirup.cli.base.SirupCli;
import sirup.cli.inputs.Input;
import org.example.handler.LogingHandler;

public class Main {
    public static void main(String[] args) {
        String pack = "org.example"; //Your project groupId
        new SirupCli(pack)
                .addLoginHandler(new LoginHandler())
                .skipHeader() // but you would never do this :^)
                .start();
    }
}
````

### Add CLI command

````java
package org.example.commands;

import sirup.cli.annotations.Commands;
import sirup.cli.base.CommandClass;

@Commands
public class MyCommandClass extends CommandClass {
    @CliAction(command = "command", alias = "c", description = "This is my command")
    public void myCommand() {
        //Command logic
    }
}
````

### Add command with args

````java
package org.example.commands;

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
    public void myCommand() {
        //Command logic
    }
}
````

### Add examples

````java
package org.example.commands;

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
    //@Example can also be used for a single example
    @Examples(value = {
        @Example("command -a \"some input here\""),
        @Example("c -b")
    })
    public void myCommand() {
        //Command logic
    }
}
````

### Add secure CLI command

Secure commands will only be parsed if a login-handler has been added.

````java
package org.example.commands;

import sirup.cli.annotations.SecureActionsClass;
import sirup.cli.base.CommandClass;

@SecureActionsClass
public class MySecureCommandClass extends CommandClass {
    @CliAction(command = "secure_command", alias = "sc", description = "This is my secure command")
    public void mySecureCommand() {
        //Secure command logic
    }
}
````

## CommandClass

The CommandClass provides method for checking if different arguments are present for the different commands.

````java
package org.example.commands;

import sirup.cli.annotations.Commands;
import sirup.cli.base.CommandClass;

@Commands
public class MyCommandClass extends CommandClass {
    @CliAction(command = "command", alias = "c", description = "This is my command")
    @Args(value = {
            @Arg(flag = "a", arg = "Name or type", description = "This will look for '-a' and whatever follows"),
            @Arg(flag = "b")
    })
    public void myCommand() {
        with("a", a -> {
            // 'a' is the value of the argument
        });

        when("b", () -> {
            // 'b' will never have a value
        });
    }
}
````

These can be chained to check for the presents of different argument in sequence

````java
package org.example.commands;

import sirup.cli.base.CommandClass;

public class Examples extends CommandClass {
    public void exampleCommand() {
        use("a", a -> {})
            .elseUse("x", x -> {})
            .elseWhen("c", () -> {})
            .elseDo(() -> {});

        when("b", () -> {})
            .elseUse("z", z -> {})
            .elseWhen("y", () -> {})
            .elseDo(() -> {});
    }
}
````

In addition to these argument checking method, there are also methods for reading inputs during the command, both for normal text and for passwords

````java
package org.example.commands;

import sirup.cli.base.CommandClass;

public class Examples extends CommandClass {
    public void exampleCommand() {
        String line = readLine();
        String secret = readPassword();
    }
}
````