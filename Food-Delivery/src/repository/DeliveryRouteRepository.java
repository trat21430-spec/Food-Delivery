/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author HP
 */

import model.DeliveryRoute;

public class DeliveryRouteRepository extends CsvRepository<DeliveryRoute> {

    public DeliveryRouteRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected DeliveryRoute fromCsvLine(String line) {
        String[] parts = line.split(",");
        DeliveryRoute route = new DeliveryRoute();
        route.setId(parts[0].trim());
        route.setOrderId(parts[1].trim());
        route.setDriverId(parts[2].trim());
        route.setDistanceKm(Double.parseDouble(parts[3].trim()));
        route.setEstimatedMinutes(Integer.parseInt(parts[4].trim()));
        route.setStatus(parts[5].trim());
        return route;
    }

    @Override
    protected String toCsvLine(DeliveryRoute route) {
        return String.join(",", route.getId(), route.getOrderId(), route.getDriverId(),
                String.valueOf(route.getDistanceKm()), String.valueOf(route.getEstimatedMinutes()),
                route.getStatus());
    }

    @Override
    protected String getHeader() {
        return "id,orderId,driverId,distanceKm,estimatedMinutes,status";
    }
}