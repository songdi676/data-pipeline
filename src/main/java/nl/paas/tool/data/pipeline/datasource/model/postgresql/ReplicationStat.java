package nl.paas.tool.data.pipeline.datasource.model.postgresql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("pg_stat_replication")
public class ReplicationStat {
    private Integer pid;
    private String usesysid;
    private String usename;
    private String applicationName;
    private String clientAddr;
    private String clientHostname;
    private Integer clientPort;
    private String backendStart;
    private String backendXmin;
    private String state;
    private String sentLsn;
    private String writeLsn;
    private String flushLsn;
    private String replayLsn;
    @TableField(exist = false)
    private Long sentLsnValue;
    @TableField(exist = false)
    private Long writeLsnValue;
    @TableField(exist = false)
    private Long flushLsnValue;
    @TableField(exist = false)
    private Long replayLsnValue;
    private String writeLag;
    private String flushLag;
    private String replayLag;
    private String syncPriority;
    private String syncState;
    private String replyTime;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getUsesysid() {
        return usesysid;
    }

    public void setUsesysid(String usesysid) {
        this.usesysid = usesysid;
    }

    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getClientAddr() {
        return clientAddr;
    }

    public void setClientAddr(String clientAddr) {
        this.clientAddr = clientAddr;
    }

    public String getClientHostname() {
        return clientHostname;
    }

    public void setClientHostname(String clientHostname) {
        this.clientHostname = clientHostname;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public String getBackendStart() {
        return backendStart;
    }

    public void setBackendStart(String backendStart) {
        this.backendStart = backendStart;
    }

    public String getBackendXmin() {
        return backendXmin;
    }

    public void setBackendXmin(String backendXmin) {
        this.backendXmin = backendXmin;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSentLsn() {
        return sentLsn;
    }

    public void setSentLsn(String sentLsn) {
        this.sentLsn = sentLsn;
    }

    public String getWriteLsn() {
        return writeLsn;
    }

    public void setWriteLsn(String writeLsn) {
        this.writeLsn = writeLsn;
    }

    public String getFlushLsn() {
        return flushLsn;
    }

    public void setFlushLsn(String flushLsn) {
        this.flushLsn = flushLsn;
    }

    public String getReplayLsn() {
        return replayLsn;
    }

    public void setReplayLsn(String replayLsn) {
        this.replayLsn = replayLsn;
    }

    public String getWriteLag() {
        return writeLag;
    }

    public void setWriteLag(String writeLag) {
        this.writeLag = writeLag;
    }

    public String getFlushLag() {
        return flushLag;
    }

    public void setFlushLag(String flushLag) {
        this.flushLag = flushLag;
    }

    public String getReplayLag() {
        return replayLag;
    }

    public void setReplayLag(String replayLag) {
        this.replayLag = replayLag;
    }

    public String getSyncPriority() {
        return syncPriority;
    }

    public void setSyncPriority(String syncPriority) {
        this.syncPriority = syncPriority;
    }

    public String getSyncState() {
        return syncState;
    }

    public void setSyncState(String syncState) {
        this.syncState = syncState;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public Long getSentLsnValue() {
        return sentLsnValue;
    }

    public void setSentLsnValue(Long sentLsnValue) {
        this.sentLsnValue = sentLsnValue;
    }

    public Long getWriteLsnValue() {
        return writeLsnValue;
    }

    public void setWriteLsnValue(Long writeLsnValue) {
        this.writeLsnValue = writeLsnValue;
    }

    public Long getFlushLsnValue() {
        return flushLsnValue;
    }

    public void setFlushLsnValue(Long flushLsnValue) {
        this.flushLsnValue = flushLsnValue;
    }

    public Long getReplayLsnValue() {
        return replayLsnValue;
    }

    public void setReplayLsnValue(Long replayLsnValue) {
        this.replayLsnValue = replayLsnValue;
    }
}
