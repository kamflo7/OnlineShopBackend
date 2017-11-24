package pl.kflorczyk.onlineshopbackend.rest_controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.kflorczyk.onlineshopbackend.dto.OrderProductDTO;
import pl.kflorczyk.onlineshopbackend.exceptions.ProductNotFoundException;
import pl.kflorczyk.onlineshopbackend.exceptions.UserAddressNotFoundException;
import pl.kflorczyk.onlineshopbackend.exceptions.UserNotFoundException;
import pl.kflorczyk.onlineshopbackend.model.Order;
import pl.kflorczyk.onlineshopbackend.rest_controllers.responses.ResponseDetail;
import pl.kflorczyk.onlineshopbackend.services.JwtService;
import pl.kflorczyk.onlineshopbackend.services.OrderService;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtService jwtService;

    @PutMapping(path = "/orders")
    public String createOrder(
            @RequestParam(name = "addressID") long addressID,
            @RequestParam(name = "deliveryMethod") String deliveryMethod,
            @RequestParam(name = "paymentMethod") String paymentMethod,
            @RequestBody List<OrderProductDTO> orderProductDTOs
    ) {
        Order order = null;

        try {
            order = orderService.makeOrder(addressID, deliveryMethod, paymentMethod, orderProductDTOs);
        } catch(UserNotFoundException | UserAddressNotFoundException | ProductNotFoundException e) {
            e.printStackTrace();
            return mapToJSON(Claimant.SINGLE_ORDER, new ResponseDetail<>(e.getMessage()));
        }
        return mapToJSON(Claimant.SINGLE_ORDER, new ResponseDetail<>(order));
    }

    @GetMapping(path = "/orders/{orderID}")
    public String getOrder(@PathVariable(name = "orderID") long orderID) {
        Order order = orderService.getOrder(orderID);
        return mapToJSON(Claimant.SINGLE_ORDER, new ResponseDetail<>(order));
    }

    @GetMapping(path = "/orders")
    public String getOrders() {
        List<Order> orders = orderService.getOrders();
        return mapToJSON(Claimant.MANY_ORDERS, new ResponseDetail<>(orders));
    }

    private FilterProvider getJSONFilters(Claimant claimant) {
        if(claimant == Claimant.SINGLE_ORDER) {
            return new SimpleFilterProvider()
                    .addFilter("Order", SimpleBeanPropertyFilter.serializeAllExcept("user"))
                    .addFilter("User", SimpleBeanPropertyFilter.serializeAllExcept("password", "addresses"))
                    .addFilter("Product", SimpleBeanPropertyFilter.filterOutAllExcept("id", "name"));
        } else if(claimant == Claimant.MANY_ORDERS) {
            return new SimpleFilterProvider()
                    .addFilter("Order", SimpleBeanPropertyFilter.serializeAllExcept("user"))
                    .addFilter("User", SimpleBeanPropertyFilter.serializeAllExcept("password", "addresses"))
                    .addFilter("Product", SimpleBeanPropertyFilter.filterOutAllExcept("id", "name"));
        }
        return null;
    }

    private enum Claimant {
        SINGLE_ORDER,
        MANY_ORDERS
    }

    private String mapToJSON(Claimant claimant, Object valueToMap) {
        String result = null;
        try {
            result = new ObjectMapper()
                    .writer(getJSONFilters(claimant))
                    .writeValueAsString(valueToMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{\"status\": \"failure\", \"description\": \"internal error\"}";
        }
        return result;
    }
}
