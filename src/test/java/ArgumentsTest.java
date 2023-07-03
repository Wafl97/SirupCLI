import org.junit.jupiter.api.Test;
import sirup.cli.base.Arguments;

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
}
