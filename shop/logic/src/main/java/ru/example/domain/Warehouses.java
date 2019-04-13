package ru.example.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Warehouses {
    @Id
    private String Id;
    private String descr;
    private String address;
    @OneToMany
    private List<WarehouseLines> warehouseLines;

    public List<WarehouseLines> getWarehouseLines() {
        return warehouseLines;
    }

    public void setWarehouseLines(List<WarehouseLines> warehouseLines) {
        this.warehouseLines = warehouseLines;
    }

    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }
    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
