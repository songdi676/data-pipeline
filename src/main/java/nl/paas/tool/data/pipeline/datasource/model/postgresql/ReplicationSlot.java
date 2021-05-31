package nl.paas.tool.data.pipeline.datasource.model.postgresql;

import io.debezium.connector.postgresql.connection.Lsn;
import io.debezium.connector.postgresql.connection.ServerInfo;
import io.debezium.connector.postgresql.spi.SlotState;

public class ReplicationSlot {
    public static final ReplicationSlot INVALID = new ReplicationSlot(false, null, null, null);

    private boolean active;
    private Lsn latestFlushedLsn;
    private Lsn restartLsn;
    private Long catalogXmin;

    public ReplicationSlot(boolean active, Lsn latestFlushedLsn, Lsn restartLsn, Long catalogXmin) {
        this.active = active;
        this.latestFlushedLsn = latestFlushedLsn;
        this.restartLsn = restartLsn;
        this.catalogXmin = catalogXmin;
    }

    protected boolean active() {
        return active;
    }

    /**
     * Represents the `confirmed_flushed_lsn` field of the replication slot.
     * <p>
     * This value represents the latest LSN that the logical replication
     * consumer has reported back to postgres.
     *
     * @return the latestFlushedLsn
     */
    protected Lsn latestFlushedLsn() {
        return latestFlushedLsn;
    }

    /**
     * Represents the `restart_lsn` field of the replication slot.
     * <p>
     * The restart_lsn will be the LSN the slot restarts from
     * in the event of the disconnect. This can be distinct from
     * the `confirmed_flushed_lsn` as the two pointers are moved
     * independently
     *
     * @return the restartLsn
     */
    protected Lsn restartLsn() {
        return restartLsn;
    }

    protected Long catalogXmin() {
        return catalogXmin;
    }

    protected boolean hasValidFlushedLsn() {
        return latestFlushedLsn != null;
    }

    protected SlotState asSlotState() {
        return new SlotState(latestFlushedLsn, restartLsn, catalogXmin, active);
    }

    @Override
    public String toString() {
        return "ReplicationSlot [active=" + active + ", latestFlushedLsn=" + latestFlushedLsn + ", catalogXmin="
            + catalogXmin + "]";
    }
}
