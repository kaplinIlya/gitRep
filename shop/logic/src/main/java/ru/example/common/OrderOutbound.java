package ru.example.common;

import ru.example.domain.OrderStatus;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

@XmlRootElement(name="OrderOutbound")
@XmlType(propOrder = {"id","createDate","amount","status","orderLines"})
public class OrderOutbound {

    private long id;
    private int amount;
    private String createDate;
    private OrderStatus status;
    private ArrayList<OrderLineOutbound> orderLines;

    @XmlElement(name="ID")
    public long getId(){
        return this.id;
    }
    public void setId(long id){
        this.id=id;
    }

    @XmlElement(name = "createDate")
    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @XmlElement(name = "amount")
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @XmlElement(name = "status")
    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @XmlElement(name = "orderLines")
    @XmlElementWrapper
    public ArrayList<OrderLineOutbound> getOrderLines() {
        return orderLines;
    }
    public void setOrderLines(ArrayList<OrderLineOutbound> orderLines) {
        this.orderLines = orderLines;
    }
}
