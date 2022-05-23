package com.danielhaimov.ordermanagement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ORDERS_ITEMS")
public class OrderItemEntity {

    private String id;
    private String orderId;
    private String productId;
    private int quantity;

    public OrderItemEntity() {
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "OrderItemEntity [id=" + id + ", orderId=" + orderId + ", productId=" + productId + ", quantity="
				+ quantity + "]";
	}
    
    

    
}
