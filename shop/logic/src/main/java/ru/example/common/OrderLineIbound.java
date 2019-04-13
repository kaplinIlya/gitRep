package ru.example.common;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(propOrder = {"itemID","count"})
public class OrderLineIbound implements Serializable {
    private String itemID;
    private int count;

    @XmlElement(name = "itemID",required = true)
    public String getItemID() {
        return itemID;
    }
    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    @XmlElement(name = "count",required = true)
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

}
