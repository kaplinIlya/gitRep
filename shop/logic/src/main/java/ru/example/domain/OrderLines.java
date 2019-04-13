package ru.example.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class OrderLines implements Serializable {

    @EmbeddedId
    private OrderLinesPK orderLinesPK;
    private int count;
    private String unit;
    private int cost;

    public OrderLinesPK getOrderLinesPK() {
        return orderLinesPK;
    }

    public void setOrderLinesPK(OrderLinesPK orderLinesPK) {
        this.orderLinesPK = orderLinesPK;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
