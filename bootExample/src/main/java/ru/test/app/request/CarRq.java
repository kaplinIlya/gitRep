package ru.test.app.request;

public class CarRq
{
private String model;
private long power;
private String color;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getPower() {
        return power;
    }

    public void setPower(long power) {
        this.power = power;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "CarRq{" +
                "model='" + model + '\'' +
                ", power=" + power +
                ", color='" + color + '\'' +
                '}';
    }
}
