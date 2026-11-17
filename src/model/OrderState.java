package model;

/**
 * Master Plan 4-State Machine: ORDER STATE MACHINE (Trang 4, Mục 2.3)
 * Manages the order lifecycle from creation, merchant preparation to completion.
 * Strictly decoupled from Payment, Delivery, and Settlement states.
 */
public enum OrderState {
    PENDING("Chờ nhà hàng tiếp nhận"),
    CONFIRMED("Nhà hàng đã xác nhận"),
    PREPARING("Bếp đang chuẩn bị món"),
    READY_FOR_PICKUP("Món đã nấu xong, chờ tài xế lấy"),
    COMPLETED("Đơn hàng đã hoàn tất"),
    CANCELLED("Đơn hàng đã bị hủy");

    private String description;

    OrderState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean canTransitionTo(OrderState next) {
        if (this == CANCELLED || this == COMPLETED) {
            return false;
        }
        if (next == CANCELLED) {
            return this == PENDING || this == CONFIRMED;
        }
        switch (this) {
            case PENDING:
                return next == CONFIRMED;
            case CONFIRMED:
                return next == PREPARING;
            case PREPARING:
                return next == READY_FOR_PICKUP;
            case READY_FOR_PICKUP:
                return next == COMPLETED;
            default:
                return false;
        }
    }
}
