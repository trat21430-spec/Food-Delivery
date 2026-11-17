package model;

/**
 * Master Plan 4-State Machine: DELIVERY STATE MACHINE (Trang 4, Mục 2.3 & Trang 13)
 * Tracks the physical delivery task independently from the food preparation state.
 */
public enum DeliveryState {
    CREATED("Đã tạo nhiệm vụ giao hàng"),
    SEARCHING_DRIVER("Đang tìm và phát offer cho tài xế"),
    ASSIGNED("Tài xế đã nhận đơn, đang di chuyển đến quán"),
    ARRIVED_AT_MERCHANT("Tài xế đã đến quán ăn"),
    PICKED_UP("Tài xế đã lấy món, đang giao đến khách"),
    DELIVERED("Giao hàng thành công đến khách"),
    FAILED("Giao hàng thất bại (khách không nhận/sự cố)");

    private String description;

    DeliveryState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean canTransitionTo(DeliveryState next) {
        if (this == DELIVERED) {
            return false;
        }
        if (next == FAILED) {
            return this != DELIVERED;
        }
        switch (this) {
            case CREATED:
                return next == SEARCHING_DRIVER;
            case SEARCHING_DRIVER:
                return next == ASSIGNED || next == FAILED;
            case ASSIGNED:
                return next == ARRIVED_AT_MERCHANT || next == SEARCHING_DRIVER;
            case ARRIVED_AT_MERCHANT:
                return next == PICKED_UP;
            case PICKED_UP:
                return next == DELIVERED || next == FAILED;
            default:
                return false;
        }
    }
}
