import java.io.*;
import java.util.Scanner;

public class CommandLineInterpreter {
    private static String currentDirectory = System.getProperty("user.dir");

    public static void main(String[] args) {
        System.out.println("Welcome to the Java Unix-based shell...");

        boolean isExit = false;
        Scanner scanner = new Scanner(System.in);

        while (!isExit) {
            System.out.print("$> ");
            String userInput = scanner.nextLine().trim();

            // Check for pipe operator
            if (userInput.contains("|")) {
                String[] commands = userInput.split("\\|");
                String previousOutput = "";
                for (String command : commands) {
                    command = command.trim();
                    previousOutput = processCommand(command, previousOutput);
                }
                continue;
            } else if (userInput.contains(">>")) {
                String[] redirectionParts = userInput.split(">>");
                String command = redirectionParts[0].trim();
                String targetFile = redirectionParts[1].trim();
                String output = processCommand(command, "");
                appendToFile(targetFile, output);
                continue;
            } else if (userInput.contains(">")) {
                String[] redirectionParts = userInput.split(">");
                String command = redirectionParts[0].trim();
                String targetFile = redirectionParts[1].trim();
                String output = processCommand(command, "");
                writeToFile(targetFile, output);
                continue;
            }

            String output = processCommand(userInput, "");
            if (!output.isEmpty()) {
                System.out.println(output);
            }
        }

        scanner.close();
    }

    private static String processCommand(String userInput, String previousOutput) {
        // Check for commands that need to use the previous output as input
        String[] inputParts = userInput.split("\\s+", 2);
        String command = inputParts[0];
        String parameters = inputParts.length > 1 ? inputParts[1] : "";

        // If there's a previous output, treat it as input for this command
        if (!previousOutput.isEmpty()) {
            parameters = previousOutput;
        }

        switch (command) {
            case "help":
                return displayHelp();
            case "ls":
                return listFiles();
            case "cd":
                changeDirectory(parameters);
                return "";
            case "pwd":
                return "Current working directory: " + currentDirectory;
            case "mkdir":
                createDirectory(parameters);
                return "";
            case "rmdir":
                removeDirectory(parameters);
                return "";
            case "touch":
                createFile(parameters);
                return "";
            case "mv":
                String[] mvParams = parameters.split("\\s+");
                if (mvParams.length == 2) {
                    moveFile(mvParams[0], mvParams[1]);
                } else {
                    return "$[error]> Please provide source and destination.";
                }
                return "";
            case "rm":
                deleteFile(parameters);
                return "";
            case "cat":
                return displayFileContent(parameters);
            case "grep":
                return grepOutput(previousOutput, parameters);
            case "exit":
                System.out.println("$[See you next time ;D]> Bye!");
                System.exit(0);
                return "";
            default:
                return "$[error]> Unknown command: " + command;
        }
    }

    private static String displayHelp() {
        return "Available commands: \n"
                + " ls [-a | -r]: list current directory child items \n"
                + " cd <directory>: change directory \n"
                + " pwd : print working directory \n"
                + " mkdir <directory>: make directory \n"
                + " rmdir <directory>: remove empty directory \n"
                + " touch <file>: create new file \n"
                + " mv <source> <destination>: cut/rename a file \n"
                + " rm <file>: remove a file \n"
                + " cat <file>: output file's content \n"
                + " grep <pattern>: search for a pattern in the input \n"
                + " ============================= \n"
                + " Optional directives: \n"
                + " > [overwrite] \n"
                + " >> [append new line] \n"
                + " | [pipe output]";
    }

    private static String listFiles() {
        File directory = new File(currentDirectory);
        StringBuilder output = new StringBuilder();
        for (String fileName : directory.list()) {
            output.append(fileName).append("\n");
        }
        return output.toString().trim();
    }

    private static void changeDirectory(String path) {
        File newDir = new File(currentDirectory, path);
        if (newDir.exists() && newDir.isDirectory()) {
            currentDirectory = newDir.getAbsolutePath();
            System.out.println("Changed directory to: " + currentDirectory);
        } else {
            System.out.println("$[error]> Directory does not exist: " + path);
        }
    }

    private static void createDirectory(String dirName) {
        File newDir = new File(currentDirectory, dirName);
        if (newDir.mkdir()) {
            System.out.println("Directory created: " + dirName);
        } else {
            System.out.println("$[error]> Failed to create directory: " + dirName);
        }
    }

    private static void removeDirectory(String dirName) {
        File dir = new File(currentDirectory, dirName);
        if (dir.exists() && dir.isDirectory()) {
            if (dir.delete()) {
                System.out.println("Directory removed: " + dirName);
            } else {
                System.out.println("$[error]> Failed to remove directory: " + dirName);
            }
        } else {
            System.out.println("$[error]> Directory does not exist: " + dirName);
        }
    }

    private static void createFile(String fileName) {
        File file = new File(currentDirectory, fileName);
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + fileName);
            } else {
                System.out.println("$[error]> File already exists: " + fileName);
            }
        } catch (IOException e) {
            System.out.println("$[error]> " + e.getMessage());
        }
    }

    private static void moveFile(String source, String destination) {
        File srcFile = new File(currentDirectory, source);
        File destFile = new File(currentDirectory, destination);
        if (srcFile.exists()) {
            if (srcFile.renameTo(destFile)) {
                System.out.println("File moved from " + source + " to " + destination);
            } else {
                System.out.println("$[error]> Failed to move file: " + source);
            }
        } else {
            System.out.println("$[error]> Source file does not exist: " + source);
        }
    }

    private static void deleteFile(String fileName) {
        File file = new File(currentDirectory, fileName);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File removed: " + fileName);
            } else {
                System.out.println("$[error]> Failed to remove file: " + fileName);
            }
        } else {
            System.out.println("$[error]> File does not exist: " + fileName);
        }
    }

    private static String displayFileContent(String fileName) {
        File file = new File(currentDirectory, fileName);
        StringBuilder content = new StringBuilder();
        if (file.exists() && file.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } catch (IOException e) {
                return "$[error]> " + e.getMessage();
            }
        } else {
            return "$[error]> File does not exist: " + fileName;
        }
        return content.toString().trim();
    }

    private static String grepOutput(String input, String pattern) {
        StringBuilder output = new StringBuilder();
        String[] lines = input.split("\n");
        for (String line : lines) {
            if (line.contains(pattern)) {
                output.append(line).append("\n");
            }
        }
        return output.toString().trim();
    }

    private static void writeToFile(String targetFile, String content) {
        try (FileWriter fw = new FileWriter(new File(currentDirectory, targetFile))) {
            fw.write(content);
        } catch (IOException e) {
            System.out.println("$[error]> " + e.getMessage());
        }
    }

    private static void appendToFile(String targetFile, String content) {
        try (FileWriter fw = new FileWriter(new File(currentDirectory, targetFile), true)) {
            fw.write(content);
        } catch (IOException e) {
            System.out.println("$[error]> " + e.getMessage());
        }
    }
}
