package repository;

import model.OutboxEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Transactional Outbox Repository (Trang 12 Master Plan).
 * Adheres strictly to LAB211 rules: pure OOP, 100% this., no static outside main, no final, no protected.
 */
public class OutboxRepository extends CsvRepository<OutboxEvent> {

    public OutboxRepository() {
        super("data/outbox_events.csv", "id;aggregateType;aggregateId;eventType;payloadJson;createdAt;publishedAt;status;version");
    }

    public OutboxRepository(String customPath) {
        super(customPath, "id;aggregateType;aggregateId;eventType;payloadJson;createdAt;publishedAt;status;version");
    }

    @Override
    public OutboxEvent createEntity() {
        return new OutboxEvent();
    }

    public synchronized OutboxEvent enqueueEvent(String aggType, String aggId, String evtType, String payloadJson) {
        String id = "EVT_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 9000 + 1000);
        OutboxEvent evt = new OutboxEvent(id, aggType, aggId, evtType, payloadJson);
        this.save(evt);
        return evt;
    }

    public synchronized List<OutboxEvent> getPendingEvents() {
        List<OutboxEvent> res = new ArrayList<OutboxEvent>();
        List<OutboxEvent> all = this.findAll();
        for (int i = 0; i < all.size(); i++) {
            OutboxEvent e = all.get(i);
            if ("PENDING".equalsIgnoreCase(e.getStatus())) {
                res.add(e);
            }
        }
        return res;
    }

    public synchronized void markPublished(String eventId) {
        OutboxEvent evt = this.findById(eventId);
        if (evt != null) {
            evt.setStatus("PUBLISHED");
            evt.setPublishedAt(LocalDateTime.now().toString());
            this.save(evt);
        }
    }
}
