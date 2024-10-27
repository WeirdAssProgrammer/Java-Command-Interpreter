import java.util.Scanner;

public class CommandLineInterpreter {
    public static void main(String[] args) {
        System.out.println("Welcome to the java Unix-based shell...");

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
                System.out.println("Piping output of: " + command1 + " to " + command2);
                // Here you would execute command1, capture its output, and pass it as input to command2
                continue;
            } else if (userInput.contains(">>")) {
                String[] redirectionParts = userInput.split(">>");
                String command = redirectionParts[0].trim();
                String targetFile = redirectionParts[1].trim();
                System.out.println("Appending output of: " + command + " to file: " + targetFile);
                // Execute the command and append output to the target file
                continue;
            } else if (userInput.contains(">")) {
                String[] redirectionParts = userInput.split(">");
                String command = redirectionParts[0].trim();
                String targetFile = redirectionParts[1].trim();
                System.out.println("Writing output of: " + command + " to file: " + targetFile);
                // Execute the command and write output to the target file
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
                    if (parameters.equals("-a")) {
                        System.out.println("Listing all files, including hidden files.");
                        //TODO:Add code for ls -a
                    } else if (parameters.equals("-r")) {
                        System.out.println("Listing files in reverse order.");
                        //TODO:Add code for ls -r
                    } else {
                        System.out.println("Listing files in current directory.");
                        //TODO:Add code for ls
                    }
                    break;

                case "cd":
                    if (!parameters.isEmpty()) {
                        System.out.println("Changing directory to: " + parameters);
                        //TODO:Add code for cd
                    } else {
                        System.out.println("$[error]> Please provide a directory.");
                    }
                    break;

                case "pwd":
                    System.out.println("Printing working directory.");
                    //TODO:Add code for pwd
                    break;

                case "mkdir":
                    if (!parameters.isEmpty()) {
                        System.out.println("Creating directory: " + parameters);
                        //TODO:Add code for mkdir
                    } else {
                        System.out.println("$[error]> Please provide a directory name.");
                    }
                    break;

                case "rmdir":
                    if (!parameters.isEmpty()) {
                        System.out.println("Removing directory: " + parameters);
                        //TODO:Add code for rmdir
                    } else {
                        System.out.println("$[error]> Please provide a directory name.");
                    }
                    break;

                case "touch":
                    if (!parameters.isEmpty()) {
                        System.out.println("Creating file: " + parameters);
                        //TODO:Add code for touch
                    } else {
                        System.out.println("$[error]> Please provide a file name.");
                    }
                    break;

                case "mv":
                    String[] mvParams = parameters.split("\\s+");
                    if (mvParams.length == 2) {
                        System.out.println("Moving/renaming " + mvParams[0] + " to " + mvParams[1]);
                        //TODO:Add code for mv and note for renaming case
                    } else {
                        System.out.println("$[error]> Please provide source and destination.");
                    }
                    break;

                case "rm":
                    if (!parameters.isEmpty()) {
                        System.out.println("Removing file: " + parameters);
                        //TODO:Add code for rm
                    } else {
                        System.out.println("$[error]> Please provide a file name.");
                    }
                    break;

                case "cat":
                    if (!parameters.isEmpty()) {
                        System.out.println("Displaying contents of file: " + parameters);
                        //TODO:Add code for cat
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
