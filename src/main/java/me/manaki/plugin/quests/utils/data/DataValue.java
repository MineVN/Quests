package me.manaki.plugin.quests.utils.data;

public class DataValue {

    private final ValueType type;
    private final String value;

    public DataValue(ValueType type, String value) {
        this.type = type;
        this.value = value;
    }

    public ValueType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean compare(Object... objects) {
        return this.type.compare(this.value, objects);
    }
}
