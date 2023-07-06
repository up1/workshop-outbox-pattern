package com.example.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Order createNewOrder(@RequestBody Order student) throws Exception {
        return orderService.create(student);
    }

    @GetMapping(value = "/order/{orderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Order getStudent(@PathVariable Integer orderId) throws Exception {
        return orderService.get(orderId);
    }

}
