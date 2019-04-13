package ru.example.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class WarehouseLines {
    @EmbeddedId
    private WarehouseLinesPK warehouseLinesPK;
    private int count;
    private String unit;

    public WarehouseLinesPK getWarehouseLinesPK() {
        return warehouseLinesPK;
    }

    public void setWarehouseLinesPK(WarehouseLinesPK warehouseLinesPK) {
        this.warehouseLinesPK = warehouseLinesPK;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
