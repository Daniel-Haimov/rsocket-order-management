package com.danielhaimov.ordermanagement.model;

public class ItemBoundary {

    private String productId;
    private int quantity;
    
	public ItemBoundary() {
		super();
	}

	public ItemBoundary(String productId, int quantity) {
		super();
		this.productId = productId;
		this.quantity = quantity;
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
		return "OrderItemBoundary [productId=" + productId + ", quantity=" + quantity + "]";
	}
	
	
}
