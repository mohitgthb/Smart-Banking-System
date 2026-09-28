# Smart Banking System

A Java-based banking application designed to demonstrate **Object-Oriented Programming, repository-based architecture, persistent storage, database transactions, exception handling, concurrent transfers, and a JavaFX desktop interface**.

The system supports account management, deposits, withdrawals, fund transfers, balance checking, and transaction history using both file-based and MySQL persistence.

---

## Features

### Account Management

* Create Savings and Current accounts
* Unique account numbers
* Account type-specific behavior
* Opening balance validation
* Retrieve account details
* Update account information

### Banking Operations

* Deposit money
* Withdraw money
* Transfer funds between accounts
* Check account balance
* View transaction history

### Validation & Exception Handling

* Account-not-found validation
* Negative/invalid amount validation
* Insufficient-balance validation
* Same-account transfer prevention
* Custom checked exceptions
* Meaningful error messages

### Transaction Management

* Database transactions using JDBC
* Commit on successful operations
* Rollback when an operation fails
* Atomic fund transfers
* Transaction history for deposits, withdrawals, and transfers
* `TRANSFER_OUT` and `TRANSFER_IN` records for transfers

### Concurrent Transfers

* Transfer operations can be executed concurrently
* Database row locking using `SELECT ... FOR UPDATE`
* Consistent lock ordering to reduce deadlock risk
* Atomic updates of sender and receiver accounts
* `TransferTask` implementation using `Runnable`

### Persistence

The application supports multiple persistence implementations through the repository abstraction:

* File-based persistence using serialization
* MySQL persistence using JDBC

### Desktop GUI

JavaFX interface providing:

* Dashboard
* Create Account
* Deposit
* Withdraw
* Transfer
* Balance
* Transaction History

---

## Architecture

The application follows a layered architecture with the **Repository Pattern**.

```text
                    ┌──────────────────────┐
                    │      JavaFX UI       │
                    │  FXML + Controllers  │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   AccountService     │
                    │ Business Operations   │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   BankRepository     │
                    │      Interface       │
                    └──────────┬───────────┘
                               │
                ┌──────────────┴──────────────┐
                ▼                             ▼
      ┌──────────────────┐          ┌──────────────────┐
      │FileBankRepository│          │SqlBankRepository │
      │   Serialization  │          │      JDBC        │
      └────────┬─────────┘          └────────┬─────────┘
               │                             │
               ▼                             ▼
         bank_data.ser                    MySQL
```

---

## Project Structure

```text
SmartBankingSystem/
│
├── app/
│   └── Main.java
│
├── exception/
│   ├── AccountNotFoundException.java
│   ├── InsufficientBalanceException.java
│   └── InvalidAmountException.java
│
├── factory/
│   └── AccountFactory.java
│
├── model/
│   ├── Account.java
│   ├── CurrentAccount.java
│   ├── SavingsAccount.java
│   └── Transaction.java
│
├── repository/
│   ├── BankRepository.java
│   ├── DBconnection.java
│   ├── FileBankRepository.java
│   ├── FileStorage.java
│   └── SqlBankRepository.java
│
├── service/
│   ├── AccountService.java
│   ├── TransactionService.java
│   │
│   └── strategy/
│       ├── InterestStrategy.java
│       ├── SavingsInterest.java
│       └── CurrentInterest.java
│
├── ui/
│   ├── MainApp.java
│   ├── AppContext.java
│   ├── DashboardController.java
│   ├── CreateAccountController.java
│   ├── DepositController.java
│   ├── WithdrawController.java
│   ├── TransferController.java
│   ├── BalanceController.java
│   ├── TransactionsController.java
│   │
│   ├── Dashboard.fxml
│   ├── CreateAccount.fxml
│   ├── Deposit.fxml
│   ├── Withdraw.fxml
│   ├── Transfer.fxml
│   ├── Balance.fxml
│   └── Transactions.fxml
│
├── util/
│   ├── FileUtil.java
│   ├── IdGenerator.java
│   └── TransferTask.java
│
├── lib/
│   └── mysql-connector-j-8.3.0.jar
│
├── .env
├── .gitignore
├── README.md
└── bank_data.ser
```

---

## Technologies Used

| Technology         | Purpose                         |
| ------------------ | ------------------------------- |
| Java 17            | Core application                |
| JavaFX             | Desktop GUI                     |
| FXML               | GUI layouts                     |
| MySQL              | Relational database persistence |
| JDBC               | Database connectivity           |
| Java Serialization | File-based persistence          |
| Git                | Version control                 |
| GitHub             | Source-code hosting             |

---

## Core Java Concepts Demonstrated

### Object-Oriented Programming

The project uses:

* Encapsulation
* Inheritance
* Abstraction
* Polymorphism
* Interfaces
* Composition

The base `Account` class provides common account behavior, while:

```text
Account
├── SavingsAccount
└── CurrentAccount
```

provide account-specific behavior.

---

## Design Patterns

### Repository Pattern

The `BankRepository` interface abstracts persistence operations from the business layer.

```java
BankRepository
      │
      ├── FileBankRepository
      │
      └── SqlBankRepository
```

This allows the service layer to work without being tightly coupled to a specific storage mechanism.

For example:

```java
AccountService service =
        new AccountService(repository);
```

The service can operate with either the file-based or SQL repository.

---

### Factory Pattern

`AccountFactory` is responsible for creating account objects based on the requested account type.

```text
AccountFactory
      │
      ├── SavingsAccount
      └── CurrentAccount
```

This keeps object creation separate from the business service.

---

### Strategy Pattern

Interest calculation is separated using:

```text
InterestStrategy
      │
      ├── SavingsInterest
      └── CurrentInterest
```

This allows interest behavior to vary independently from the account model.

---

## Database Design

The MySQL implementation uses account and transaction data.

### Accounts

Conceptually:

```text
ACCOUNTS
├── ACCOUNT_NUMBER
├── TYPE
├── NAME
└── BALANCE
```

### Transactions

```text
TRANSACTIONS
├── ID
├── ACCOUNT_NUMBER
├── TYPE
├── AMOUNT
└── DATE
```

Transaction types include:

```text
DEPOSIT
WITHDRAW
TRANSFER_OUT
TRANSFER_IN
```

A transfer creates two transaction records:

```text
Sender
  └── TRANSFER_OUT

Receiver
  └── TRANSFER_IN
```

This provides an auditable transaction history for both accounts.

---

# Transaction Handling

One of the main technical aspects of the project is safe fund transfer handling.

A transfer follows this general flow:

```text
Request Transfer
       │
       ▼
Validate Amount
       │
       ▼
Validate Source/Destination
       │
       ▼
Begin Database Transaction
       │
       ▼
Lock Required Account Rows
       │
       ▼
Check Account Balances
       │
       ▼
Update Sender Balance
       │
       ▼
Update Receiver Balance
       │
       ▼
Insert Transaction Records
       │
       ▼
Commit
```

If any operation fails:

```text
Exception
   │
   ▼
ROLLBACK
   │
   ▼
Restore Previous Database State
```

This prevents a situation where money is deducted from one account but not credited to the other.

---

## Concurrent Transfer Handling

The project includes `TransferTask`, which implements `Runnable` and can execute transfers concurrently.

The SQL repository protects concurrent transfers using database row locking.

The important mechanism is:

```sql
SELECT ...
FROM ACCOUNTS
WHERE ACCOUNT_NUMBER = ?
FOR UPDATE;
```

The application also establishes a consistent ordering when acquiring account locks.

Conceptually:

```text
Thread 1                 Thread 2

A → B                    B → A

Lock A                   Lock A
Lock B                   Lock B
   │                         │
   ▼                         ▼
Transfer                 Transfer
```

Consistent lock ordering helps reduce the possibility of deadlocks when multiple transfers involve the same accounts.

---

# Error Handling

Custom exceptions are used to represent business-level failures.

### `AccountNotFoundException`

Used when the requested account does not exist.

### `InsufficientBalanceException`

Used when an account does not have sufficient funds for a withdrawal or transfer.

### `InvalidAmountException`

Used when an invalid amount is supplied, such as a negative amount.

Example:

```java
if (amount <= 0) {
    throw new InvalidAmountException(
        "Amount must be greater than zero"
    );
}
```

---

# Persistence Options

## File-Based Persistence

The project includes:

```text
FileBankRepository
FileStorage
```

and Java serialization can be used to persist account data locally.

This provides a simple persistence option without requiring a database server.

---

## MySQL Persistence

The SQL implementation uses:

```text
SqlBankRepository
       │
       ▼
JDBC
       │
       ▼
MySQL
```

Database operations use prepared statements and explicit transaction management.

---

# JavaFX Application

The project includes a desktop interface built using JavaFX and FXML.

### Dashboard

Provides access to the major banking operations.

### Create Account

Creates a new Savings or Current account.

### Deposit

Adds funds to an account.

### Withdraw

Withdraws funds after validating the available balance.

### Transfer

Transfers funds atomically between two accounts.

### Balance

Displays the current account balance.

### Transactions

Displays transaction history stored in the database.

---

# Setup

## Prerequisites

Install:

* Java JDK 17
* JavaFX SDK 21
* MySQL Server
* MySQL Connector/J

Verify Java:

```bash
java --version
javac --version
```

---

# MySQL Configuration

Create a MySQL database:

```sql
CREATE DATABASE practice;
```

Select it:

```sql
USE practice;
```

Create the required tables according to the SQL schema used by the project.

Configure the database connection using the project's environment configuration.

Do **not** commit real database credentials to GitHub.

---

# JavaFX Configuration

Download and extract the JavaFX SDK for Windows.

Example:

```text
C:\Users\<username>\Downloads\javafx-sdk-21.0.11
```

The SDK should contain:

```text
javafx-sdk-21.0.11/
└── lib/
    ├── javafx.base.jar
    ├── javafx.controls.jar
    ├── javafx.fxml.jar
    ├── javafx.graphics.jar
    └── ...
```

---

# Compilation

From the project root:

```bat
javac --module-path "C:\path\to\javafx-sdk-21.0.11\lib" --add-modules javafx.controls,javafx.fxml -cp "lib\mysql-connector-j-8.3.0.jar" -d out app\Main.java exception\*.java factory\*.java model\*.java repository\*.java service\*.java service\strategy\*.java ui\*.java util\*.java
```

---

# Running the JavaFX Application

Because JavaFX requires native libraries, specify the JavaFX SDK's native library directory.

```bat
java --module-path "C:\path\to\javafx-sdk-21.0.0\lib" --add-modules javafx.controls,javafx.fxml -Djava.library.path="C:\path\to\javafx-sdk-21.0.0\bin" -cp "out;lib\mysql-connector-j-8.3.0.jar" ui.MainApp
```

Replace the JavaFX SDK path with the location on your machine.

---

# Running the Console Application

The project also contains a console entry point.

Compile the project and run:

```bat
java -cp "out;lib\mysql-connector-j-8.3.0.jar" app.Main
```

The console provides operations such as:

```text
1. Create Account
2. Deposit
3. Withdraw
4. Transfer
5. Check Balance
6. Exit
```

---

# Testing

The following scenarios should be tested before using the application:

### Account Tests

* Create Savings account
* Create Current account
* Reject invalid opening balance
* Retrieve existing account
* Handle non-existent account

### Deposit Tests

* Valid deposit
* Invalid amount
* Deposit to non-existent account
* Verify updated balance
* Verify transaction record

### Withdrawal Tests

* Valid withdrawal
* Insufficient balance
* Invalid amount
* Non-existent account
* Verify updated balance
* Verify transaction record

### Transfer Tests

* Valid transfer
* Insufficient balance
* Invalid amount
* Same source and destination
* Non-existent source account
* Non-existent destination account
* Verify sender balance
* Verify receiver balance
* Verify `TRANSFER_OUT`
* Verify `TRANSFER_IN`

### Transaction Tests

Verify transaction history using:

```sql
SELECT ACCOUNT_NUMBER, TYPE, AMOUNT, DATE
FROM TRANSACTIONS
ORDER BY DATE DESC;
```

---

# Rollback Scenario

A failed transfer should not partially update the accounts.

Example:

```text
Sender balance   = ₹100
Receiver balance = ₹500

Attempt transfer = ₹500
```

The operation should fail because the sender does not have sufficient funds.

After failure:

```text
Sender balance   = ₹100
Receiver balance = ₹500
```

No partial transfer should occur.

---

# Project Goals

This project was developed to demonstrate practical understanding of:

* Java OOP
* Clean separation of responsibilities
* Repository abstraction
* Design patterns
* JDBC
* MySQL
* Database transactions
* Rollback
* Row-level locking
* Concurrent operations
* Exception handling
* JavaFX
* FXML
* Persistent storage
* Git/GitHub

---

# Future Improvements

Possible future enhancements include:

* User authentication
* Role-based access control
* Password hashing
* Account statements
* Pagination for transaction history
* Scheduled interest calculation
* REST API layer
* Unit and integration test suite
* Dockerized MySQL environment
* Connection pooling
* Structured application logging
* Maven/Gradle build configuration
* Automated CI/CD pipeline
* Improved JavaFX styling and responsive layouts

---

# Learning Outcomes

Through this project, the following software engineering concepts are demonstrated:

```text
OOP
 │
 ├── Inheritance
 ├── Encapsulation
 ├── Abstraction
 └── Polymorphism
       │
       ▼
Design Patterns
 │
 ├── Repository
 ├── Factory
 └── Strategy
       │
       ▼
Persistence
 │
 ├── File Storage
 └── MySQL + JDBC
       │
       ▼
Database Engineering
 │
 ├── Transactions
 ├── Commit / Rollback
 ├── Row Locking
 └── Concurrency
       │
       ▼
Application Layer
 │
 └── JavaFX Desktop UI
```

---

## Author

**Mohit Choudhari**

Computer Engineering Student

GitHub: [mohitgthb](https://github.com/mohitgthb)
