package com.danielhaimov.ordermanagement.service;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;

import com.danielhaimov.ordermanagement.model.OrderEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderDAO extends ReactiveMongoRepository<OrderEntity, String> {

	Mono<OrderEntity> findByUserEmailAndFulfilledTimestamp(@Param("userEmail") String userEmail, @Param("fulfilledTimestamp") String fulfilledTimestamp);
	
	Flux<OrderEntity> findAllByUserEmail(@Param("userEmail") String userEmail);
	
	Mono<OrderEntity> findByOrderId(@Param("orderId") String orderId);
}
