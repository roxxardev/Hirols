package test;

import org.junit.Before;
import org.junit.Test;

import pl.pollub.hirols.console.CommandsContainer;
import pl.pollub.hirols.console.Hidden;
import pl.pollub.hirols.console.TestConsole;

import static org.junit.Assert.*;

/**
 * Created by erykp_000 on 2016-07-16.
 */
public class TestConsoleTest {

    private TestConsole testConsole;
    private boolean commandFound;

    @Before
    public void setUp() throws Exception {
        CommandsContainer commandsContainer = new CommandsContainer() {
            @Override
            public void exit() {
                System.out.println("EXIT\n");
            }

            @Override
            public void showCommands() {

            }

            @Override
            public void clear() {

            }

            public void testString(String str) {
                commandFound = true;

                testConsole.log(str);
            }

            public void testByte(Byte arg1, byte arg2) {
                commandFound = true;
            }

            public void testShort(Short arg1, short arg2) {
                commandFound = true;
            }

            public void testInteger(Integer arg1, int arg2) {
                commandFound = true;
            }

            public void testLong(Long arg1, long arg2) {
                commandFound = true;
            }

            public void testFloat(Float arg1, float arg2) {
                commandFound = true;
            }

            public void testDouble(Double arg1, double arg2) {
                commandFound = true;
            }

            public void testObject(Object arg1) {
                commandFound = true;
            }

            @Hidden
            public void testHidden() {
                commandFound = true;
            }
        };

        testConsole = new TestConsole(commandsContainer);
    }

    @Test
    public void test_StringArgument() {
        testConsole.executeCommand("testString STRING");

        assertTrue(commandFound);
    }

    @Test
    public void test_ByteArgument() {
        testConsole.executeCommand("testByte " + Byte.MAX_VALUE + " " + Byte.MIN_VALUE);

        assertTrue(commandFound);
    }

    @Test
    public void test_ShortArgument() {
        testConsole.executeCommand("testShort " + Short.MAX_VALUE + " " + Short.MIN_VALUE);

        assertTrue(commandFound);
    }

    @Test
    public void test_IntegerArgument() {
        testConsole.executeCommand("testInteger " + Integer.MAX_VALUE + " " + Integer.MIN_VALUE);

        assertTrue(commandFound);
    }

    @Test
    public void test_LongArgument() {
        testConsole.executeCommand("testLong " + Long.MAX_VALUE + " " + Long.MIN_VALUE);

        assertTrue(commandFound);
    }

    @Test
    public void test_FloatArgument() {
        testConsole.executeCommand("testFloat " + Float.MAX_VALUE + " " + Float.MIN_VALUE);

        assertTrue(commandFound);
    }

    @Test
    public void test_DoubleArgument() {
        testConsole.executeCommand("testDouble " + Double.MAX_VALUE + " " + Double.MIN_VALUE);

        assertTrue(commandFound);
    }

    @Test
    public void test_ObjectArgument() {
        testConsole.executeCommand("testObject test test");

        assertFalse(commandFound);
    }

    @Test
    public void testDupa() throws Exception {
        testConsole.executeCommand("testHidden");

        assertTrue(commandFound);
    }
}