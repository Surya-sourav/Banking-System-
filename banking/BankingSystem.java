package banking;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Interface for common banking operations
interface Transactionable {
    void deposit(double amount);
    void withdraw(double amount);
    void transfer(Account destination, double amount);
}

// Abstract class representing an account
abstract class Account implements Transactionable {
    protected String accountNumber;
    protected double balance;
    protected Customer owner;
    protected Map<String, Double> transactions;

    public Account(String accountNumber, Customer owner) {
        this.accountNumber = accountNumber;
        this.balance = 0.0;
        this.owner = owner;
        this.transactions = new HashMap<>();
    }

    public void checkBalance() {
        System.out.println("Current Balance: " + balance);
    }

    // Corrected access modifiers and method signatures
    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);

    public void addTransaction(String type, double amount) {
        transactions.put(type, amount);
    }

    public Map<String, Double> getTransactions() {
        return transactions;
    }

    @Override
    public void transfer(Account destination, double amount) {
        if (amount <= balance) {
            withdraw(amount);
            destination.deposit(amount);
            addTransaction("Transfer to " + destination.accountNumber, amount);
        } else {
            System.out.println("Insufficient balance!");
        }
    }
}

// Concrete class representing a savings account
class SavingsAccount extends Account {
    public SavingsAccount(String accountNumber, Customer owner) {
        super(accountNumber, owner);
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposit", amount);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            addTransaction("Withdrawal", amount);
        } else {
            System.out.println("Insufficient balance!");
        }
    }
}

// Class representing a customer
class Customer {
    private String name;
    private String address;
    private String contactNumber;

    public Customer(String name, String address, String contactNumber) {
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
    }
    
    // getters and setters
}

// Main class to run the banking system
public class BankingSystem {
    private static Map<String, Customer> users = new HashMap<>();
    private static Map<String, Account> accounts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            System.out.println("####################################################################################");
            System.out.println("#                                                                                  #");
            System.out.println("#                                    | WELCOME |                                   #");
            System.out.println("#                   GOLDEN LOCK BANK |    $    |ONE STOP SOLUTION FOR FINANCE      #");
            System.out.println("#                                                                                  #");
            System.out.println("#      Founders - SURYA SOURAV PARIDA , UDIT SAHOO , SWAGAT NAYAK , TUTUL SWAIN    #");
            System.out.println("#                                                                                  #");
            System.out.println("###################################################################################");
            System.out.println("1. New User\n2. Existing User\n3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline character
            
            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    existingUserMenu();
                    break;
                case 3:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice! Please choose again.");
            }
        }
    }

    private static void createUser() {
        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("Enter address: ");
        String address = scanner.nextLine();
        System.out.println("Enter contact number: ");
        String contactNumber = scanner.nextLine();

        Customer newUser = new Customer(name, address, contactNumber);
        users.put(contactNumber, newUser);
        System.out.println("User created successfully!");
    }

    private static void existingUserMenu() {
        System.out.println("Enter contact number: ");
        String contactNumber = scanner.nextLine();
        Customer existingUser = users.get(contactNumber);
        if (existingUser == null) {
            System.out.println("User not found! Please create a new user.");
            return;
        }

        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("1. Open Account\n2. Login to Account\n3. Back");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline character

            switch (choice) {
                case 1:
                    openAccount(existingUser);
                    break;
                case 2:
                    loginToAccount(existingUser);
                    break;
                case 3:
                    loggedIn = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please choose again.");
            }
        }
    }

    private static void openAccount(Customer user) {
        System.out.println("Enter account number: ");
        String accountNumber = scanner.nextLine();
        Account newAccount = new SavingsAccount(accountNumber, user);
        accounts.put(accountNumber, newAccount);
        System.out.println("Account opened successfully!");
    }

    private static void loginToAccount(Customer user) {
        System.out.println("Enter account number: ");
        String accountNumber = scanner.nextLine();
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("Account not found! Please open an account.");
            return;
        }

        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("1. Deposit\n2. Withdraw\n3. Transfer\n4. Check Balance\n5. View Transactions\n6. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline character

            switch (choice) {
                case 1:
                    System.out.println("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    break;
                case 2:
                    System.out.println("Enter withdrawal amount: ");
                    double withdrawalAmount = scanner.nextDouble();
                    account.withdraw(withdrawalAmount);
                    break;
                case 3:
                    System.out.println("Enter destination account number: ");
                    String destinationAccountNumber = scanner.nextLine();
                    Account destinationAccount = accounts.get(destinationAccountNumber);
                    if (destinationAccount == null) {
                        System.out.println("Destination account not found!");
                        break;
                    }
                    System.out.println("Enter transfer amount: ");
                    double transferAmount = scanner.nextDouble();
                    account.transfer(destinationAccount, transferAmount);
                    break;
                case 4:
                    account.checkBalance();
                    break;
                case 5:
                    viewTransactions(account);
                    break;
                case 6:
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please choose again.");
            }
        }
    }

    private static void viewTransactions(Account account) {
        Map<String, Double> transactions = account.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
            return;
        }
        System.out.println("Transaction History:");
        for (Map.Entry<String, Double> entry : transactions.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
