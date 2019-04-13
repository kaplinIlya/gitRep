package ru.example.ejb;

import ru.example.domain.Items;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class ItemsManagerBean implements Serializable {

    @PersistenceContext(unitName = "persistenceUnit")
    private EntityManager entityManager;

    public Items addItem(String id, String descr, String unit, int price){
        Items item = new Items();
        item.setId(id);
        item.setDescr(descr);
        item.setUnit(unit);
        item.setPrice(price);
        entityManager.persist(item);
        return item;
    }

    public Boolean delItem(String id) {
        Items item = entityManager.find(Items.class, id);
        if (item != null) {
            entityManager.remove(item);
            return true;
        } else {
            return false;
        }
    }

    public List<Items> getItems(String filter){
        TypedQuery<Items> query = entityManager.createQuery
                ("select c from Items c where lower(c.descr) like lower(:filter)", Items.class);
        query.setParameter("filter", "%"+filter+"%");
        return query.getResultList();
    }
}
