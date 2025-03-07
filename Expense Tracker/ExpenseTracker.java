import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class ExpenseTracker {
    private static final String FILE_NAME = "expenses.txt";
    private static Map<String, Double> expenses = new HashMap<>();
    private static double budgetLimit = 0;
    private static double totalIncome = 0;

    public static void main(String[] args) {
        loadExpenses();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n1. Add Expense\n2. View Expenses\n3. Set Budget Limit\n4. Set Income\n5. View Savings\n6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    addExpense(scanner);
                    break;
                case 2:
                    viewExpenses();
                    break;
                case 3:
                    setBudget(scanner);
                    break;
                case 4:
                    setIncome(scanner);
                    break;
                case 5:
                    viewSavings();
                    break;
                case 6:
                    saveExpenses();
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private static void addExpense(Scanner scanner) {
        System.out.print("Enter category (Food, Transport, Shopping, etc.): ");
        String category = scanner.nextLine();
        System.out.print("Enter amount (in Rupees): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        
        expenses.put(category, expenses.getOrDefault(category, 0.0) + amount);
        System.out.println("Expense added successfully!");
        checkBudget();
    }

    private static void viewExpenses() {
        System.out.println("\nExpense Summary:");
        double total = 0;
        for (Map.Entry<String, Double> entry : expenses.entrySet()) {
            System.out.println(entry.getKey() + ": ₹" + entry.getValue());
            total += entry.getValue();
        }
        System.out.println("Total Expenses: ₹" + total);
    }

    private static void setBudget(Scanner scanner) {
        System.out.print("Enter your budget limit (in Rupees): ");
        budgetLimit = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Budget limit set to: ₹" + budgetLimit);
        checkBudget();
    }

    private static void setIncome(Scanner scanner) {
        System.out.print("Enter your total income (in Rupees): ");
        totalIncome = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Income recorded: ₹" + totalIncome);
    }

    private static void viewSavings() {
        double totalExpenses = expenses.values().stream().mapToDouble(Double::doubleValue).sum();
        double savings = totalIncome - totalExpenses;
        System.out.println("Total Income: ₹" + totalIncome);
        System.out.println("Total Expenses: ₹" + totalExpenses);
        System.out.println("Total Savings: ₹" + savings);
    }

    private static void checkBudget() {
        double total = expenses.values().stream().mapToDouble(Double::doubleValue).sum();
        if (budgetLimit > 0 && total > budgetLimit) {
            System.out.println("WARNING! You have exceeded your budget limit of ₹" + budgetLimit);
        }
    }

    private static void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, Double> entry : expenses.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }

    private static void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    expenses.put(parts[0], Double.parseDouble(parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("No previous expenses found. Starting fresh.");
        }
    }
}
