import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Duke {
    private final static String CHATBOT_NAME = "Fluke";
    private final static String LOGO =
            "    ________      __\n" +
                    "   / ____/ /_  __/ /_____\n" +
                    "  / /_  / / / / / //_/ _ \\\n" +
                    " / __/ / / /_/ / ,< /  __/\n" +
                    "/_/   /_/\\__,_/_/|_|\\___/";
    private static ArrayList<Task> listOfTasks = new ArrayList<>();

    private enum Command {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT;
    }

    public static void main(String[] args) {
        // introduce Fluke
        System.out.println(LOGO);
        addHorizontalLine();
        greet();
        addHorizontalLine();
        // initialise scanner to check for user input
        Scanner scanner = new Scanner(System.in);
        boolean waitingForInput = true;
        while (waitingForInput) {
            // check for user commands
            try {
                String nextCommand = scanner.nextLine();
                Command commandType = parseCommand(nextCommand);
                switch (commandType) {
                case BYE:
                    waitingForInput = false;
                    sayBye();
                    break;
                case LIST:
                    list();
                    break;
                case MARK:
                    markTaskAsDone(nextCommand);
                    break;
                case UNMARK:
                    markTaskAsUndone(nextCommand);
                    break;
                case DELETE:
                    deleteTask(nextCommand);
                    break;
                case TODO:
                    addTodo(nextCommand);
                    break;
                case DEADLINE:
                    addDeadline(nextCommand);
                    break;
                case EVENT:
                    addEvent(nextCommand);
                    break;
                default:
                    throw new InvalidInputException();
                }
            } catch (DukeException d) {
                printError(d.getMessage());
            }
            addHorizontalLine();
        }
    }

    private static Command parseCommand(String nextCommand) throws InvalidInputException {
        if (nextCommand.equals("bye")) {
            return Command.BYE;
        } else if (nextCommand.equals("list")) {
            return Command.LIST;
        } else if (nextCommand.startsWith("mark")) {
            return Command.MARK;
        } else if (nextCommand.startsWith("unmark")) {
            return Command.UNMARK;
        } else if (nextCommand.startsWith("delete")) {
            return Command.DELETE;
        } else if (nextCommand.startsWith("todo")) {
            return Command.TODO;
        } else if (nextCommand.startsWith("deadline")) {
            return Command.DEADLINE;
        } else if (nextCommand.startsWith("event")) {
            return Command.EVENT;
        } else {
            throw new InvalidInputException();
        }
    }

    private static void greet() {
        System.out.println(
                "Hello! I'm " + CHATBOT_NAME + ", everything I do is down to luck!" + "\n" +
                        "Feeling lucky today?"
        );
    }

    private static void sayBye() {
        System.out.println("Bye. Good luck!");
    }

    private static void addHorizontalLine() {
        System.out.println("____________________________________________________________");
    }

    private static void addTask(Task task) {
        System.out.println("(Scribbles randomly). Hope I got it right!");
        System.out.println("  " + task);
        listOfTasks.add(task);
        System.out.println("I think there are now " + listOfTasks.size() + " tasks in the list.");
    }

    private static void addTodo(String command) {
        try {
            if (command.length() <= 5) {
                // command is too short, description is invalid
                throw new EmptyDescriptionException();
            }
            String str = command.substring(5);
            Todo newTodo = new Todo(str);
            addTask(newTodo);
        } catch (DukeException d) {
            printError(d.getMessage());
        }
    }

    private static void addDeadline(String command) {
        try {
            if (command.length() <= 9) {
                // command is too short, description is invalid
                throw new EmptyDescriptionException();
            }
            String str = command.substring(9);
            int byIndex = str.indexOf("/by");
            String description = str.substring(0, byIndex - 1);
            String by = str.substring(byIndex + 4);
            Task task = new Deadline(description, by);
            addTask(task);
        } catch (DukeException d) {
            printError(d.getMessage());
        }
    }

    private static void addEvent(String command) {
        try {
            if (command.length() <= 6) {
                // command is too short, description is invalid
                throw new EmptyDescriptionException();
            }
            String str = command.substring(6);
            int fromIndex = str.indexOf("/from");
            int toIndex = str.indexOf("/to");
            String description = str.substring(0, fromIndex - 1);
            String from = str.substring(fromIndex + 6, toIndex - 1);
            String to = str.substring(toIndex + 4);
            Task task = new Event(description, from, to);
            addTask(task);
        } catch (DukeException d) {
            printError(d.getMessage());
        }
    }

    private static void list() {
        System.out.println("Here are the tasks we have currently!");
        for (int i = 0; i < listOfTasks.size(); i++) {
            Task task = listOfTasks.get(i);
            int number = i + 1;
            System.out.println(number + "." + task);
        }
    }

    private static void markTaskAsDone(String nextCommand) {
        try {
            if (nextCommand.length() <= 5) {
                throw new InvalidInputException();
            }
            int taskNumber = obtainTaskNumber(nextCommand.substring(5));
            int index = taskNumber - 1;
            // check if task exists
            if (index < listOfTasks.size()) {
                listOfTasks.get(index).markAsDone();
                System.out.println("I have marked this task as done, I hope it's the right one:");
                System.out.println("  " + listOfTasks.get(index));
            } else {
                throw new TaskDoesNotExistException();
            }
        } catch (DukeException d) {
            printError(d.getMessage());
        }
    }

    private static void markTaskAsUndone(String nextCommand) {
        try {
            if (nextCommand.length() <= 7) {
                throw new InvalidInputException();
            }
            int taskNumber = obtainTaskNumber(nextCommand.substring(7));
            int index = taskNumber - 1;
            // check if task exists
            if (index < listOfTasks.size()) {
                listOfTasks.get(index).markAsUndone();
                System.out.println("  " + "I have marked this task as not done yet, I hope it's the right one:");
                System.out.println(listOfTasks.get(index));
            } else {
                throw new TaskDoesNotExistException();
            }
        } catch (DukeException d) {
            printError(d.getMessage());
        }
    }

    private static void deleteTask(String nextCommand) {
        try {
            if (nextCommand.length() <= 7) {
                throw new InvalidInputException();
            }
            int taskNumber = obtainTaskNumber(nextCommand.substring(7));
            int index = taskNumber - 1;
            // check if task exists
            if (index < listOfTasks.size()) {
                String taskString = listOfTasks.get(index).toString();
                listOfTasks.remove(index);
                System.out.println("Task deleted! I hope it's the right one:");
                System.out.println("  " + taskString);
                System.out.println("I think there are now " + listOfTasks.size() + " tasks in the list.");
            } else {
                throw new TaskDoesNotExistException();
            }
        } catch (DukeException d) {
            printError(d.getMessage());
        }
    }

    private static int obtainTaskNumber(String taskNumberString) throws InvalidInputException {
        try {
            return Integer.parseInt(taskNumberString);
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }
    }

    private static void printError(String message) {
        System.out.println("☹ OOPS!!! " + message);
    }
}
