package repository;

<<<<<<< HEAD
import model.MenuItem;
import model.LockMechanism;
import exception.OversellException;
=======
import exception.OversellException;
import model.LockMechanism;
import model.MenuItem;
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9

import java.util.ArrayList;
import java.util.List;

public class MenuItemRepository extends CsvRepository<MenuItem> {

    public MenuItemRepository() {
        super("data/menu_items.csv", "id,restaurantId,name,price,stockQty,createdAt,updatedAt,version");
    }

    public MenuItemRepository(String customFilePath) {
        super(customFilePath, "id,restaurantId,name,price,stockQty,createdAt,updatedAt,version");
    }

    @Override
    public MenuItem createEntity() {
        return new MenuItem();
    }

    public List<MenuItem> findByRestaurantId(String restaurantId) {
        List<MenuItem> all = this.findAll();
        List<MenuItem> filtered = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            MenuItem item = all.get(i);
            if (item.getRestaurantId().equals(restaurantId)) {
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
<<<<<<< HEAD
        } catch (InterruptedException ignored) {
=======
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Stock deduction was interrupted", e);
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
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
<<<<<<< HEAD
            } catch (InterruptedException ignored) {
=======
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Stock deduction was interrupted", e);
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
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
<<<<<<< HEAD
        } catch (InterruptedException ignored) {
=======
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Stock deduction was interrupted", e);
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
        }

        item.setStockQty(item.getStockQty() - quantity);
        this.updateWithOptimisticLock(item, expectedVersion);
        return true;
    }

    private boolean deductStockFileLock(String menuItemId, int quantity) throws OversellException {
        Boolean result = this.executeWithFileLock(() -> {
            MenuItem item = this.findById(menuItemId);
            if (item == null) {
<<<<<<< HEAD
                return false;
=======
                return null;
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
            }

            if (item.getStockQty() < quantity) {
                return false;
            }

            try {
                Thread.sleep(3);
<<<<<<< HEAD
            } catch (InterruptedException ignored) {
=======
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Stock deduction was interrupted", e);
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
            }

            item.setStockQty(item.getStockQty() - quantity);
            this.save(item);
            return true;
        });

<<<<<<< HEAD
        if (result == null || !result) {
=======
        if (result == null) {
            MenuItem item = this.findById(menuItemId);
            if (item == null) {
                return false;
            }
            throw new OversellException(menuItemId, quantity, item.getStockQty());
        }

        if (!result) {
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
            MenuItem item = this.findById(menuItemId);
            int available = item != null ? item.getStockQty() : 0;
            throw new OversellException(menuItemId, quantity, available);
        }

        return true;
    }
}
