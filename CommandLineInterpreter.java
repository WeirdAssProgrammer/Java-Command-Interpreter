import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class CommandLineInterpreter {
    public static String currentDirectory = System.getProperty("user.dir");

    public static void main(String[] args) {
        System.out.println("Welcome to the Java Unix-based shell...");

        boolean isExit = false;
        Scanner scanner = new Scanner(System.in);

        while (!isExit) {
            System.out.print("$> ");
            String userInput = scanner.nextLine().trim();

            if (userInput.contains("|")) {
                String[] commands = userInput.split("\\|");
                String previousOutput = "";
                for (String command : commands) {
                    previousOutput = processCommand(command.trim(), previousOutput);
                }
                if (!previousOutput.isEmpty()) {
                    System.out.println(previousOutput);
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

    public static String processCommand(String userInput, String previousOutput) {
        String[] inputParts = userInput.split("\\s+", 2);
        String command = inputParts[0];
        String parameters = inputParts.length > 1 ? inputParts[1] : "";

        if (!previousOutput.isEmpty()) {
            parameters = previousOutput;
        }

        switch (command) {
            case "help":
                return displayHelp();
            case "ls":
                return listFiles(parameters);
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
            case "exit":
                System.out.println("$[See you next time ;D]> Bye!");
                System.exit(0);
                return "";
            default:
                return "$[error]> Unknown command: " + command;
        }
    }

    public static String displayHelp() {
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
    public static String getCurrentDirectory() {
        return currentDirectory;
    }

    public static String listFiles(String parameters) {
        File directory = new File(currentDirectory);
        String[] files = directory.list();
        if (files == null) {
            return "$[error]> Unable to access directory.";
        }

        boolean showAll = parameters.contains("-a");
        Arrays.sort(files);

        StringBuilder output = new StringBuilder();
        for (String fileName : files) {
            File file = new File(directory, fileName);
            // Include file only if it's not hidden, or if '-a' is specified
            if ((showAll || (!file.isHidden()) && !fileName.startsWith("."))) {
                output.append(fileName).append("\n");
            }
        }
        return output.toString().trim();
    }


    public static void changeDirectory(String path) {
        // If the user enters cd .., navigate to the parent directory
        if (path.equals("..")) {
            File currentDir = new File(currentDirectory);
            String parentDir = currentDir.getParent();
            if (parentDir != null) {
                currentDirectory = parentDir;
                System.out.println("Changed directory to: " + currentDirectory);
            } else {
                System.out.println("$[error]> No parent directory exists.");
            }
        } else {
            // For other paths, navigate to the specified directory
            File newDir = new File(currentDirectory, path);
            if (newDir.exists() && newDir.isDirectory()) {
                currentDirectory = newDir.getAbsolutePath();
                System.out.println("Changed directory to: " + currentDirectory);
            } else {
                System.out.println("$[error]> Directory does not exist: " + path);
            }
        }
    }


    public static void createDirectory(String dirName) {
        File newDir = new File(currentDirectory, dirName);
        if (newDir.mkdir()) {
            System.out.println("Directory created: " + dirName);
        } else {
            System.out.println("$[error]> Failed to create directory: " + dirName);
        }
    }

    public static void removeDirectory(String dirName) {
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

    public static void createFile(String fileName) {
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

    public static void moveFile(String source, String destination) {
        File srcFile = new File(currentDirectory, source);
        File destFile = new File(currentDirectory, destination);

        if (!srcFile.exists()) {
            System.out.println("$[error]> Source file does not exist: " + source);
            return;
        }

        // If the destination is a directory, move the file into the directory
        if (destFile.isDirectory()) {
            destFile = new File(destFile, srcFile.getName()); // Move into the directory with the original name
        }

        // Attempt to rename/move the file
        if (srcFile.renameTo(destFile)) {
            System.out.println("File moved");
        } else {
            System.out.println("$[error]> Failed to move file: " + source);
        }
    }


    public static void deleteFile(String fileName) {
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

    public static String displayFileContent(String fileName) {
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

    public static void writeToFile(String targetFile, String content) {
        try (FileWriter fw = new FileWriter(new File(currentDirectory, targetFile))) {
            fw.write(content);
        } catch (IOException e) {
            System.out.println("$[error]> " + e.getMessage());
        }
    }

    public static void appendToFile(String targetFile, String content) {
        try (FileWriter fw = new FileWriter(new File(currentDirectory, targetFile), true)) {
            fw.write(content);
        } catch (IOException e) {
            System.out.println("$[error]> " + e.getMessage());
        }
    }
}