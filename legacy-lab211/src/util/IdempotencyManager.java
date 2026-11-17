package util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Idempotency Key Manager (Trang 11 Master Plan).
 * Prevents duplicate order creation, payment charging, and driver assignment retries.
 * Cached in-memory with automatic 24-hour expiration check.
 */
public class IdempotencyManager {
    public static class CachedResponse {
        private int statusCode;
        private String responseBody;
        private long createdAtMs;

        public CachedResponse(int statusCode, String responseBody) {
            this.statusCode = statusCode;
            this.responseBody = responseBody;
            this.createdAtMs = System.currentTimeMillis();
        }

        public int getStatusCode() { return this.statusCode; }
        public String getResponseBody() { return this.responseBody; }
        public boolean isExpired() {
            return (System.currentTimeMillis() - this.createdAtMs) > (24 * 60 * 60 * 1000L); // 24 hours
        }
    }

    private Map<String, CachedResponse> keyStore;

    public IdempotencyManager() {
        this.keyStore = new ConcurrentHashMap<String, CachedResponse>();
    }

    public boolean hasKey(String key) {
        if (key == null || key.trim().isEmpty()) return false;
        CachedResponse cached = this.keyStore.get(key);
        if (cached == null) return false;
        if (cached.isExpired()) {
            this.keyStore.remove(key);
            return false;
        }
        return true;
    }

    public CachedResponse getCachedResponse(String key) {
        if (key == null) return null;
        CachedResponse cached = this.keyStore.get(key);
        if (cached != null && cached.isExpired()) {
            this.keyStore.remove(key);
            return null;
        }
        return cached;
    }

    public void recordResponse(String key, int statusCode, String responseBody) {
        if (key != null && !key.trim().isEmpty()) {
            this.keyStore.put(key, new CachedResponse(statusCode, responseBody));
        }
    }
}
