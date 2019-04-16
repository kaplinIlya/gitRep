package ru.test.app.entity;

import javax.persistence.*;

@Entity
@Table(name ="CUSTOMER")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "AGE")
    private Integer age;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "SALARY")
    private Double salary;
}
