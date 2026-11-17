package model;

/**
 * Master Plan 4-State Machine: SETTLEMENT STATE MACHINE (Trang 4, Mục 2.3 & Trang 14)
 * Handles merchant payouts, driver earnings reconciliation, and platform commission.
 */
public enum SettlementState {
    OPEN("Chờ chu kỳ đối soát"),
    CALCULATED("Đã tính toán nghĩa vụ tài chính"),
    APPROVED("Kế toán đã duyệt chi trả"),
    PROCESSING("Đang chuyển khoản ngân hàng"),
    PAID("Đã tất toán thành công"),
    ON_HOLD("Tạm giữ do tranh chấp hoặc khiếu nại"),
    FAILED("Tất toán thất bại");

    private String description;

    SettlementState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean canTransitionTo(SettlementState next) {
        if (this == PAID) {
            return false;
        }
        switch (this) {
            case OPEN:
                return next == CALCULATED || next == ON_HOLD;
            case CALCULATED:
                return next == APPROVED || next == ON_HOLD;
            case APPROVED:
                return next == PROCESSING || next == ON_HOLD;
            case PROCESSING:
                return next == PAID || next == FAILED;
            case ON_HOLD:
                return next == CALCULATED || next == APPROVED || next == FAILED;
            case FAILED:
                return next == PROCESSING;
            default:
                return false;
        }
    }
}
