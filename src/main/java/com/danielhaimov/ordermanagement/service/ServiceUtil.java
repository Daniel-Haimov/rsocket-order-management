package com.danielhaimov.ordermanagement.service;

import java.util.ArrayList;

import com.danielhaimov.ordermanagement.model.*;

public class ServiceUtil {

    public OrderEntity orderToEntity(OrderBoundary boundary) {
        OrderEntity entity = new OrderEntity();
        entity.setOrderId(boundary.getOrderId());
        entity.setUserEmail(boundary.getUserEmail());
        entity.setCreatedTimestamp(boundary.getCreatedTimestamp());
        entity.setFulfilledTimestamp(boundary.getFulfilledTimestamp());
        entity.setProducts(boundary.getProducts());
        return entity;
    }

    public OrderBoundary orderToBoundary(OrderEntity entity) {
        OrderBoundary boundary = new OrderBoundary();
        boundary.setOrderId(entity.getOrderId());
        boundary.setUserEmail(entity.getUserEmail());
        boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
        boundary.setFulfilledTimestamp(entity.getFulfilledTimestamp());
        boundary.setProducts(entity.getProducts());
        return boundary;
    }

    public OrderItemEntity orderItemToEntity(OrderItemBoundary boundary) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrderId(boundary.getOrderId());
        entity.setProductId(boundary.getProductId());
        entity.setQuantity(boundary.getQuantity());
        return entity;
    }

    public OrderItemBoundary orderItemToBoundary(OrderItemEntity entity) {
        OrderItemBoundary boundary = new OrderItemBoundary();
        boundary.setOrderId(entity.getOrderId());
        boundary.setProductId(entity.getProductId());
        boundary.setQuantity(entity.getQuantity());
        return boundary;
    }
    
    public ArrayList<OrderItemBoundary> getItemsFromOrderBoundary(OrderBoundary boundary) {
    	String orderId = boundary.getOrderId();
    	ArrayList<ItemBoundary> list = boundary.getProducts();
    	ArrayList<OrderItemBoundary> orderItemsList = new ArrayList<>();
    	for (ItemBoundary itemBoundary : list) {
    		orderItemsList.add(new OrderItemBoundary(orderId, itemBoundary.getProductId(), itemBoundary.getQuantity()));
		}
    	return orderItemsList;
    }
}
