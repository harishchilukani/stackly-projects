package bankingmanagement;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TransactionService {

    // Validate and read a positive integer
    private int readPositiveInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                int value = Integer.parseInt(input);

                if (value <= 0) {
                    System.out.println(
                            "Error: Enter a number greater than zero.");
                } else {
                    return value;
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: Please enter a valid whole number.");
            }
        }
    }

    // Validate and read a positive monetary amount
    private BigDecimal readPositiveAmount(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                if (!input.matches("[0-9]+(\\.[0-9]{1,2})?")) {
                    System.out.println(
                            "Error: Enter a valid amount with up to "
                                    + "two decimal places.");
                    continue;
                }

                BigDecimal amount = new BigDecimal(input);

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println(
                            "Error: Amount must be greater than zero.");
                } else if (amount.precision() > 12) {
                    System.out.println(
                            "Error: Amount exceeds the allowed limit.");
                } else {
                    return amount;
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: Please enter a valid amount.");
            }
        }
    }

    // Deposit money
    public void deposit(Scanner sc) {

        int accountId = readPositiveInt(
                sc, "Enter account ID: ");

        BigDecimal amount = readPositiveAmount(
                sc, "Enter deposit amount: ");

        String updateSql =
                "UPDATE accounts SET balance = balance + ? "
                        + "WHERE account_id = ?";

        String insertSql =
                "INSERT INTO transactions "
                        + "(account_id, transaction_type, amount, description) "
                        + "VALUES (?, 'Deposit', ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement update =
                         con.prepareStatement(updateSql);
                 PreparedStatement insert =
                         con.prepareStatement(insertSql)) {

                update.setBigDecimal(1, amount);
                update.setInt(2, accountId);

                if (update.executeUpdate() != 1) {
                    throw new BankingException("Account not found.");
                }

                insert.setInt(1, accountId);
                insert.setBigDecimal(2, amount);
                insert.setString(3, "Cash deposit");
                insert.executeUpdate();

                con.commit();
                System.out.println("Deposit successful!");

            } catch (BankingException e) {
                rollback(con, e);
                System.out.println(
                        "Banking error: " + e.getMessage());

            } catch (SQLException e) {
                rollback(con, e);
                System.out.println(
                        "Deposit failed: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(
                    "Database error: " + e.getMessage());
        }
    }

    // Withdraw money
    public void withdraw(Scanner sc) {

        int accountId = readPositiveInt(
                sc, "Enter account ID: ");

        BigDecimal amount = readPositiveAmount(
                sc, "Enter withdrawal amount: ");

        String updateSql =
                "UPDATE accounts SET balance = balance - ? "
                        + "WHERE account_id = ? AND balance >= ?";

        String insertSql =
                "INSERT INTO transactions "
                        + "(account_id, transaction_type, amount, description) "
                        + "VALUES (?, 'Withdrawal', ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement update =
                         con.prepareStatement(updateSql);
                 PreparedStatement insert =
                         con.prepareStatement(insertSql)) {

                update.setBigDecimal(1, amount);
                update.setInt(2, accountId);
                update.setBigDecimal(3, amount);

                if (update.executeUpdate() != 1) {
                    throw new BankingException(
                            "Account not found or insufficient balance.");
                }

                insert.setInt(1, accountId);
                insert.setBigDecimal(2, amount);
                insert.setString(3, "Cash withdrawal");
                insert.executeUpdate();

                con.commit();
                System.out.println("Withdrawal successful!");

            } catch (BankingException e) {
                rollback(con, e);
                System.out.println(
                        "Banking error: " + e.getMessage());

            } catch (SQLException e) {
                rollback(con, e);
                System.out.println(
                        "Withdrawal failed: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(
                    "Database error: " + e.getMessage());
        }
    }

    // Transfer funds between accounts
    public void transfer(Scanner sc) {

        int fromAccount = readPositiveInt(
                sc, "Enter sender account ID: ");

        int toAccount = readPositiveInt(
                sc, "Enter receiver account ID: ");

        if (fromAccount == toAccount) {
            System.out.println(
                    "Error: Sender and receiver must be different.");
            return;
        }

        BigDecimal amount = readPositiveAmount(
                sc, "Enter transfer amount: ");

        String lockSql =
                "SELECT account_id, balance FROM accounts "
                        + "WHERE account_id IN (?, ?) "
                        + "ORDER BY account_id FOR UPDATE";

        String debitSql =
                "UPDATE accounts SET balance = balance - ? "
                        + "WHERE account_id = ? AND balance >= ?";

        String creditSql =
                "UPDATE accounts SET balance = balance + ? "
                        + "WHERE account_id = ?";

        String transactionSql =
                "INSERT INTO transactions "
                        + "(account_id, transaction_type, amount, description) "
                        + "VALUES (?, 'Transfer', ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                int foundAccounts = 0;
                BigDecimal senderBalance = null;

                // Lock both accounts while the transfer is processed
                try (PreparedStatement lock =
                             con.prepareStatement(lockSql)) {

                    lock.setInt(1, fromAccount);
                    lock.setInt(2, toAccount);

                    try (ResultSet rs = lock.executeQuery()) {

                        while (rs.next()) {
                            foundAccounts++;

                            if (rs.getInt("account_id") == fromAccount) {
                                senderBalance =
                                        rs.getBigDecimal("balance");
                            }
                        }
                    }
                }

                if (foundAccounts != 2) {
                    throw new BankingException(
                            "Sender or receiver account does not exist.");
                }

                if (senderBalance.compareTo(amount) < 0) {
                    throw new BankingException(
                            "Insufficient balance.");
                }

                // Deduct money from sender
                try (PreparedStatement debit =
                             con.prepareStatement(debitSql)) {

                    debit.setBigDecimal(1, amount);
                    debit.setInt(2, fromAccount);
                    debit.setBigDecimal(3, amount);

                    if (debit.executeUpdate() != 1) {
                        throw new BankingException(
                                "Sender debit failed.");
                    }
                }

                // Add money to receiver
                try (PreparedStatement credit =
                             con.prepareStatement(creditSql)) {

                    credit.setBigDecimal(1, amount);
                    credit.setInt(2, toAccount);

                    if (credit.executeUpdate() != 1) {
                        throw new BankingException(
                                "Receiver credit failed.");
                    }
                }

                // Record both sides of the transfer
                try (PreparedStatement insert =
                             con.prepareStatement(transactionSql)) {

                    insert.setInt(1, fromAccount);
                    insert.setBigDecimal(2, amount);
                    insert.setString(3,
                            "Transfer sent to account " + toAccount);
                    insert.executeUpdate();

                    insert.setInt(1, toAccount);
                    insert.setBigDecimal(2, amount);
                    insert.setString(3,
                            "Transfer received from account " + fromAccount);
                    insert.executeUpdate();
                }

                con.commit();
                System.out.println("Fund transfer successful!");

            } catch (BankingException e) {
                rollback(con, e);
                System.out.println(
                        "Banking error: " + e.getMessage());
                System.out.println(
                        "Transfer changes have been rolled back.");

            } catch (SQLException e) {
                rollback(con, e);
                System.out.println(
                        "Transfer failed: " + e.getMessage());
                System.out.println(
                        "Transfer changes have been rolled back.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Database error: " + e.getMessage());
        }
    }

    // View transaction history
    public void viewTransactionHistory(Scanner sc) {

        int accountId = readPositiveInt(
                sc, "Enter account ID: ");

        String sql =
                "SELECT transaction_id, transaction_type, amount, "
                        + "transaction_date, description "
                        + "FROM transactions WHERE account_id = ? "
                        + "ORDER BY transaction_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountId);

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                System.out.println("\n--- Transaction History ---");

                while (rs.next()) {
                    found = true;

                    System.out.printf(
                            "Transaction ID: %d | Type: %s | "
                                    + "Amount: %s | Date: %s | Description: %s%n",
                            rs.getInt("transaction_id"),
                            rs.getString("transaction_type"),
                            rs.getBigDecimal("amount"),
                            rs.getTimestamp("transaction_date"),
                            rs.getString("description"));
                }

                if (!found) {
                    System.out.println(
                            "No transactions found for this account.");
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Unable to retrieve history: " + e.getMessage());
        }
    }

    // Roll back failed database transactions
    private void rollback(Connection con, Exception originalError) {
        try {
            con.rollback();
        } catch (SQLException rollbackError) {
            originalError.addSuppressed(rollbackError);
            System.out.println(
                    "Rollback failed: " + rollbackError.getMessage());
        }
    }
}
