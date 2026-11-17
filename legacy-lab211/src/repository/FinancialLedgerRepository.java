package repository;

import model.FinancialLedgerEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Financial Ledger Entries adhering to Double-Entry Accounting Rules.
 * Every transaction creates matching Debit and Credit entries with guaranteed balance.
 * Adheres strictly to LAB211 rules: pure OOP, 100% this., no static outside main, no final, no protected.
 */
public class FinancialLedgerRepository extends CsvRepository<FinancialLedgerEntry> {

    public FinancialLedgerRepository() {
        super("data/financial_ledger.csv", "id;transactionRef;orderId;accountCode;debitVnd;creditVnd;currency;occurredAt;sourceType;sourceId;reversalOf;note;version");
    }

    public FinancialLedgerRepository(String customPath) {
        super(customPath, "id;transactionRef;orderId;accountCode;debitVnd;creditVnd;currency;occurredAt;sourceType;sourceId;reversalOf;note;version");
    }

    @Override
    public FinancialLedgerEntry createEntity() {
        return new FinancialLedgerEntry();
    }

    /**
     * Records a balanced double-entry transaction.
     */
    public synchronized boolean recordDoubleEntry(String txRef, String orderId, String debitAccount,
                                                  String creditAccount, long amountVnd, String note) {
        if (amountVnd <= 0) {
            return false;
        }

        String debitId = "LEDG_" + System.currentTimeMillis() + "_D";
        String creditId = "LEDG_" + (System.currentTimeMillis() + 1) + "_C";

        FinancialLedgerEntry debitEntry = new FinancialLedgerEntry(
                debitId, txRef, orderId, debitAccount, amountVnd, 0L, "ORDER_PAYMENT", orderId, "", note + " [DEBIT]"
        );

        FinancialLedgerEntry creditEntry = new FinancialLedgerEntry(
                creditId, txRef, orderId, creditAccount, 0L, amountVnd, "ORDER_PAYMENT", orderId, "", note + " [CREDIT]"
        );

        this.save(debitEntry);
        this.save(creditEntry);
        return true;
    }

    /**
     * Verifies system-wide financial integrity: Sum of all debits must equal sum of all credits.
     */
    public synchronized boolean verifyLedgerBalance() {
        List<FinancialLedgerEntry> entries = this.findAll();
        long totalDebit = 0;
        long totalCredit = 0;
        for (int i = 0; i < entries.size(); i++) {
            FinancialLedgerEntry e = entries.get(i);
            totalDebit += e.getDebitVnd();
            totalCredit += e.getCreditVnd();
        }
        return totalDebit == totalCredit;
    }

    public synchronized List<FinancialLedgerEntry> findByOrderId(String orderId) {
        List<FinancialLedgerEntry> res = new ArrayList<FinancialLedgerEntry>();
        if (orderId == null) {
            return res;
        }
        List<FinancialLedgerEntry> all = this.findAll();
        for (int i = 0; i < all.size(); i++) {
            FinancialLedgerEntry e = all.get(i);
            if (orderId.equalsIgnoreCase(e.getOrderId())) {
                res.add(e);
            }
        }
        return res;
    }
}
