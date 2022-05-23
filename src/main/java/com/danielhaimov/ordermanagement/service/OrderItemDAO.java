package com.danielhaimov.ordermanagement.service;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;

import com.danielhaimov.ordermanagement.model.OrderItemEntity;

import reactor.core.publisher.Flux;

public interface OrderItemDAO extends ReactiveMongoRepository<OrderItemEntity, String> {

    Flux<OrderItemEntity> findAllByOrderId(@Param("orderId") String orderId);
}

