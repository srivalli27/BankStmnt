import java.io.*;
import java.util.*;

class Account implements Serializable {
    String username;
    String password;
    double balance;
    ArrayList<String> transactions = new ArrayList<>();

    private static final long serialVersionUID = 1L;

    Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0.0;
        transactions.add("Account created with balance ₹0.0");
    }

    void deposit(double amount) {
        balance += amount;
        transactions.add("Deposited ₹" + amount + " | Balance: ₹" + balance);
    }

    void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactions.add("Withdrew ₹" + amount + " | Balance: ₹" + balance);
        } else {
            transactions.add("Failed withdrawal attempt of ₹" + amount + " (Insufficient balance)");
            System.out.println("⚠ Not enough balance!");
        }
    }

    void showTransactions() {
        System.out.println("\nTransaction History:");
        for (String t : transactions) {
            System.out.println(" - " + t);
        }
    }
}

public class Banking {
    static Scanner sc = new Scanner(System.in);
    static HashMap<String, Account> accounts = new HashMap<>();
    static final String DATA_FILE = "bank_data.txt";

    public static void main(String[] args) {
        loadData();

        System.out.println("🏦 Welcome to Mini Banking System 🏦");

        while (true) {
            System.out.println("\n1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> login();
                case 3 -> {
                    saveData();
                    System.out.println("✅ Thank you for using Mini Banking System!");
                    return;
                }
                default -> System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }

    static void createAccount() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        if (accounts.containsKey(username)) {
            System.out.println("⚠ Username already exists!");
            return;
        }
        System.out.print("Set password: ");
        String password = sc.nextLine();
        accounts.put(username, new Account(username, password));
        System.out.println("✅ Account created successfully!");
        saveData();
    }

    static void login() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        Account acc = accounts.get(username);

        if (acc != null && acc.password.equals(password)) {
            System.out.println("✅ Login successful! Welcome " + username + ".");
            bankMenu(acc);
        } else {
            System.out.println("❌ Invalid username or password.");
        }
    }

    static void bankMenu(Account acc) {
        while (true) {
            System.out.println("\n--- Banking Menu ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Transactions");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> System.out.println("💰 Current Balance: ₹" + acc.balance);
                case 2 -> {
                    System.out.print("Enter amount to deposit: ₹");
                    double dep = sc.nextDouble();
                    acc.deposit(dep);
                }
                case 3 -> {
                    System.out.print("Enter amount to withdraw: ₹");
                    double wd = sc.nextDouble();
                    acc.withdraw(wd);
                }
                case 4 -> acc.showTransactions();
                case 5 -> {
                    System.out.println("👋 Logged out successfully!");
                    saveData();
                    return;
                }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

    static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(new HashMap<>(accounts));
        } catch (Exception e) {
            System.out.println("⚠ Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    static void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            accounts = (HashMap<String, Account>) ois.readObject();
        } catch (Exception e) {
            System.out.println("⚠ Error loading data: " + e.getMessage());
        }
    }
}