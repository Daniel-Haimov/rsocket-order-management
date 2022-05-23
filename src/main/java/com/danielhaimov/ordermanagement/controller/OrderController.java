package com.danielhaimov.ordermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.danielhaimov.ordermanagement.model.UserBoundary;
import com.danielhaimov.ordermanagement.model.OrderBoundary;
import com.danielhaimov.ordermanagement.model.OrderItemBoundary;
import com.danielhaimov.ordermanagement.service.OrderServiceImplementation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class OrderController {

    private final OrderServiceImplementation order;

    @Autowired
    public OrderController(OrderServiceImplementation order) {
        super();
        this.order = order;
    }

    // CLI INVOCATION SAMPLE:
    // java -jar rsc-0.9.1.jar --debug --request --data "{\"name\":\"Daniel's order\", \"userEmail\":\"daniel@afeka.ac.il\"}" --route order-req-resp tcp://localhost:7000
    @MessageMapping("order-req-resp")
    public Mono<OrderBoundary> createRequestResponse(OrderBoundary input) {
        return this.order.store(input);
    }

    // CLI INVOCATION SAMPLE:
    // java -jar rsc-0.9.1.jar --debug --fnf --data "{\"orderId\":\"61b4834f58e87861702568ae\", \"productId\":\"p42\"}" --route fulfill-fire-and-forget tcp://localhost:7000
    @MessageMapping("fulfill-fire-and-forget")
    public Mono<Void> fulfillFireAndForget(OrderBoundary input) {
        return this.order.fulfill(input);
    }
    
    // CLI INVOCATION SAMPLE:
    // java -jar rsc-0.9.1.jar --stream --data "{\"userEmail\":\"daniel@afeka.ac.il\"}" --route getOpenOrderItems-stream tcp://localhost:7000
    @MessageMapping("getOpenOrderItems-stream")
    public Flux<OrderItemBoundary> getOpenOrderItemsStream(UserBoundary user) {
        return this.order.getAllOpenOrderItemsByEmail(user);
    }
    
    // CLI INVOCATION SAMPLE:
    // java -jar rsc-0.9.1.jar --stream --data "{\"userEmail\":\"daniel@afeka.ac.il\"}" --route getOrders-stream tcp://localhost:7000
    @MessageMapping("getOrders-stream")
    public Flux<OrderBoundary> getAllOrdersByEmailStream(UserBoundary user) {
        return this.order.getAllOrdersByEmail(user);
    }
    
    // CLI INVOCATION SAMPLE:
    // java -jar rsc-0.9.1.jar --channel --data=- --route getItemsByOrder-channel tcp://localhost:7000
    @MessageMapping("getItemsByOrder-channel")
    public Flux<OrderItemBoundary> getItemsByOrderChannel(Flux<OrderBoundary> order) {
        return this.order.getItemsByOrderChannel(order);
    }
    
    // CLI INVOCATION SAMPLE:
    // java -jar rsc-0.9.1.jar --debug --fnf --route cleanup-fire-and-forget tcp://localhost:7000
    @MessageMapping("cleanup-fire-and-forget")
    public Mono<Void> cleanupFNF() {
        return this.order.deleteAll();
    }
    
}
