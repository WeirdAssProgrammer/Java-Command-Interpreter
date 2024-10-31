# Java Command Line Interpreter

## Overview

This project is a Unix-like shell interpreter implemented in Java, designed to run various shell commands (e.g., `ls`, `cd`, `touch`, `mv`, `grep`, etc.) from within a Java-based command-line environment. This interpreter aims to emulate basic shell functionality, allowing users to navigate directories, manage files, and execute simple commands. 

## Features

- **Basic Commands**: Supports a range of Unix commands like `ls`, `cd`, `pwd`, `mkdir`, `touch`, `rm`, `cat`, and `grep`.
- **Piping and Redirection**: Supports piping (`|`) and redirection (`>`, `>>`) for handling output within the shell.
- **Error Handling**: Basic error messages to help guide the user when commands or file paths are incorrect.
- **Extendable Structure**: New commands and features can be added to the `processCommand` method.

## How to Use

1. Clone the repository to your local machine:
    ```bash
    git clone https://github.com/your-username/java-command-line-interpreter.git
    cd java-command-line-interpreter
    ```
2. Compile the project:
    ```bash
    javac CommandLineInterpreter.java
    ```
3. Run the program:
    ```bash
    java CommandLineInterpreter
    ```
4. Use commands similar to a Unix-based shell:
    - `ls [-a | -r]`: List files in the current directory.
    - `cd <directory>`: Change the current directory.
    - `pwd`: Display the current working directory.
    - `mkdir <directory>`: Create a new directory.
    - `rmdir <directory>`: Remove an empty directory.
    - `touch <file>`: Create a new file.
    - `mv <source> <destination>`: Move or rename a file.
    - `rm <file>`: Delete a file.
    - `cat <file>`: Display file contents.
    - `grep <pattern>`: Search for a pattern within the output of the last command.
    - `exit`: Exit the shell.

5. **Piping** (`|`): Pipe the output from one command to another, e.g., `ls | grep pattern`.
6. **Redirection**:
   - **Overwrite** (`>`): Redirect command output to a file, replacing any existing content.
   - **Append** (`>>`): Append command output to a file without removing existing content.

### Example Usage

```sh
$> ls -a
$> touch newfile.txt
$> echo "Hello World" > newfile.txt
$> cat newfile.txt | grep "Hello"
$> exit
```
In this example:

1. We list all files, create a new directory (demo_folder), and navigate into it.
2. We create a file (example.txt) and redirect text into it using >.
3. We display and search its contents using cat and grep.
4. We rename the file with mv, list to verify, and delete it.
5. We exit the interpreter by using the exit command.

## Technologies Used
* Java: Core programming language used to develop the interpreter.
* Java I/O: Handles file read/write operations.
* File Management: The Java File class is used to manage files and directories.
* Scanner Class: Provides an interactive shell for handling user input.
* JUnit: Used for unit testing command functionality (test cases are included).
* Project Structure
* Command Processing: The processCommand method interprets user input and calls specific command methods.
* Command Methods: Each command has its method (e.g., ls, cd, grep), making the code modular.
* Redirection and Piping: Handled in the main loop, enabling chaining of commands and output redirection.
## Contributions
We welcome contributions to enhance functionality. To contribute:

1. Fork this repository.
2. Clone your fork and create a new branch for your feature.
3. Make changes, commit, and push to your branch.
4. Submit a pull request for review.

## Future Enhancements
* Error Handling: Enhanced error feedback for better user experience.
* Additional Commands: Potentially add more commands like cp, chmod, and command history support.

## Credits
1. Mohamed Yossery
2. Ahmed Khaled
3. Massa Goda
Thank you for using our Java-based Command Line Interpreter! Feel free to reach out if you encounter any issues or have suggestions.