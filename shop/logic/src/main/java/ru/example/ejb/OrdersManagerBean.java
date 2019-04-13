package ru.example.ejb;

import ru.example.common.OrderInbound;
import ru.example.common.OrderLineIbound;
import ru.example.domain.*;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Stateless
@LocalBean
public class OrdersManagerBean implements Serializable {

    @PersistenceContext(unitName = "persistenceUnit")
    private EntityManager entityManager;


    //новый заказ
    public Orders newOrder(){
        Orders order = new Orders();
        order.setCreateDate(new Timestamp(System.currentTimeMillis()));
        order.setStatus(OrderStatus.NEW);
        entityManager.persist(order);
        return order;
    }

    //Удалить заказ
    public void delOrder(long id){
        Orders order = entityManager.find(Orders.class, id);
        entityManager.remove(order);
    }

    //Выбрать по статусу
    public List<Orders> getOrdersByStatus(OrderStatus status){
        TypedQuery<Orders> query = entityManager.createQuery
                ("select c from Orders c where c.status = :status order by c.id asc", Orders.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    //добавить в заказ
    public boolean addToOrder(long id, String itemId, int count){
        Items item = entityManager.find(Items.class, itemId);
        Orders order = entityManager.find(Orders.class, id);
        if (item==null || order==null)
            return false;
        OrderLinesPK orderLinesPK = new OrderLinesPK();
        orderLinesPK.setItems(item);
        orderLinesPK.setOrders(order);

        OrderLines orderLines = new OrderLines();
        if (entityManager.find(OrderLines.class,orderLinesPK)!= null)
            return false;
        orderLines.setOrderLinesPK(orderLinesPK);
        orderLines.setCount(count);
        orderLines.setUnit(item.getUnit());
        orderLines.setCost(0);
        entityManager.persist(orderLines);
        return true;
    }

    //удалить из заказа
    public Boolean removeFromOrder(OrderLines line){
        if (line==null)
            return false;
        OrderLinesPK orderLinesPK = new OrderLinesPK();
        orderLinesPK.setItems(line.getOrderLinesPK().getItems());
        orderLinesPK.setOrders(line.getOrderLinesPK().getOrders());
        OrderLines orderLine = entityManager.find(OrderLines.class, orderLinesPK);
        if (orderLine == null)
            return false;
        entityManager.remove(orderLine);
        return true;
    }

    //строки заказа
    public ArrayList<OrderLines> getOrderLines(long id){
        Orders order = entityManager.find(Orders.class, id);
        TypedQuery<OrderLines> query = entityManager.createQuery
                ( "select c from OrderLines c where c.orderLinesPK.orders = :order", OrderLines.class);
        query.setParameter("order", order);
        return (new ArrayList(query.getResultList()));
    }

    //обновление заказа
    public void refreshOrder(long id, List<OrderLines> newLines){
        int amount=0;
        Orders order = entityManager.find(Orders.class, id);
        TypedQuery<OrderLines> query = entityManager.createQuery
                ( "select c from OrderLines c where c.orderLinesPK.orders = :order", OrderLines.class);
        query.setParameter("order", order);
        List<OrderLines> oldLines = query.getResultList();

        for (OrderLines line:oldLines) {
            entityManager.remove(line);
        }
        for (OrderLines line:newLines){
            line.setCost(line.getCount()*line.getOrderLinesPK().getItems().getPrice());
            entityManager.persist(line);
            amount += line.getCost();
        }
        order.setAmount(amount);
        entityManager.merge(order);
    }

    //заказ в работу
    public void changeStatus(long orderId){
        Orders order = entityManager.find(Orders.class, orderId);
        order.setStatus(OrderStatus.PROCESS);
        entityManager.persist(order);
    }

    //создание заказа на основе списка товаров
    public long createOrder(OrderInbound orderInbound){
        Orders order = newOrder();
        List<OrderLines> arrLines = new ArrayList<>();
        for (OrderLineIbound line:orderInbound.getOrderLines()){
            Items item = entityManager.find(Items.class,line.getItemID());
            OrderLinesPK orderLinesPK = new OrderLinesPK();
            orderLinesPK.setItems(item);
            orderLinesPK.setOrders(order);
            OrderLines orderLine = new OrderLines();
            orderLine.setOrderLinesPK(orderLinesPK);
            orderLine.setCount(line.getCount());
            orderLine.setUnit(item.getUnit());
            orderLine.setCost(line.getCount() * item.getPrice());
            entityManager.persist(orderLine);
            arrLines.add(orderLine);
        }
        refreshOrder(order.getId(), arrLines);
        return order.getId();
    }
}
