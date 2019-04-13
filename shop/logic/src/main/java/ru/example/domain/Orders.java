package ru.example.domain;

import javax.mail.FetchProfile;
import javax.persistence.*;
import javax.persistence.criteria.Order;
import java.text.SimpleDateFormat;
import java.time.temporal.TemporalAccessor;
import java.util.Date;


@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int amount;
    @Column(columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    private OrderStatus status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Enumerated(EnumType.ORDINAL)
    public OrderStatus getStatus() {return status; }

    @Enumerated(EnumType.STRING)
    public void setStatus( OrderStatus status) {this.status = status; }

    public String getCreateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(createDate);
    }

    public void setCreateDate(Date createDate) { this.createDate = createDate; }
}
