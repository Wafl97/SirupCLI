import org.junit.jupiter.api.Test;
import sirup.cli.base.Arguments;
import sirup.cli.base.TesterCommandClass;
import sirup.cli.inputs.Input;
import sirup.cli.inputs.SequenceReader;

import static org.junit.jupiter.api.Assertions.*;

public class ArgumentsTest {

    @Test
    public void argTest() {
        String input = "t1 -a arg";
        Arguments arguments = new Arguments();
        arguments.rebuild(input.split(" "));
        arguments.ifContains("-a", () -> {
            assertTrue(true);
        }).elseDo(() -> fail());

    }

    @Test
    public void missingArgTest() {
        String input = "t1 -a arg";
        Arguments arguments = new Arguments();
        arguments.rebuild(input.split(" "));
        arguments.ifContains("-b", () -> fail())
                .elseDo(() -> {
            assertTrue(true);
        });
    }

    @Test
    public void getArgTest() {
        String input = "t1 -a arg";
        Arguments arguments = new Arguments();
        arguments.rebuild(input.split(" "));
        arguments.ifContainsGet("-a", a -> {
           assertEquals("arg",a);
        });
    }

    @Test
    public void missingGetArgTest() {
        String input = "t1 -b arg";
        Arguments arguments = new Arguments();
        arguments.rebuild(input.split(" "));
        arguments.ifContainsGet("-a", a -> {
            assertNull(a);
        });
    }

    @Test
    public void goodStringTest() {
        String input = "t2 -b \"this is a good 'string'\" -c 'this is also a good \"string\"'";
        Arguments arguments = new Arguments();
        arguments.rebuild(input.split(" "));
        arguments.ifContainsGet("-b", b -> {
           assertEquals("this is a good 'string'",b);
        });
        arguments.ifContainsGet("-c", c -> {
           assertEquals("this is also a good \"string\"",c);
        });
    }

    @Test
    public void stringWithArgSeparatorTest() {
        String input = "t3 -a \"-b '-c'\"";
        Arguments arguments = new Arguments();
        arguments.rebuild(input.split(" "));
        arguments.ifContainsGet("-a", a -> {
           assertEquals("-b '-c'", a);
        });
        arguments.ifContains("-b", () -> {
            fail();
        });
        arguments.ifContains("-c", () -> {
            fail();
        });
    }

    @Test
    public void badStringTest() {
        String input = "t2 -x \"this is a bad string -y file.txt";
        Arguments arguments = new Arguments();
        arguments.rebuild(input.split(" "));
        arguments.ifContainsGet("-x", x -> {
           fail();
        });
        arguments.ifContainsGet("-y", y -> {
            assertEquals("file.txt",y);
        });
    }

    @Test
    public void commandClassTest1() {
        String input = "t3 -a 'Test a'";
        Arguments arguments = new Arguments();
        arguments.rebuild(input.split(" "));

        SequenceReader sequenceReader = new SequenceReader();
        Input in = new Input(sequenceReader);

        TestCommandClass tcc = new TestCommandClass(in,arguments);
        tcc.testCommand1();
    }

    @Test
    public void commandClassTest2() {
        String input = "t3 -b";
        Arguments arguments = new Arguments();
        arguments.rebuild(input.split(" "));

        SequenceReader sequenceReader = new SequenceReader();
        Input in = new Input(sequenceReader);

        TestCommandClass tcc = new TestCommandClass(in,arguments);
        tcc.testCommand2();
    }

    @Test
    public void commandClassTest3() {
        String input = "t3 -x";
        Arguments arguments = new Arguments();
        arguments.rebuild(input.split(" "));

        SequenceReader sequenceReader = new SequenceReader();
        Input in = new Input(sequenceReader);

        TestCommandClass tcc = new TestCommandClass(in,arguments);
        tcc.testCommand3();
    }

    @Test
    public void commandClassTest4() {
        String input = "t3 -y";
        Arguments arguments = new Arguments();
        arguments.rebuild(input.split(" "));

        SequenceReader sequenceReader = new SequenceReader();
        Input in = new Input(sequenceReader);

        TestCommandClass tcc = new TestCommandClass(in,arguments);
        tcc.testCommand4();
    }

    @Test
    public void commandClassTest5() {
        String input = "";
        Arguments arguments = new Arguments();
        arguments.rebuild(input.split(" "));

        SequenceReader sequenceReader = new SequenceReader();
        sequenceReader.then("line one");
        sequenceReader.then("line two");
        Input in = new Input(sequenceReader);

        TestCommandClass tcc = new TestCommandClass(in,arguments);
        tcc.testCommand5();
    }
}
