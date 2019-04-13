package ru.example.common;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

public class OrderInbound implements Serializable {

    private ArrayList<OrderLineIbound> orderLines;

    public ArrayList<OrderLineIbound> getOrderLines() {
        return orderLines;
    }
    public void setOrderLines(ArrayList<OrderLineIbound> orderLines) {
        this.orderLines = orderLines;
    }
}
