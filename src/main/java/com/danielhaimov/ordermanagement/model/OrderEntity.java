package com.danielhaimov.ordermanagement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

@Document(collection = "ORDERS")
public class OrderEntity {

    private String orderId;
    private String userEmail;
    private Date createdTimestamp;
    private Date fulfilledTimestamp;
    private ArrayList<ItemBoundary> products;

    public OrderEntity() {
    }

    @Id
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Date getFulfilledTimestamp() {
		return fulfilledTimestamp;
	}

	public void setFulfilledTimestamp(Date fulfilledTimestamp) {
		this.fulfilledTimestamp = fulfilledTimestamp;
	}

	public ArrayList<ItemBoundary> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<ItemBoundary> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "OrderEntity [orderId=" + orderId + ", userEmail=" + userEmail + ", createdTimestamp=" + createdTimestamp
				+ ", fulfilledTimestamp=" + fulfilledTimestamp + ", products=" + products + "]";
	}
    
}
