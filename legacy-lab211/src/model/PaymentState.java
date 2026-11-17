package model;

/**
 * Master Plan 4-State Machine: PAYMENT STATE MACHINE (Trang 4, Mục 2.3)
 * Decoupled payment lifecycle supporting Authorization, Capture, and Refunds.
 */
public enum PaymentState {
    INITIATED("Khởi tạo yêu cầu thanh toán"),
    AUTHORIZED("Đã ủy quyền/Chờ tiền về"),
    CAPTURED("Đã thanh toán thành công"),
    FAILED("Thanh toán thất bại"),
    REFUND_PENDING("Đang chờ hoàn tiền"),
    REFUNDED("Đã hoàn tiền thành công");

    private String description;

    PaymentState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean canTransitionTo(PaymentState next) {
        if (this == FAILED || this == REFUNDED) {
            return false;
        }
        switch (this) {
            case INITIATED:
                return next == AUTHORIZED || next == CAPTURED || next == FAILED;
            case AUTHORIZED:
                return next == CAPTURED || next == FAILED;
            case CAPTURED:
                return next == REFUND_PENDING || next == REFUNDED;
            case REFUND_PENDING:
                return next == REFUNDED || next == FAILED;
            default:
                return false;
        }
    }
}
