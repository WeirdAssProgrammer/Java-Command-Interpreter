import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class CommandLineInterpreterTest {
    private static final Path tempDir = Paths.get(System.getProperty("user.dir"), "testDir");
    private static final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeAll
    static void setUp() throws IOException {
        // Redirect output stream to capture System.out prints
        System.setOut(new PrintStream(outputStream));
        if (!Files.exists(tempDir)) {
            Files.createDirectory(tempDir);
        }
        CommandLineInterpreter.currentDirectory = tempDir.toString();
    }

    @AfterAll
    static void tearDown() throws IOException {
        System.setOut(originalOut); // Reset System.out
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
        Files.deleteIfExists(tempDir);
    }

    @BeforeEach
    void clearOutput() {
        outputStream.reset();
    }

    @Test
    void testCreateFile() throws IOException {
        String fileName = "testFile.txt";
        CommandLineInterpreter.createFile(fileName);
        Path filePath = tempDir.resolve(fileName);
        assertTrue(Files.exists(filePath), "File should be created");
        // Clean up
        Files.deleteIfExists(filePath);
    }

    @Test
    void testListFiles() throws IOException {
        String testFileName = "testListFile.txt";
        Files.createFile(tempDir.resolve(testFileName));
        String output = CommandLineInterpreter.listFiles("");
        assertTrue(output.contains(testFileName), "Directory listing should include the file");
        Files.deleteIfExists(tempDir.resolve(testFileName));
    }

    @Test
    void testChangeDirectory() throws IOException {
        Path newDir = tempDir.resolve("subDir");
        Files.createDirectory(newDir);
        CommandLineInterpreter.changeDirectory("subDir");
        assertEquals(newDir.toString(), CommandLineInterpreter.getCurrentDirectory(), "Should change to new directory");
        CommandLineInterpreter.changeDirectory("..");
        assertEquals(tempDir.toString(), CommandLineInterpreter.getCurrentDirectory(), "Should return to original directory");
        // Clean up
        Files.deleteIfExists(newDir);
    }

    @Test
    void testDisplayFileContent() throws IOException {
        Path testFile = tempDir.resolve("contentFile.txt");
        Files.writeString(testFile, "Sample Content\nSecond Line");
        String output = CommandLineInterpreter.displayFileContent("contentFile.txt");
        assertEquals("Sample Content\nSecond Line", output.trim(), "File content should match expected text");
        Files.deleteIfExists(testFile);
    }

    @Test
    void testDeleteFile() throws IOException {
        Path testFile = tempDir.resolve("deleteFile.txt");
        Files.createFile(testFile);
        CommandLineInterpreter.deleteFile("deleteFile.txt");
        assertFalse(Files.exists(testFile), "File should be deleted");
    }

    @Test
    void testMoveFile() throws IOException {
        Path sourceFile = tempDir.resolve("source.txt");
        Path destinationFile = tempDir.resolve("destination.txt");
        Files.writeString(sourceFile, "Move this file");

        CommandLineInterpreter.moveFile("source.txt", "destination.txt");
        assertFalse(Files.exists(sourceFile), "Source file should be moved");
        assertTrue(Files.exists(destinationFile), "Destination file should exist");

        // Clean up
        Files.deleteIfExists(destinationFile);
    }

    @Test
    void testAppendToFile() throws IOException {
        Path appendFile = tempDir.resolve("append_test.txt");
        Files.writeString(appendFile, "First line\n");
        CommandLineInterpreter.appendToFile("append_test.txt", "Second line\n");

        String content = Files.readString(appendFile);
        assertTrue(content.contains("First line"), "File should contain initial content");
        assertTrue(content.contains("Second line"), "File should contain appended content");

        // Clean up
        Files.deleteIfExists(appendFile);
    }

    @Test
    void testWriteToFile() throws IOException {
        Path writeFile = tempDir.resolve("write_test.txt");
        CommandLineInterpreter.writeToFile("write_test.txt", "Hello World");

        String content = Files.readString(writeFile);
        assertEquals("Hello World", content.trim(), "File content should match the written text");

        // Clean up
        Files.deleteIfExists(writeFile);
    }

    @Test
    void testCreateDirectory() throws IOException {
        String dirName = "newDir";
        CommandLineInterpreter.createDirectory(dirName);
        assertTrue(Files.exists(tempDir.resolve(dirName)), "Directory should be created");
        // Clean up
        Files.deleteIfExists(tempDir.resolve(dirName));
    }

    @Test
    void testRemoveDirectory() throws IOException {
        String dirName = "removableDir";
        Files.createDirectory(tempDir.resolve(dirName));
        CommandLineInterpreter.removeDirectory(dirName);
        assertFalse(Files.exists(tempDir.resolve(dirName)), "Directory should be removed");
    }

    @Test
    void testDisplayHelp() {
        String helpOutput = CommandLineInterpreter.displayHelp();
        assertTrue(helpOutput.contains("Available commands:"), "Help should display available commands");
    }

    @Test
    void testPwd() {
        String output = CommandLineInterpreter.getCurrentDirectory();
        assertEquals(tempDir.toString(), output, "Current directory should match tempDir");
    }
}