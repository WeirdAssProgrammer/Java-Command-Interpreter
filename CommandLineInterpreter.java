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
            
                default:
                    break;
            }
        }
    }
}

