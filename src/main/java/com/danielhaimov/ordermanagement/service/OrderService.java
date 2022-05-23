package com.danielhaimov.ordermanagement.service;

import com.danielhaimov.ordermanagement.model.UserBoundary;
import com.danielhaimov.ordermanagement.model.OrderBoundary;
import com.danielhaimov.ordermanagement.model.OrderItemBoundary;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {

    Mono<OrderBoundary> store(OrderBoundary order);
    
    Mono<Void> fulfill(OrderBoundary order);
    
    Flux<OrderItemBoundary> getAllOpenOrderItemsByEmail(UserBoundary user);

    Flux<OrderBoundary> getAllOrdersByEmail(UserBoundary user);
    
    Flux<OrderItemBoundary> getItemsByOrderChannel(Flux<OrderBoundary> order);
    
    Mono<Void> deleteAll();
    
}
