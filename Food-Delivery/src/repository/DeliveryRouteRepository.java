package repository;

import model.DeliveryRoute;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository for DeliveryRoute entities backed by delivery_routes.csv.
 */
public class DeliveryRouteRepository extends CsvRepository<DeliveryRoute> {

    @Override
    protected String getCsvFilePath() {
        return "data/delivery_routes.csv";
    }

    @Override
    protected String getCsvHeader() {
        return "id,orderId,driverId,distanceKm,estimatedMinutes,routePathJson,createdAt,updatedAt,version";
    }

    @Override
    protected DeliveryRoute createEmpty() {
        return new DeliveryRoute();
    }

    /**
     * Find all delivery routes associated with a specific order.
     */
    public List<DeliveryRoute> findByOrderId(String orderId) {
        return findAll().stream()
                .filter(r -> orderId.equals(r.getOrderId()))
                .collect(Collectors.toList());
    }

    /**
     * Find all delivery routes handled by a specific driver.
     */
    public List<DeliveryRoute> findByDriverId(String driverId) {
        return findAll().stream()
                .filter(r -> driverId.equals(r.getDriverId()))
                .collect(Collectors.toList());
    }
}
