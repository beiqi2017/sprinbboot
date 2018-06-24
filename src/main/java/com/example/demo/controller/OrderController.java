package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.example.demo.dao.OrderDao;
import com.example.demo.domain.Order;

/**
 * Created by wuwf on 17/4/19.
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderDao orderRepository;
    
    @Autowired
    private IdGenerator idGenerator;

    @RequestMapping("/add")
    public Object add() {
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setOrder_id((long) i);
            order.setUser_id((long) i);
            order.setId(idGenerator.generateId().longValue());
            orderRepository.add(order);
        }
        for (int i = 10; i < 20; i++) {
            Order order = new Order();
            order.setUser_id((long) i + 1);
            order.setOrder_id((long) i);
            order.setId(idGenerator.generateId().longValue());
            orderRepository.add(order);
        }
        return "success";
    }

    @RequestMapping("query")
    private Object queryAll() {
        return orderRepository.get();
    }
}
