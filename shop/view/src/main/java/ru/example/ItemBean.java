package ru.example;

import ru.example.domain.Items;
import ru.example.ejb.ItemsManagerBean;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

@Named("itemBean")
@RequestScoped
public class ItemBean implements Serializable {
    private String id;
    private String descr;
    private String unit;
    private int price;

    private String itemFilter = new String();

    @EJB
    private ItemsManagerBean itemsManagerBean;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getItemFilter() {
        return itemFilter;
    }

    public void setItemFilter(String itemFilter) {
        this.itemFilter = itemFilter;
    }

    public ItemsManagerBean getItemsManagerBean() {
        return itemsManagerBean;
    }

    public void setItemsManagerBean(ItemsManagerBean itemsManagerBean) {
        this.itemsManagerBean = itemsManagerBean;
    }

    public void addItem(){

        itemsManagerBean.addItem(id,descr,unit,price);
        id="";
        descr="";
        unit="";
        price=0;
    }

    public List<Items> getItemsByFilter(){

        return itemsManagerBean.getItems(itemFilter);
    }

    public void validateId(FacesContext context, UIComponent component, Object value){
        String id = (String) value;
        Pattern p = Pattern.compile("\\d{2}-\\d{6}");
        if (!p.matcher(id).matches()){
            ((UIInput)component).setValid(false);
            String message = "Формат: хх-хххххх";
            context.addMessage(component.getClientId(context), new FacesMessage(message));
        }
    }
}
