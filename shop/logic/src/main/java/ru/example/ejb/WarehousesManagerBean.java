package ru.example.ejb;

import ru.example.domain.Items;
import ru.example.domain.WarehouseLines;
import ru.example.domain.WarehouseLinesPK;
import ru.example.domain.Warehouses;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Stateless
@LocalBean
public class WarehousesManagerBean implements Serializable {

    @PersistenceContext(unitName = "persistenceUnit")
    private EntityManager entityManager;

    //новый склад
    public Warehouses newWarehouse(String id, String descr, String address){
        Warehouses warehouse = new Warehouses();
        warehouse.setDescr(descr);
        warehouse.setAddress(address);
        entityManager.persist(warehouse);
        return warehouse;
    }
    //приемка товаров
    public Boolean addItem(String id, String itemId, int count, String unit){
        Items item = entityManager.find(Items.class, itemId);
        Warehouses warehouse = entityManager.find(Warehouses.class, id);

        if (item == null || warehouse == null)
                return false;

        WarehouseLinesPK warehouseLinesPK = new WarehouseLinesPK();
        warehouseLinesPK.setItems(item);
        warehouseLinesPK.setWarehouses(warehouse);

        WarehouseLines warehouseLines = entityManager.find(WarehouseLines.class, warehouseLinesPK);
        if (warehouseLines==null){
            WarehouseLines newLine = new WarehouseLines();
            newLine.setWarehouseLinesPK(warehouseLinesPK);
            newLine.setCount(count);
            newLine.setUnit(unit);
            entityManager.persist(newLine);
            return true;
        }
        else {
            warehouseLines.setCount(warehouseLines.getCount() + count);
            warehouseLines.setUnit(unit);
            entityManager.persist(warehouseLines);
            return true;
        }
    }

    //отгрузка товаров
    public Boolean shipItem (String id, String itemId, int count, String unit){
        Items item = entityManager.find(Items.class, itemId);
        Warehouses warehouse = entityManager.find(Warehouses.class, id);

        if (item == null || warehouse == null)
            return false;

        WarehouseLinesPK warehouseLinesPK = new WarehouseLinesPK();
        warehouseLinesPK.setItems(item);
        warehouseLinesPK.setWarehouses(warehouse);
        WarehouseLines warehouseLines = entityManager.find(WarehouseLines.class, warehouseLinesPK);
        if (warehouseLines!=null) {
            if (warehouseLines.getCount()<count)
                return false;
            warehouseLines.setCount(warehouseLines.getCount()-count);
            entityManager.persist(warehouseLines);
            return true;
        }
        return false;
    }
}
