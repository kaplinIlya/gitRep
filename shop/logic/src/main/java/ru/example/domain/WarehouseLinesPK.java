package ru.example.domain;

        import javax.persistence.*;
        import java.io.Serializable;
        import java.util.List;

@Embeddable
public class WarehouseLinesPK implements Serializable {
    @ManyToOne
    private Warehouses warehouses;
    @ManyToOne
    private Items items;

    public Warehouses getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(Warehouses warehouses) {
        this.warehouses = warehouses;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }
}
