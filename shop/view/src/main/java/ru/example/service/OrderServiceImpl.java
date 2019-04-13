package ru.example.service;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import ru.example.common.OrderInbound;
import ru.example.ejb.*;

import java.io.Serializable;

@Stateless
@WebService(name = "orderService")
public class OrderServiceImpl implements OrderService, Serializable {

    @EJB
    private OrdersManagerBean ordersManagerBean;

    @Inject
    private Bean bean;

    public Bean getBean() {
        return bean;
    }

    public void setBean(Bean bean) {
        this.bean = bean;
    }

    @WebMethod
    public long createOrder(OrderInbound orderInbound){
        if (orderInbound.getOrderLines().size()==0)
            return -1;
        return ordersManagerBean.createOrder(orderInbound);
    }

    @WebMethod
    public String testBean() {
        return bean.print();
    }
}

