package repository;

import model.DeliveryRoute;

public class DeliveryRouteRepository extends CsvRepository<DeliveryRoute> {

    public DeliveryRouteRepository() {
        super("data/delivery_routes.csv", "id,orderId,driverId,distanceKm,estimatedMinutes,routePathJson,createdAt,updatedAt,version");
    }

    public DeliveryRouteRepository(String customFilePath) {
        super(customFilePath, "id,orderId,driverId,distanceKm,estimatedMinutes,routePathJson,createdAt,updatedAt,version");
    }

    @Override
    public DeliveryRoute createEntity() {
        return new DeliveryRoute();
    }
}
