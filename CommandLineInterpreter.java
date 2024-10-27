import java.util.Scanner;

public class CommandLineInterpreter {
    public static void main(String[] args) {
        System.out.println("Welcome to the java Unix-based shell...");

        boolean isExit = false;
        Scanner scanner = new Scanner(System.in);

        while (!isExit) {
            System.out.print("$> ");
            String userInput = scanner.nextLine().trim();
            
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
                    if (parameters.equals("-a")) {
                        // Handle ls -a logic here
                        System.out.println("Listing all files, including hidden files.");
                    } else if (parameters.equals("-r")) {
                        // Handle ls -r logic here
                        System.out.println("Listing files in reverse order.");
                    } else {
                        // Handle regular ls command here
                        System.out.println("Listing files in current directory.");
                    }
                    break;

                case "cd":
                    if (!parameters.isEmpty()) {
                        // Handle cd <directory> logic here
                        System.out.println("Changing directory to: " + parameters);
                    } else {
                        System.out.println("$[error]> Please provide a directory.");
                    }
                    break;

                case "pwd":
                    // Handle pwd logic here
                    System.out.println("Printing working directory.");
                    break;

                case "mkdir":
                    if (!parameters.isEmpty()) {
                        // Handle mkdir <directory> logic here
                        System.out.println("Creating directory: " + parameters);
                    } else {
                        System.out.println("$[error]> Please provide a directory name.");
                    }
                    break;

                case "rmdir":
                    if (!parameters.isEmpty()) {
                        // Handle rmdir <directory> logic here
                        System.out.println("Removing directory: " + parameters);
                    } else {
                        System.out.println("$[error]> Please provide a directory name.");
                    }
                    break;

                case "touch":
                    if (!parameters.isEmpty()) {
                        // Handle touch <file> logic here
                        System.out.println("Creating file: " + parameters);
                    } else {
                        System.out.println("$[error]> Please provide a file name.");
                    }
                    break;

                case "mv":
                    String[] mvParams = parameters.split("\\s+");
                    if (mvParams.length == 2) {
                        // Handle mv <source> <destination> logic here
                        System.out.println("Moving/renaming " + mvParams[0] + " to " + mvParams[1]);
                    } else {
                        System.out.println("$[error]> Please provide source and destination.");
                    }
                    break;

                case "rm":
                    if (!parameters.isEmpty()) {
                        // Handle rm <file> logic here
                        System.out.println("Removing file: " + parameters);
                    } else {
                        System.out.println("$[error]> Please provide a file name.");
                    }
                    break;

                case "cat":
                    if (!parameters.isEmpty()) {
                        // Handle cat <file> logic here
                        System.out.println("Displaying contents of file: " + parameters);
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
}
