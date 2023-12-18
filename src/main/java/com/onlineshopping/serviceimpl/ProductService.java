package com.onlineshopping.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlineshopping.dto.ProductViewDto;
import com.onlineshopping.entity.Category;
import com.onlineshopping.entity.Product;
import com.onlineshopping.exceptions.CategoryNotFoundException;
import com.onlineshopping.exceptions.ProductNotFoundException;
import com.onlineshopping.repository.CategoryRepository;
import com.onlineshopping.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<ProductViewDto> viewProducts() {
		List<Product> products = productRepository.findAll();

		List<ProductViewDto> productsDto = new ArrayList<>();
		for (Product prod : products) {
		     ProductViewDto productView = new ProductViewDto();
		     productView.setProductId(prod.getProductId());
		     productView.setProductName(prod.getProductName());
		     productView.setProductPrice(prod.getProductPrice());
		     productView.setProductStock(prod.getProductStock());
		     productView.setProductDesc(prod.getProductDescription());
		     productView.setProductManufDate(prod.getProductManufactureDate());
		     productView.setProductExPDate(prod.getProductExpiryDate());
		     productView.setCategory(prod.getCategory().getCategoryName());
		     productsDto.add(productView);
		}
		if (productsDto.isEmpty())
			throw new ProductNotFoundException("No Products to show");
		return productsDto;

	}

	@Transactional(readOnly = true)
	public List<ProductViewDto> viewByCategory(String categoryName) {
		Optional<Category> categoryOptional = categoryRepository.findByCategoryNameIgnoreCase(categoryName);
		if (categoryOptional.isPresent()) {
			Category category = categoryOptional.get();

			List<Product> products = productRepository.findByCategory(category);
			List<ProductViewDto> productDto = new ArrayList<>();

			for (Product prod : products) {
				ProductViewDto productView = new ProductViewDto();
			     productView.setProductId(prod.getProductId());
			     productView.setProductName(prod.getProductName());
			     productView.setProductPrice(prod.getProductPrice());
			     productView.setProductStock(prod.getProductStock());
			     productView.setProductDesc(prod.getProductDescription());
			     productView.setProductManufDate(prod.getProductManufactureDate());
			     productView.setProductExPDate(prod.getProductExpiryDate());
			     productView.setCategory(prod.getCategory().getCategoryName());
			     productDto.add(productView);	
			}
			if (productDto.isEmpty()) 
				throw new ProductNotFoundException("No Products available in the category:" + categoryName);
			return productDto;
		}
		throw new CategoryNotFoundException("Category Not Available");
	}

	@Transactional(readOnly = true)
	public ProductViewDto viewProductByName(String productName) {
		Optional<Product> prodOptional = productRepository.findByProductNameIgnoreCase(productName);
		if (prodOptional.isPresent()) {
			Product prod = prodOptional.get();
			ProductViewDto productView = new ProductViewDto();
		     productView.setProductId(prod.getProductId());
		     productView.setProductName(prod.getProductName());
		     productView.setProductPrice(prod.getProductPrice());
		     productView.setProductStock(prod.getProductStock());
		     productView.setProductDesc(prod.getProductDescription());
		     productView.setProductManufDate(prod.getProductManufactureDate());
		     productView.setProductExPDate(prod.getProductExpiryDate());
		     productView.setCategory(prod.getCategory().getCategoryName());
		     return productView;	
		}
		throw new ProductNotFoundException("No Products available with the product name:"+productName);
	}
}
