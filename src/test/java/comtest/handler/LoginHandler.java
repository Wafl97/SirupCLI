package comtest.handler;

import sirup.cli.base.SirupCli;
import sirup.cli.inputs.Input;

public class LoginHandler implements SirupCli.LoginHandler {
    @Override
    public boolean login(Input input) {
        return false;
    }
}
