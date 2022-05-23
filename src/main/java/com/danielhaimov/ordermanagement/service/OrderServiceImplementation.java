package com.danielhaimov.ordermanagement.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danielhaimov.ordermanagement.model.UserBoundary;
import com.danielhaimov.ordermanagement.model.ItemBoundary;
import com.danielhaimov.ordermanagement.model.OrderBoundary;
import com.danielhaimov.ordermanagement.model.OrderEntity;
import com.danielhaimov.ordermanagement.model.OrderItemBoundary;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

@Service
public class OrderServiceImplementation implements OrderService {

	private final OrderDAO orderDAO;
	private final OrderItemDAO orderItemDAO;
	private final ServiceUtil util;
	private final Log logger;

	@PostConstruct
	public void init() {
		Hooks.onErrorDropped(e -> {
		});
	}

	@Autowired
	public OrderServiceImplementation(OrderDAO orderDAO, OrderItemDAO orderItemDAO) {
		super();
		this.orderDAO = orderDAO;
		this.orderItemDAO = orderItemDAO;
		this.util = new ServiceUtil();
		this.logger = LogFactory.getLog(OrderServiceImplementation.class);
	}

	@Override
	public Mono<OrderBoundary> store(OrderBoundary order) {
        this.logger.debug("Received order-req-resp request with: " + order);

        validateUserEmail(order.getUserEmail());
       
        Mono<OrderEntity> optianlOpenOrder = this.orderDAO.findByUserEmailAndFulfilledTimestamp(order.getUserEmail(), null);
        
        if (optianlOpenOrder != null) {
        	return optianlOpenOrder
        			.map(this.util::orderToBoundary)
        			.map(boundary -> updateOrder(boundary, order))
                    .map(this.util::orderToEntity)
                    .flatMap(this.orderDAO::save)
                    .map(this.util::orderToBoundary)
                    .log();
        }

        return Mono.just(order)
                .map(boundary -> {
                    boundary.setOrderId(null);
                    boundary.setCreatedTimestamp(new Date());
                    boundary.setFulfilledTimestamp(null);
                    return boundary;
                })
                .map(this.util::orderToEntity)
                .flatMap(this.orderDAO::save)
                .map(this.util::orderToBoundary)
                .log();
	}

	@Override
	public Mono<Void> fulfill(OrderBoundary order) {
		this.logger.debug("Received fulfill-fire-and-forget request with: " + order);
        // Validate user provided productId field
        if (order.getOrderId() == null || order.getOrderId().isEmpty() || order.getFulfilledTimestamp() != null)
            return Mono.empty();

        return this.orderDAO
                .findById(order.getOrderId())
                .flatMap(entity -> Mono.just(order))
                .map(boundary -> {
                	boundary.setFulfilledTimestamp(new Date());
                    return boundary;
                })
                .map(this.util::orderToEntity)
                .flatMap(this.orderDAO::save)
                .log()
                .then();
	}

	@Override
	public Flux<OrderItemBoundary> getAllOpenOrderItemsByEmail(UserBoundary user) {
		this.logger.debug("Received getOpenOrderItems-stream request with: " + user);
		validateUserEmail(user.getUserEmail());
        
        Mono<OrderEntity> optianlOpenOrder = this.orderDAO.findByUserEmailAndFulfilledTimestamp(user.getUserEmail(), null);
        
        if (optianlOpenOrder != null) {
        	return optianlOpenOrder
        			.map(this.util::orderToBoundary)
        			.flatMapIterable(boundary -> this.util.getItemsFromOrderBoundary(boundary))
                    .log();
        }
        
        return Flux.empty();
	}

	@Override
	public Flux<OrderBoundary> getAllOrdersByEmail(UserBoundary user) {
		this.logger.debug("Received getOrders-stream request with: " + user);
		validateUserEmail(user.getUserEmail());
		
		return this.orderDAO
				.findAllByUserEmail(user.getUserEmail())
				.map(this.util::orderToBoundary)
				.log();
	}

	@Override
	public Flux<OrderItemBoundary> getItemsByOrderChannel(Flux<OrderBoundary> order) {
		this.logger.debug("Received getItemsByOrder-channel request.");
		return order
				.switchMap(this::getAllItemsByOrder)
				.log();
	}

	@Override
	public Mono<Void> deleteAll() {
		this.logger.debug("Received cleanup-fire-and-forget request.");

		return this.orderDAO.deleteAll().then(this.orderItemDAO.deleteAll());
	}

	private OrderBoundary updateOrder(OrderBoundary openOrder, OrderBoundary order) {
		ArrayList<ItemBoundary> openOrderProducts = openOrder.getProducts();
		ArrayList<ItemBoundary> orderProducts = order.getProducts();
		
		ArrayList<ItemBoundary> mergedOrderProducts = new ArrayList<>();
		
		String openOrderItemBoundaryProductId;
		String orderItemBoundaryProductId;
		
		for (ItemBoundary openOrderItemBoundary : openOrderProducts) {
			openOrderItemBoundaryProductId = openOrderItemBoundary.getProductId();
			
			for (ItemBoundary orderItemBoundary : orderProducts) {
				orderItemBoundaryProductId = orderItemBoundary.getProductId();
				if(openOrderItemBoundaryProductId.equals(orderItemBoundaryProductId)) {
					int sum = openOrderItemBoundary.getQuantity() + orderItemBoundary.getQuantity();
					if(sum < 0) {
						sum = 0;
					}
					openOrderItemBoundary.setQuantity(sum);
					orderProducts.remove(orderItemBoundary);
					break;
				}
			}
			mergedOrderProducts.add(openOrderItemBoundary);
		}
		
		mergedOrderProducts.addAll(orderProducts);
		
		openOrder.setProducts(mergedOrderProducts);
		return openOrder;
	}
	
	private void validateUserEmail(String email) {
        // Validate user provided name and userEmail fields
        if (email == null || email.isEmpty())
            throw new RuntimeException("Please provide Name and userEmail.");

        if (!Pattern.compile("[A-Z0-9_.]+@([A-Z0-9]+\\.)+[A-Z0-9]{2,6}$",
                Pattern.CASE_INSENSITIVE).matcher(email).find()) {
            throw new RuntimeException("Please provide a valid email address");
        }
	}

    private Flux<OrderItemBoundary> getAllItemsByOrder(OrderBoundary order) {
        // Validate user provided wishListId
    	validateUserEmail(order.getUserEmail());

        return this.orderDAO
        		.findByOrderId(order.getOrderId())
                .map(this.util::orderToBoundary)
                .flatMapIterable(boundary -> this.util.getItemsFromOrderBoundary(boundary));
    }
}
