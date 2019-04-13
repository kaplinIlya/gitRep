package ru.example;

import ru.example.domain.*;
import ru.example.ejb.OrdersManagerBean;
import ru.example.mq.SendMessage;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("orderBean")
@SessionScoped
public class OrderBean implements Serializable {
    @EJB
    private OrdersManagerBean ordersManagerBean;
    private long id;
    private int amount;
    private OrderStatus status;
    private String createDate;
    private Orders selectedOrder;
    private List<OrderLines> orderLines = new ArrayList<>();

    public OrdersManagerBean getOrdersManagerBean() {
        return ordersManagerBean;
    }

    public void setOrdersManagerBean(OrdersManagerBean ordersManagerBean) {
        this.ordersManagerBean = ordersManagerBean;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Orders getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Orders selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public void setOrderLines(List<OrderLines> orderLines) {
        this.orderLines = orderLines;
    }

    public List<OrderLines> getOrderLines() {
        return orderLines;
    }

    public void newOrder(){
       ordersManagerBean.newOrder();
    }

    public void delOrder(long id){

        ordersManagerBean.delOrder(id);
        selectedOrder=null;
        orderLines=null;
    }

    public List<Orders> getOrdersByStatus(){
        return ordersManagerBean.getOrdersByStatus(OrderStatus.NEW);
    }

    public void getOrderLinesByID(){
        if (selectedOrder!=null) {
            orderLines.clear();
            this.orderLines = ordersManagerBean.getOrderLines(selectedOrder.getId());
        }
    }


    public void saveLines(){
        if (orderLines != null && selectedOrder != null){
                ordersManagerBean.refreshOrder(selectedOrder.getId(),orderLines);
        }
     }

    public void addToOrder(Items item){
        if (selectedOrder == null)
            return;
        else
            for (OrderLines line:orderLines){
                if (item.getId().equals(line.getOrderLinesPK().getItems().getId()))
                    return;
            }
        OrderLines newLine = new OrderLines();
        OrderLinesPK newLinePK = new OrderLinesPK();
        newLinePK.setItems(item);
        newLinePK.setOrders(selectedOrder);
        newLine.setOrderLinesPK(newLinePK);
        newLine.setUnit(item.getUnit());
        newLine.setCount(0);
        newLine.setCost(0);
        orderLines.add(newLine);
    }

    public void removeFromOrder(OrderLines line){
        orderLines.remove(line);
    }

    public void sendOrder(Orders order){
        ArrayList<OrderLines> orderLines = ordersManagerBean.getOrderLines(order.getId());
        SendMessage sendMessage = new SendMessage(order,orderLines);
        if (sendMessage.sendOrder()){
            ordersManagerBean.changeStatus(order.getId());
        }
    }

}
