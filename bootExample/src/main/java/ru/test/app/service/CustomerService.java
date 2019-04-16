package ru.test.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.test.app.entity.Customer;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<Customer> getCustomers(){
        return jdbcTemplate.query("select id, name, age, address, salary from customer",
                (rs, rowNum) -> new Customer(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getDouble(5)));
    }
}
