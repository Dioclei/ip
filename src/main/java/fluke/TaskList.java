package fluke;

import java.util.ArrayList;

import fluke.exceptions.FlukeException;
import fluke.exceptions.TaskDoesNotExistException;
import fluke.tasks.Deadline;
import fluke.tasks.Event;
import fluke.tasks.Task;
import fluke.tasks.Todo;

/**
 * TaskList is a wrapper class that manages the internal list of tasks by implementing
 * various methods related to managing the list of tasks, such as adding and deleting.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs a TaskList with an empty list of tasks.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with an initial list of tasks.
     * @param initialTasks the initial list of tasks
     */
    public TaskList(ArrayList<Task> initialTasks) {
        this.tasks = initialTasks;
    }

    /**
     * Getter for the list of tasks.
     * @return the list of tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Gets the size of the list of tasks.
     * @return size of the list of tasks.
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Adds a task to the task list.
     * @param task task to be added.
     */
    private void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Adds a Todo to the list of tasks
     * @param description description of the task
     * @return the task added.
     * @throws FlukeException if there is an error while adding the task, for instance an invalid description.
     */
    public Task addTodo(String description) throws FlukeException {
        Todo newTodo = new Todo(description);
        addTask(newTodo);
        return newTodo;
    }

    /**
     * Adds a Deadline to the list of tasks.
     * @param description description of the task
     * @param byDate the date which the task is due
     * @return the task added.
     * @throws FlukeException if there is an error while adding the task, for instance an invalid description.
     */
    public Task addDeadline(String description, String byDate) throws FlukeException {
        Task newDeadline = new Deadline(description, byDate);
        addTask(newDeadline);
        return newDeadline;
    }

    /**
     * Adds an Event to the list of tasks.
     * @param description description of the task
     * @param fromDate the date which the event starts
     * @param toDate the date which the event ends
     * @return the task added.
     * @throws FlukeException if there is an error while adding the task, for instance an invalid description.
     */
    public Task addEvent(String description, String fromDate, String toDate) throws FlukeException {
        Task newEvent = new Event(description, fromDate, toDate);
        addTask(newEvent);
        return newEvent;
    }

    /**
     * Deletes a task from the task list.
     * @param index index of the task in the list
     * @return the deleted task.
     * @throws TaskDoesNotExistException if the index given is invalid.
     */
    public Task deleteTask(int index) throws TaskDoesNotExistException {
        // check if task exists
        if (tasks.size() <= index) {
            throw new TaskDoesNotExistException();
        }
        Task taskToBeDeleted = tasks.get(index);
        tasks.remove(index);
        return taskToBeDeleted;
    }

    /**
     * Marks a task in the task list as done.
     * @param index index of the task in the list
     * @return the marked task.
     * @throws TaskDoesNotExistException if the index given is invalid.
     */
    public Task markTaskAsDone(int index) throws TaskDoesNotExistException {
        // check if task exists
        if (tasks.size() <= index) {
            throw new TaskDoesNotExistException();
        }
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Marks a task in the task list as not done.
     * @param index index of the task in the list
     * @return the marked task.
     * @throws TaskDoesNotExistException if the index given is invalid.
     */
    public Task markTaskAsUndone(int index) throws TaskDoesNotExistException {
        // check if task exists
        if (tasks.size() <= index) {
            throw new TaskDoesNotExistException();
        }
        Task task = tasks.get(index);
        task.markAsUndone();
        return task;
    }

    /**
     * Finds all tasks containing a specific keyword.
     * @param keyword the keyword
     * @return a TaskList containing all tasks containing the keyword.
     */
    public TaskList findTask(String keyword) {
        ArrayList<Task> newListOfTasks = new ArrayList<>();
        for (int i = 0; i < this.getSize(); i++) {
            Task task = this.tasks.get(i);
            if (task.hasKeyword(keyword)) {
                newListOfTasks.add(task);
            }
        }
        return new TaskList(newListOfTasks);
    }

    /**
     * Returns a string representation of the list of tasks.
     * @return a string representation of the list of tasks.
     */
    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            int number = i + 1;
            str += (number + "." + task);
            if (i != tasks.size() - 1) {
                str += "\n";
            }
        }
        return str;
    }
}
