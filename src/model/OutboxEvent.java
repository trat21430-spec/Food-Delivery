package model;

import java.time.LocalDateTime;

/**
 * Transactional Outbox Event (Trang 12 Master Plan).
 * Persisted in the same database transaction as business operations,
 * ensuring zero event loss even during sudden service restarts or network partitions.
 * Adheres strictly to LAB211 rules: pure OOP, 100% this., no static outside main, no final, no protected.
 */
public class OutboxEvent extends BaseEntity {
    private String aggregateType;
    private String aggregateId;
    private String eventType;
    private String payloadJson;
    private String eventCreatedAt;
    private String publishedAt;
    private String status;

    public OutboxEvent() {
        super();
        this.aggregateType = "";
        this.aggregateId = "";
        this.eventType = "";
        this.payloadJson = "{}";
        this.eventCreatedAt = LocalDateTime.now().toString();
        this.publishedAt = "";
        this.status = "PENDING";
    }

    public OutboxEvent(String id, String aggregateType, String aggregateId,
                       String eventType, String payloadJson) {
        super(id);
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payloadJson = payloadJson != null ? payloadJson : "{}";
        this.eventCreatedAt = LocalDateTime.now().toString();
        this.publishedAt = "";
        this.status = "PENDING";
    }

    public String getAggregateType() { return this.aggregateType; }
    public void setAggregateType(String aggregateType) { this.aggregateType = aggregateType; }

    public String getAggregateId() { return this.aggregateId; }
    public void setAggregateId(String aggregateId) { this.aggregateId = aggregateId; }

    public String getEventType() { return this.eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getPayloadJson() { return this.payloadJson; }
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }

    public String getEventCreatedAt() { return this.eventCreatedAt; }
    public void setEventCreatedAt(String eventCreatedAt) { this.eventCreatedAt = eventCreatedAt; }

    public String getPublishedAt() { return this.publishedAt; }
    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }

    private String encodeField(String text) {
        if (text == null) return "";
        return text.replace(";", "%3B").replace(",", "%2C").replace("\n", "%0A");
    }

    private String decodeField(String text) {
        if (text == null) return "";
        return text.replace("%3B", ";").replace("%2C", ",").replace("%0A", "\n");
    }

    @Override
    public String toCsvLine() {
        return String.join(";",
                this.encodeField(this.getId()),
                this.encodeField(this.aggregateType),
                this.encodeField(this.aggregateId),
                this.encodeField(this.eventType),
                this.encodeField(this.payloadJson),
                this.encodeField(this.eventCreatedAt),
                this.encodeField(this.publishedAt),
                this.encodeField(this.status),
                String.valueOf(this.getVersion()));
    }

    @Override
    public void fromCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) return;
        String[] p = line.split(";", -1);
        if (p.length >= 8) {
            this.setId(this.decodeField(p[0].trim()));
            this.aggregateType = this.decodeField(p[1].trim());
            this.aggregateId = this.decodeField(p[2].trim());
            this.eventType = this.decodeField(p[3].trim());
            this.payloadJson = this.decodeField(p[4].trim());
            this.eventCreatedAt = this.decodeField(p[5].trim());
            this.publishedAt = this.decodeField(p[6].trim());
            this.status = this.decodeField(p[7].trim());
            if (p.length > 8) {
                try { this.setVersion(Long.parseLong(p[8].trim())); } catch (Exception ignored) {}
            }
        }
    }
}
