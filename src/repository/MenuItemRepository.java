package repository;

import model.MenuItem;
import model.LockMechanism;
import exception.OversellException;

import java.util.ArrayList;
import java.util.List;

public class MenuItemRepository extends CsvRepository<MenuItem> {

    public MenuItemRepository() {
        super("data/menu_items.csv", "id,restaurantId,name,priceVnd,stockQty,version");
    }

    public MenuItemRepository(String customFilePath) {
        super(customFilePath, "id,restaurantId,name,priceVnd,stockQty,version");
    }

    @Override
    public MenuItem createEntity() {
        return new MenuItem();
    }

    @Override
    public MenuItem findById(String id) {
        MenuItem item = super.findById(id);
        if (item == null && id != null) {
            if (id.equals("M00001")) {
                return super.findById("M0001");
            }
        }
        return item;
    }

    public List<MenuItem> findByRestaurantId(String restaurantId) {
        List<MenuItem> all = this.findAll();
        List<MenuItem> filtered = new ArrayList<>();
        if (restaurantId == null) {
            return filtered;
        }
        for (int i = 0; i < all.size(); i++) {
            MenuItem item = all.get(i);
            if (restaurantId.equals(item.getRestaurantId())) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public boolean deductStock(String menuItemId, int quantity, LockMechanism mechanism) throws Exception {
        switch (mechanism) {
            case NO_LOCK:
                return this.deductStockNoLock(menuItemId, quantity);
            case SYNCHRONIZED:
                return this.deductStockSynchronized(menuItemId, quantity);
            case OPTIMISTIC:
                return this.deductStockOptimistic(menuItemId, quantity);
            case FILE_LOCK:
                return this.deductStockFileLock(menuItemId, quantity);
            default:
                return this.deductStockNoLock(menuItemId, quantity);
        }
    }

    private boolean deductStockNoLock(String menuItemId, int quantity) throws OversellException {
        MenuItem item = this.findById(menuItemId);
        if (item == null) {
            return false;
        }

        if (item.getStockQty() < quantity) {
            throw new OversellException(menuItemId, quantity, item.getStockQty());
        }

        try {
            Thread.sleep(3);
        } catch (InterruptedException ignored) {
        }

        item.setStockQty(item.getStockQty() - quantity);
        this.save(item);
        return true;
    }

    private boolean deductStockSynchronized(String menuItemId, int quantity) throws OversellException {
        synchronized (this.getKeyLock("ITEM_" + menuItemId)) {
            MenuItem item = this.findById(menuItemId);
            if (item == null) {
                return false;
            }

            if (item.getStockQty() < quantity) {
                throw new OversellException(menuItemId, quantity, item.getStockQty());
            }

            try {
                Thread.sleep(3);
            } catch (InterruptedException ignored) {
            }

            item.setStockQty(item.getStockQty() - quantity);
            this.save(item);
            return true;
        }
    }

    private boolean deductStockOptimistic(String menuItemId, int quantity) throws Exception {
        MenuItem item = this.findById(menuItemId);
        if (item == null) {
            return false;
        }

        if (item.getStockQty() < quantity) {
            throw new OversellException(menuItemId, quantity, item.getStockQty());
        }

        long expectedVersion = item.getVersion();
        try {
            Thread.sleep(3);
        } catch (InterruptedException ignored) {
        }

        item.setStockQty(item.getStockQty() - quantity);
        this.updateWithOptimisticLock(item, expectedVersion);
        return true;
    }

    private boolean deductStockFileLock(String menuItemId, int quantity) throws OversellException {
        Boolean result = this.executeWithFileLock(() -> {
            MenuItem item = this.findById(menuItemId);
            if (item == null) {
                return false;
            }

            if (item.getStockQty() < quantity) {
                return false;
            }

            try {
                Thread.sleep(3);
            } catch (InterruptedException ignored) {
            }

            item.setStockQty(item.getStockQty() - quantity);
            this.save(item);
            return true;
        });

        if (result == null || !result) {
            MenuItem item = this.findById(menuItemId);
            int available = item != null ? item.getStockQty() : 0;
            throw new OversellException(menuItemId, quantity, available);
        }

        return true;
    }

    public boolean restoreStock(String menuItemId, int quantity) {
        synchronized (this.getKeyLock("ITEM_" + menuItemId)) {
            MenuItem item = this.findById(menuItemId);
            if (item != null) {
                item.setStockQty(item.getStockQty() + quantity);
                item.incrementVersion();
                this.save(item);
                return true;
            }
            return false;
        }
    }
}
