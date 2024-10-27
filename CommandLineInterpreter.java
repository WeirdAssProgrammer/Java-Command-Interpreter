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

            // Check for pipe or redirection operators
            if (userInput.contains("|")) {
                String[] pipeParts = userInput.split("\\|");
                String command1 = pipeParts[0].trim();
                String command2 = pipeParts[1].trim();

                // Execute the first command and get its output
                String output1 = execCommand(command1.split(" "));
                // Pass the output of the first command to the second command
                String output2 = execCommand(command2.split(" "), output1);
                System.out.println(output2);
                continue;
            } else if (userInput.contains(">>")) {
                String[] redirectionParts = userInput.split(">>");
                String command = redirectionParts[0].trim();
                String targetFile = redirectionParts[1].trim();
                String output = execCommand(command.split(" "));
                appendToFile(targetFile, output);
                continue;
            } else if (userInput.contains(">")) {
                String[] redirectionParts = userInput.split(">");
                String command = redirectionParts[0].trim();
                String targetFile = redirectionParts[1].trim();
                String output = execCommand(command.split(" "));
                writeToFile(targetFile, output);
                continue;
            }

            // Split the input into command and parameters
            String[] inputParts = userInput.split("\\s+", 2);
            String command = inputParts[0];
            String parameters = inputParts.length > 1 ? inputParts[1] : "";

            switch (command) {
                case "help":
                    System.out.println("Available commands: \n"
                            + " ls [-a | -r]: list current directory child items \n"
                            + " cd <directory>: change directory \n"
                            + " pwd : print working directory \n"
                            + " mkdir <directory>: make directory \n"
                            + " rmdir <directory>: remove empty directory \n"
                            + " touch <file>: create new file \n"
                            + " mv <source> <destination>: cut/rename a file \n"
                            + " rm <file>: remove a file \n"
                            + " cat <file>: output file's content \n"
                            + " ============================= \n"
                            + " Optional directives: \n"
                            + " > [overwrite] \n"
                            + " >> [append new line] \n"
                            + " | [pipe output]");
                    break;

                case "ls":
                    System.out.println("Listing files in current directory.");
                    String output = execCommand(new String[]{"cmd", "/c", "dir"});
                    System.out.println(output);
                    break;

                case "cd":
                    if (!parameters.isEmpty()) {
                        changeDirectory(parameters);
                    } else {
                        System.out.println("$[error]> Please provide a directory.");
                    }
                    break;

                case "pwd":
                    System.out.println("Current working directory: " + currentDirectory);
                    break;

                case "mkdir":
                    if (!parameters.isEmpty()) {
                        createDirectory(parameters);
                    } else {
                        System.out.println("$[error]> Please provide a directory name.");
                    }
                    break;

                case "rmdir":
                    if (!parameters.isEmpty()) {
                        removeDirectory(parameters);
                    } else {
                        System.out.println("$[error]> Please provide a directory name.");
                    }
                    break;

                case "touch":
                    if (!parameters.isEmpty()) {
                        createFile(parameters);
                    } else {
                        System.out.println("$[error]> Please provide a file name.");
                    }
                    break;

                case "mv":
                    String[] mvParams = parameters.split("\\s+");
                    if (mvParams.length == 2) {
                        moveFile(mvParams[0], mvParams[1]);
                    } else {
                        System.out.println("$[error]> Please provide source and destination.");
                    }
                    break;

                case "rm":
                    if (!parameters.isEmpty()) {
                        deleteFile(parameters);
                    } else {
                        System.out.println("$[error]> Please provide a file name.");
                    }
                    break;

                case "cat":
                    if (!parameters.isEmpty()) {
                        displayFileContent(parameters);
                    } else {
                        System.out.println("$[error]> Please provide a file name.");
                    }
                    break;

                case "exit":
                    System.out.println("$[See you next time ;D]> Bye!");
                    isExit = true;
                    break;

                default:
                    System.out.println("$[error]> Unknown command: " + command);
                    break;
            }
        }

        scanner.close();
    }

    private static String execCommand(String[] commandParts) {
        StringBuilder output = new StringBuilder();

        try {
            // ProcessBuilder for running commands
            ProcessBuilder processBuilder = new ProcessBuilder(commandParts);
            processBuilder.directory(new File(currentDirectory));

            Process process = processBuilder.start();

            // Read the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Wait for the process to finish
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            return "$[error]> " + e.getMessage();
        }

        return output.toString().trim();
    }

    private static String execCommand(String[] commandParts, String input) {
        StringBuilder output = new StringBuilder();

        try {
            // Create a process to execute the command
            ProcessBuilder processBuilder = new ProcessBuilder(commandParts);
            processBuilder.directory(new File(currentDirectory));

            // Start the process
            Process process = processBuilder.start();

            // Write the input to the process's output stream
            try (OutputStream os = process.getOutputStream()) {
                os.write(input.getBytes());
                os.flush();
            }

            // Read the output from the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Wait for the process to finish
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            return "$[error]> " + e.getMessage();
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

    private static void displayFileContent(String fileName) {
        File file = new File(currentDirectory, fileName);
        if (file.exists() && file.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("$[error]> " + e.getMessage());
            }
        } else {
            System.out.println("$[error]> File does not exist: " + fileName);
        }
    }
}
