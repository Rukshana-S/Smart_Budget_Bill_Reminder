import java.time.LocalDate;
import java.util.*;

// === Model Classes ===
class User {
    private int userId;
    private String username;
    private String password;

    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
    public String getUsername() { return username; }
}

class Budget {
    private int budgetId;
    private String category;
    private double amount;

    public Budget(int budgetId, String category, double amount) {
        this.budgetId = budgetId;
        this.category = category;
        this.amount = amount;
    }
    public String toString() {
        return "Budget{" + category + ": " + amount + "}";
    }
}

// Base (abstract) class → Abstraction
abstract class Bill {
    protected int billId;
    protected String name;
    protected double amount;
    protected LocalDate dueDate;

    public Bill(int billId, String name, double amount, LocalDate dueDate) {
        this.billId = billId;
        this.name = name;
        this.amount = amount;
        this.dueDate = dueDate;
    }

    // Polymorphism: different reminder messages
    public abstract String reminderText();

    public String toString() {
        return name + " - " + amount + " due on " + dueDate;
    }
}

// Inheritance
class OneTimeBill extends Bill {
    public OneTimeBill(int id, String name, double amt, LocalDate due) {
        super(id, name, amt, due);
    }
    public String reminderText() {
        return "One-time Bill: " + name + " due on " + dueDate;
    }
}

class RecurringBill extends Bill {
    public RecurringBill(int id, String name, double amt, LocalDate due) {
        super(id, name, amt, due);
    }
    public String reminderText() {
        return "Recurring Bill: " + name + " due on " + dueDate + " (will repeat)";
    }
}

// Composition: Manager holds Budgets & Bills
class BudgetManager {
    private List<Budget> budgets = new ArrayList<>();
    private List<Bill> bills = new ArrayList<>();

    public void addBudget(Budget b) { budgets.add(b); }
    public void addBill(Bill b) { bills.add(b); }

    public void showBudgets() {
        System.out.println("=== Budgets ===");
        for (Budget b : budgets) System.out.println(b);
    }
    public void showBills() {
        System.out.println("=== Bills ===");
        for (Bill b : bills) System.out.println(b + " → " + b.reminderText());
    }
}

// === Main Program (Phase I Demo) ===
public class SmartBudget {
    public static void main(String[] args) {
        User u = new User(1, "demoUser", "1234");
        BudgetManager manager = new BudgetManager();

        // seed data
        manager.addBudget(new Budget(1, "Food", 5000));
        manager.addBudget(new Budget(2, "Rent", 10000));
        manager.addBill(new OneTimeBill(1, "Electricity", 1500, LocalDate.of(2025, 9, 5)));
        manager.addBill(new RecurringBill(2, "Netflix", 500, LocalDate.of(2025, 9, 1)));

        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome " + u.getUsername());

        boolean running = true;
        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1) Show Budgets");
            System.out.println("2) Show Bills");
            System.out.println("3) Add Budget");
            System.out.println("4) Add Bill");
            System.out.println("5) Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    manager.showBudgets();
                    break;
                case "2":
                    manager.showBills();
                    break;
                case "3":
                    promptAddBudget(sc, manager);
                    break;
                case "4":
                    promptAddBill(sc, manager);
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        sc.close();
        System.out.println("Goodbye.");
    }

    private static void promptAddBudget(Scanner sc, BudgetManager manager) {
        try {
            System.out.print("Category: ");
            String category = sc.nextLine().trim();
            System.out.print("Amount: ");
            double amt = Double.parseDouble(sc.nextLine().trim());
            int id = new Random().nextInt(10000) + 3; // avoid colliding with seeded ids
            manager.addBudget(new Budget(id, category, amt));
            System.out.println("Budget added.");
        } catch (Exception e) {
            System.out.println("Failed to add budget: " + e.getMessage());
        }
    }

    private static void promptAddBill(Scanner sc, BudgetManager manager) {
        try {
            System.out.print("Name: ");
            String name = sc.nextLine().trim();
            System.out.print("Amount: ");
            double amt = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Due date (YYYY-MM-DD): ");
            LocalDate due = LocalDate.parse(sc.nextLine().trim());
            System.out.print("Recurring? (y/n): ");
            boolean recurring = sc.nextLine().trim().equalsIgnoreCase("y");
            int id = new Random().nextInt(10000) + 3;
            if (recurring) manager.addBill(new RecurringBill(id, name, amt, due));
            else manager.addBill(new OneTimeBill(id, name, amt, due));
            System.out.println("Bill added.");
        } catch (Exception e) {
            System.out.println("Failed to add bill: " + e.getMessage());
        }
    }
}
