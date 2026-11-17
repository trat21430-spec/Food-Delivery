package repository;

import model.DeliveryRoute;

public class DeliveryRouteRepository extends CsvRepository<DeliveryRoute> {

    public DeliveryRouteRepository() {
        super("data/delivery_routes.csv", "id,orderId,driverId,pickupDistanceKm,deliveryDistanceKm,estimatedMinutes");
    }

    public DeliveryRouteRepository(String filePath) {
        super(filePath, "id,orderId,driverId,pickupDistanceKm,deliveryDistanceKm,estimatedMinutes");
    }

    @Override
    public DeliveryRoute createEntity() {
        return new DeliveryRoute();
    }
}
