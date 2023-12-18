package com.onlineshopping.dto;

import java.time.LocalDate;

public class ProductViewDto {
	
	 
    private int  productId;
     private String productName;
    private double productPrice;
    private String productDesc;
    private int productStock;
    private LocalDate productExPDate;
    private LocalDate productManufDate;
    private String category;
    
    public ProductViewDto() {
    	
    }

	public ProductViewDto(int productId, String productName, double productPrice, String productDesc, int productStock,
			LocalDate productExPDate, LocalDate productManufDate, String category) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productDesc = productDesc;
		this.productStock = productStock;
		this.productExPDate = productExPDate;
		this.productManufDate = productManufDate;
		this.category = category;
	}


	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public int getProductStock() {
		return productStock;
	}

	public void setProductStock(int productStock) {
		this.productStock = productStock;
	}

	public LocalDate getProductExPDate() {
		return productExPDate;
	}

	public void setProductExPDate(LocalDate productExPDate) {
		this.productExPDate = productExPDate;
	}

	public LocalDate getProductManufDate() {
		return productManufDate;
	}

	public void setProductManufDate(LocalDate productManufDate) {
		this.productManufDate = productManufDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "ProductViewDto [productId=" + productId + ", productName=" + productName + ", productPrice="
				+ productPrice + ", productDesc=" + productDesc + ", productStock=" + productStock + ", productExPDate="
				+ productExPDate + ", productManufDate=" + productManufDate + ", category=" + category + "]";
	}

    

}
