package tests;
import org.junit.Test;

import CommandLineInterpreter;

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class CommandLineInterpreterTest {

    @Test
    public void testUserInput() {
        // Set up the input stream to simulate user input
        String simulatedInput = "Hello, World!\n";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        // Set up the output stream to capture output
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the main method of CommandLineInterpreter
        CommandLineInterpreter.main(new String[0]);

        // Assert that the output is as expected
        assertEquals("You entered: Hello, World!\n", outContent.toString());

        // Reset the System.in and System.out to their original values
        System.setIn(System.in);
        System.setOut(originalOut);
    }
}
