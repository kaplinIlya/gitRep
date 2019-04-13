package ru.example.service;

import ru.example.domain.OrderStatus;
import ru.example.domain.Orders;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.sql.Timestamp;

@Named("bean")
@SessionScoped
class Bean implements Serializable {
    public String print(){
        return("this is a bean.class");
    }
}
