package ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.beans.property.SimpleStringProperty;

import model.Transaction;
import service.AccountService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionsController {

    @FXML
    private TextField accountField;

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, String> amountColumn;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private Label messageLabel;

    private final AccountService service =
            AppContext.getAccountService();

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(
                    "dd-MM-yyyy HH:mm:ss"
            );

    @FXML
    public void initialize() {

        typeColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getType()
                        )
        );

        amountColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                String.format(
                                        "₹%.2f",
                                        data.getValue().getAmount()
                                )
                        )
        );

        dateColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getDate()
                                        .format(formatter)
                        )
        );
    }

    @FXML
    private void handleSearch() {

        try {

            String accountNumber =
                    accountField.getText().trim();

            if (accountNumber.isEmpty()) {

                messageLabel.setText(
                        "Enter account number."
                );

                return;
            }

            List<Transaction> transactions =
                    service.getTransactions(
                            accountNumber
                    );

            transactionTable.setItems(
                    FXCollections.observableArrayList(
                            transactions
                    )
            );

            if (transactions.isEmpty()) {

                messageLabel.setText(
                        "No transactions found."
                );

            } else {

                messageLabel.setText(
                        transactions.size()
                                + " transaction(s) found."
                );
            }

        } catch (Exception e) {

            messageLabel.setText(
                    e.getMessage()
            );
        }
    }
}