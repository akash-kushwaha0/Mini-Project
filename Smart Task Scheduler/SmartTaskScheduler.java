import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// Abstract class for Task
abstract class Task {
    protected String title;
    protected String category;
    protected int priority; // 1 = High, 2 = Medium, 3 = Low
    protected Date dueDate;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public Task(String title, String category, int priority, Date dueDate) {
        this.title = title;
        this.category = category;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public abstract void displayTask();
    
    public int getPriority() {
        return priority;
    }
    
    public Date getDueDate() {
        return dueDate;
    }
    
    public String getFormattedDate() {
        return dateFormat.format(dueDate);
    }
}

// Work Task class extending Task
class WorkTask extends Task {
    public WorkTask(String title, int priority, Date dueDate) {
        super(title, "Work", priority, dueDate);
    }

    @Override
    public void displayTask() {
        System.out.println("[Work] " + title + " | Priority: " + priority + " | Due: " + getFormattedDate());
    }
}

// Personal Task class extending Task
class PersonalTask extends Task {
    public PersonalTask(String title, int priority, Date dueDate) {
        super(title, "Personal", priority, dueDate);
    }

    @Override
    public void displayTask() {
        System.out.println("[Personal] " + title + " | Priority: " + priority + " | Due: " + getFormattedDate());
    }
}

// Task Scheduler to manage tasks
class TaskScheduler {
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void displayAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        tasks.sort(Comparator.comparing(Task::getPriority).thenComparing(Task::getDueDate));
        for (Task task : tasks) {
            task.displayTask();
        }
    }
}

// Main class to run the program
public class SmartTaskScheduler {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskScheduler scheduler = new TaskScheduler();

        while (true) {
            System.out.println("1. Add Work Task\n2. Add Personal Task\n3. Show Tasks\n4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 4) break;
            
            if (choice == 3) {
                System.out.println("\nScheduled Tasks:");
                scheduler.displayAllTasks();
                continue;
            }

            System.out.print("Enter Task Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Priority (1-High, 2-Medium, 3-Low): ");
            int priority = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Due Date (dd-MM-yyyy): ");
            String dateInput = scanner.nextLine();
            
            Date dueDate = null;
            try {
                dueDate = dateFormat.parse(dateInput);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter the date in dd-MM-yyyy format.");
                continue;
            }

            if (choice == 1) {
                scheduler.addTask(new WorkTask(title, priority, dueDate));
            } else if (choice == 2) {
                scheduler.addTask(new PersonalTask(title, priority, dueDate));
            }
        }
        
        scanner.close();
    }
}
