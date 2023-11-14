import org.junit.jupiter.api.Assertions;
import sirup.cli.base.Arguments;
import sirup.cli.base.TesterCommandClass;
import sirup.cli.inputs.Input;

import static org.junit.jupiter.api.Assertions.*;


public class TestCommandClass extends TesterCommandClass {

    public TestCommandClass(Input input, Arguments arguments) {
        super(input,arguments);
    }

    public void testCommand1() {
        with("a", a -> assertEquals("Test a", a)).elseDo(Assertions::fail);
    }

    public void testCommand2() {
        when("b", () -> assertTrue(true)).elseDo(Assertions::fail);
    }

    public void testCommand3() {
        with("y", c -> fail())
        .elseDo(() -> assertTrue(true));
    }

    public void testCommand4() {
        when("x", Assertions::fail)
        .elseDo(() -> assertTrue(true));
    }

    public void testCommand5() {
        String line1 = readLine();
        assertEquals("line one", line1);
        String line2 = readLine();
        assertEquals("line two", line2);
    }
}
