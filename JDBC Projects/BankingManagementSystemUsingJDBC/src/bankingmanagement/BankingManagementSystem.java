
package bankingmanagement;

import java.util.Scanner;

public class BankingManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        CustomerService customerService = new CustomerService();
        AccountService accountService = new AccountService();
        TransactionService transactionService = new TransactionService();

        int choice = 0;

        while (choice != 9) {

            System.out.println("\n===== BANKING MANAGEMENT SYSTEM =====");
            System.out.println("1. Register Customer");
            System.out.println("2. View Customers");
            System.out.println("3. Create Bank Account");
            System.out.println("4. View Bank Accounts");
            System.out.println("5. Deposit Money");
            System.out.println("6. Withdraw Money");
            System.out.println("7. Transfer Funds");
            System.out.println("8. View Transaction History");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        customerService.registerCustomer(sc);
                        break;

                    case 2:
                        customerService.viewCustomers();
                        break;

                    case 3:
                        accountService.createAccount(sc);
                        break;

                    case 4:
                        accountService.viewAccounts();
                        break;

                    case 5:
                        transactionService.deposit(sc);
                        break;

                    case 6:
                        transactionService.withdraw(sc);
                        break;

                    case 7:
                        transactionService.transfer(sc);
                        break;

                    case 8:
                        transactionService.viewTransactionHistory(sc);
                        break;

                    case 9:
                        System.out.println(
                                "Thank you for using Banking Management System!");
                        break;

                    default:
                        System.out.println(
                                "Invalid choice. Please enter a number from 1 to 9.");
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid input. Please enter a valid number.");
            }
        }

        sc.close();
    }
}
