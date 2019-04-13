package com.model;

import com.model.enums.Colors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name ="CARS")
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firm;

    private String model;

    private int power;

    @Enumerated(EnumType.ORDINAL)
    private Colors color;

    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", company:'" + firm + '\'' +
                ", model:'" + model + '\'' +
                ", power:" + power +
                ", color:" + color +
                '}';
    }
}
