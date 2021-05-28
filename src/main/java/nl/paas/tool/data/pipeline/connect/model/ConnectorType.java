package nl.paas.tool.data.pipeline.connect.model;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ConnectorType {
    SOURCE, SINK, UNKNOWN;

    @Override
    @JsonValue
    public String toString() {
        return super.toString().toLowerCase(Locale.ROOT);
    }

    @JsonCreator
    public static ConnectorType forValue(String value) {
        return ConnectorType.valueOf(value.toUpperCase(Locale.ROOT));
    }
}
