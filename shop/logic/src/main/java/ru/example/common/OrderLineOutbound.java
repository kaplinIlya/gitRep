package ru.example.common;


import javax.xml.bind.annotation.*;

@XmlRootElement(name="orderLines")
@XmlType(propOrder = {"itemID","itemDescr","count","unit","cost"})
public class OrderLineOutbound {
    private String itemID;
    private String itemDescr;
    private int count;
    private String unit;
    private int cost;

    @XmlElement(name = "itemID")
    public String getItemID() {
        return itemID;
    }
    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    @XmlElement(name = "item")
    public String getItemDescr() {
        return itemDescr;
    }
    public void setItemDescr(String itemDescr) {
        this.itemDescr = itemDescr;
    }

    @XmlElement(name = "count")
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    @XmlElement(name = "unit")
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    @XmlElement(name = "cost")
    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
}
