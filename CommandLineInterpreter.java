import java.util.Scanner;
public class CommandLineInterpreter {

    public static void main(String[] args) {
        //scanner object to read input
        boolean isExit = false;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n$> ");
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
                    break;
            
                default:
                System.out.println("$[error]> Please provide a command");
                    break;
            }
        }
        
    }
}

