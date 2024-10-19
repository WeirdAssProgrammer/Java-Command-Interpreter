import java.util.Scanner;
public class CommandLineInterpreter {
    //comment to test the goddamn connection
    public static void main(String[] args) {
        System.out.println("Welcome to the java Unix-based shell...");
        
        boolean isExit = false;
        while (true) {
            if (isExit) {
                break;
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("$> ");
            String userInput = scanner.nextLine();
            switch (userInput) {
                case "help":
                    System.out.println("Avaliable commands: \n ls [-a | -r]: list current directory child items \n cd : change directory \n pwd : print working directory \n mkdir : make directory \n rmdir : remove empty directory \n touch : create new file \n mv : cut/rename a file \n rm : remove a file \n cat : output file's content \n ============================= \n Optional directors: \n > [rewrite] \n >> [add new line] \n | [pipe output]");
                    break;
            
                case "ls":
                    
                    break;
            
                case "cd":
                    
                    break;
                case "pwd":
                    
                    break;
                case "ls -a":
                    
                    break;
                case "ls -r":
                    
                    break;
                case "mkdir":
                    
                    break;
                case "rmdir":
                    
                    break;
                case "cat":
                    
                    break;
                case "rm":
                    
                    break;
                case "touch":
                    
                    break;
                case "exit":
                    System.out.println("$[See you next time ;D]> Bye!");
                    isExit = true;
                    scanner.close();
                    break;
            
                default:
                System.out.println("$[error]> Please provide a command");
                    break;
            }
        }
        
    }
}

