package model;

import java.time.LocalDateTime;

/**
 * Double-Entry Financial Ledger Entry (Trang 10 & Trang 14 Master Plan).
 * Standardized accounting record representing debits and credits across platform accounts:
 * - 111: Tiền mặt COD
 * - 112: Tiền gửi ngân hàng / VietQR Gateway
 * - 331_REST: Phải trả nhà hàng (80% tiền món)
 * - 331_DRV: Phải trả tài xế (100% phí giao hàng)
 * - 511_COMM: Doanh thu hoa hồng sàn (20% tiền món)
 * - 642_PROMO: Chi phí trợ giá Voucher
 * Adheres strictly to LAB211 rules: pure OOP, 100% this., no static outside main, no final, no protected.
 */
public class FinancialLedgerEntry extends BaseEntity {
    private String transactionRef;
    private String orderId;
    private String accountCode;
    private long debitVnd;
    private long creditVnd;
    private String currency;
    private String occurredAt;
    private String sourceType;
    private String sourceId;
    private String reversalOf;
    private String note;

    public FinancialLedgerEntry() {
        super();
        this.transactionRef = "";
        this.orderId = "";
        this.accountCode = "";
        this.debitVnd = 0L;
        this.creditVnd = 0L;
        this.currency = "VND";
        this.occurredAt = LocalDateTime.now().toString();
        this.sourceType = "ORDER_PAYMENT";
        this.sourceId = "";
        this.reversalOf = "";
        this.note = "";
    }

    public FinancialLedgerEntry(String id, String transactionRef, String orderId,
                                String accountCode, long debitVnd, long creditVnd,
                                String sourceType, String sourceId, String reversalOf, String note) {
        super(id);
        this.transactionRef = transactionRef;
        this.orderId = orderId;
        this.accountCode = accountCode;
        this.debitVnd = debitVnd;
        this.creditVnd = creditVnd;
        this.currency = "VND";
        this.occurredAt = LocalDateTime.now().toString();
        this.sourceType = sourceType != null ? sourceType : "ORDER_PAYMENT";
        this.sourceId = sourceId != null ? sourceId : "";
        this.reversalOf = reversalOf != null ? reversalOf : "";
        this.note = note != null ? note : "";
    }

    public String getTransactionRef() { return this.transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    public String getOrderId() { return this.orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getAccountCode() { return this.accountCode; }
    public void setAccountCode(String accountCode) { this.accountCode = accountCode; }

    public long getDebitVnd() { return this.debitVnd; }
    public void setDebitVnd(long debitVnd) { this.debitVnd = debitVnd; }

    public long getCreditVnd() { return this.creditVnd; }
    public void setCreditVnd(long creditVnd) { this.creditVnd = creditVnd; }

    public String getCurrency() { return this.currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getOccurredAt() { return this.occurredAt; }
    public void setOccurredAt(String occurredAt) { this.occurredAt = occurredAt; }

    public String getSourceType() { return this.sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

    public String getSourceId() { return this.sourceId; }
    public void setSourceId(String sourceId) { this.sourceId = sourceId; }

    public String getReversalOf() { return this.reversalOf; }
    public void setReversalOf(String reversalOf) { this.reversalOf = reversalOf; }

    public String getNote() { return this.note; }
    public void setNote(String note) { this.note = note; }

    private String encodeField(String text) {
        if (text == null) return "";
        return text.replace(";", "%3B").replace(",", "%2C");
    }

    private String decodeField(String text) {
        if (text == null) return "";
        return text.replace("%3B", ";").replace("%2C", ",");
    }

    @Override
    public String toCsvLine() {
        return String.join(";",
                this.encodeField(this.getId()),
                this.encodeField(this.transactionRef),
                this.encodeField(this.orderId),
                this.encodeField(this.accountCode),
                String.valueOf(this.debitVnd),
                String.valueOf(this.creditVnd),
                this.encodeField(this.currency),
                this.encodeField(this.occurredAt),
                this.encodeField(this.sourceType),
                this.encodeField(this.sourceId),
                this.encodeField(this.reversalOf),
                this.encodeField(this.note),
                String.valueOf(this.getVersion()));
    }

    @Override
    public void fromCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) return;
        String[] p = line.split(";", -1);
        if (p.length >= 12) {
            this.setId(this.decodeField(p[0].trim()));
            this.transactionRef = this.decodeField(p[1].trim());
            this.orderId = this.decodeField(p[2].trim());
            this.accountCode = this.decodeField(p[3].trim());
            try { this.debitVnd = Long.parseLong(p[4].trim()); } catch (Exception e) { this.debitVnd = 0L; }
            try { this.creditVnd = Long.parseLong(p[5].trim()); } catch (Exception e) { this.creditVnd = 0L; }
            this.currency = this.decodeField(p[6].trim());
            this.occurredAt = this.decodeField(p[7].trim());
            this.sourceType = this.decodeField(p[8].trim());
            this.sourceId = this.decodeField(p[9].trim());
            this.reversalOf = this.decodeField(p[10].trim());
            this.note = this.decodeField(p[11].trim());
            if (p.length > 12) {
                try { this.setVersion(Long.parseLong(p[12].trim())); } catch (Exception ignored) {}
            }
        }
    }
}
