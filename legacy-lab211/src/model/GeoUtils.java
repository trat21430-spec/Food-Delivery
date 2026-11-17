package model;

/**
 * Utility class calculating real-world geographic distance using the Haversine formula.
 * Adheres strictly to OOP requirements (instance members, non-final class).
 */
public class GeoUtils {
    private double earthRadiusKm;

    public GeoUtils() {
        this.earthRadiusKm = 6371.0;
    }

    public GeoUtils(double earthRadiusKm) {
        this.earthRadiusKm = earthRadiusKm;
    }

    public double getEarthRadiusKm() {
        return this.earthRadiusKm;
    }

    public void setEarthRadiusKm(double earthRadiusKm) {
        this.earthRadiusKm = earthRadiusKm;
    }

    /**
     * Calculates great-circle distance between two GPS coordinates in kilometers.
     */
    public double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2.0), 2)
                 + Math.cos(rLat1) * Math.cos(rLat2) * Math.pow(Math.sin(dLon / 2.0), 2);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return Math.round(this.earthRadiusKm * c * 100.0) / 100.0;
    }

    /**
     * Calculates delivery fee (VND) based on distance (Haversine km):
     * Base fee 15,000 VND for first 2km, +5,000 VND per additional km.
     */
    public double calculateDeliveryFee(double distanceKm) {
        if (distanceKm <= 2.0) {
            return 15000.0;
        }
        return Math.round((15000.0 + (distanceKm - 2.0) * 5000.0) / 1000.0) * 1000.0;
    }

    public double haversine(double lat1, double lon1, double lat2, double lon2) {
        return this.haversineDistance(lat1, lon1, lat2, lon2);
    }

    public double getFee(double distanceKm) {
        return this.calculateDeliveryFee(distanceKm);
    }

    /**
     * Calculates estimated pickup time in minutes for Quy Nhon city area.
     * Speed in urban area is ~25 km/h (2.5 min/km) + 5 mins food prep time.
     */
    public int calculatePickupMinutes(double driverToRestKm) {
        int travelMin = (int) Math.round(driverToRestKm * 2.5);
        int prepMin = 5;
        return Math.max(5, travelMin + prepMin);
    }

    /**
     * Calculates estimated total delivery time in minutes for Quy Nhon city area.
     * Total = pickup time + travel from restaurant to customer's address.
     */
    public int calculateDeliveryMinutes(double driverToRestKm, double restToCustKm) {
        int pickupMin = this.calculatePickupMinutes(driverToRestKm);
        int deliveryTravelMin = Math.max(4, (int) Math.round(restToCustKm * 2.5));
        return pickupMin + deliveryTravelMin;
    }

    /**
     * Formats an estimated clock time (HH:mm) based on order creation epoch and duration in minutes.
     */
    public String formatEstimatedClockTime(long baseEpochMs, int durationMinutes) {
        if (baseEpochMs <= 0) {
            baseEpochMs = System.currentTimeMillis();
        }
        long targetMs = baseEpochMs + (durationMinutes * 60L * 1000L);
        java.time.Instant instant = java.time.Instant.ofEpochMilli(targetMs);
        java.time.ZonedDateTime zdt = instant.atZone(java.time.ZoneId.systemDefault());
        return String.format("%02d:%02d", zdt.getHour(), zdt.getMinute());
    }

    /**
     * Formats an estimated clock time (HH:mm) based on LocalDateTime and duration in minutes.
     */
    public String formatEstimatedClockTime(java.time.LocalDateTime baseTime, int durationMinutes) {
        if (baseTime == null) {
            baseTime = java.time.LocalDateTime.now();
        }
        java.time.LocalDateTime target = baseTime.plusMinutes(durationMinutes);
        return String.format("%02d:%02d", target.getHour(), target.getMinute());
    }
}
