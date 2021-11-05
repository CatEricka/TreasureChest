package com.mtihc.minecraft.treasurechest.v8.compatible.mmoitems;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class MMOItemWrapper implements ConfigurationSerializable {
    private String type, id;
    private int amount;

    MMOItemWrapper(String type, String id, int amount) {
        this.type = type;
        this.id = id;
        this.amount = amount;
    }

    /**
     * Deserialization constructor.
     *
     * @param values the serialized values
     */
    public MMOItemWrapper(Map<String, Object> values) {
        type = (String) values.get("type");
        id = (String) values.get("id");
        amount = (int) values.get("amount");
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> values = new LinkedHashMap<String, Object>();
        values.put("type", type);
        values.put("id", id);
        values.put("amount", amount);

        return values;
    }
}
