package nl.paas.tool.data.pipeline.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectorStateInfo {
    private String name;
    private ConnectorState connector;
    private List<TaskState> tasks;
    private ConnectorType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConnectorState getConnector() {
        return connector;
    }

    public void setConnector(ConnectorState connector) {
        this.connector = connector;
    }

    public List<TaskState> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskState> tasks) {
        this.tasks = tasks;
    }

    public ConnectorType getType() {
        return type;
    }

    public void setType(ConnectorType type) {
        this.type = type;
    }

    public abstract static class AbstractState {
        private final String state;
        private final String trace;
        private final String workerId;

        public AbstractState(String state, String workerId, String trace) {
            this.state = state;
            this.workerId = workerId;
            this.trace = trace;
        }

        @JsonProperty
        public String state() {
            return state;
        }

        @JsonProperty("worker_id")
        public String workerId() {
            return workerId;
        }

        @JsonProperty
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public String trace() {
            return trace;
        }
    }

    public static class ConnectorState extends AbstractState {
        @JsonCreator
        public ConnectorState(@JsonProperty("state") String state, @JsonProperty("worker_id") String worker,
            @JsonProperty("msg") String msg) {
            super(state, worker, msg);
        }
    }

    public static class TaskState extends AbstractState implements Comparable<TaskState> {
        private final int id;

        @JsonCreator
        public TaskState(@JsonProperty("id") int id, @JsonProperty("state") String state,
            @JsonProperty("worker_id") String worker, @JsonProperty("msg") String msg) {
            super(state, worker, msg);
            this.id = id;
        }

        @JsonProperty
        public int id() {
            return id;
        }

        @Override
        public int compareTo(TaskState that) {
            return Integer.compare(this.id, that.id);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof TaskState))
                return false;
            TaskState other = (TaskState)o;
            return compareTo(other) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
