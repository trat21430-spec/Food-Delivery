
package repository;

import model.Driver;

public class DriverRepository extends CsvRepository<Driver> {

    public DriverRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected Driver fromCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",", -1);

        if (parts.length < 6) {
            return null;
        }

        try {
            Driver driver = new Driver();

            driver.setId(parts[0].trim());
            driver.setName(parts[1].trim());
            driver.setPhone(parts[2].trim());
            driver.setLatitude(Double.parseDouble(parts[3].trim()));
            driver.setLongitude(Double.parseDouble(parts[4].trim()));
            driver.setStatus(parts[5].trim());

            return driver;

        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected String toCsvLine(Driver driver) {
        return String.join(",",
                driver.getId(),
                driver.getName(),
                driver.getPhone(),
                String.valueOf(driver.getLatitude()),
                String.valueOf(driver.getLongitude()),
                driver.getStatus()
        );
    }

    @Override
    protected String getHeader() {
        return "id,name,phone,latitude,longitude,status";
    }
}

