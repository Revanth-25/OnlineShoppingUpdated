package com.onlineshopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineshopping.dto.ProductViewDto;
import com.onlineshopping.serviceimpl.ProductService;

@RestController
@RequestMapping("api/products")
public class ProductController {
	
   @Autowired
   ProductService productService;
   
   @GetMapping("/view")
   public ResponseEntity<List<ProductViewDto>> viewProducts()
   {
	   return new ResponseEntity<>(productService.viewProducts(),HttpStatus.OK);
   }
   
   @GetMapping("/category/{categoryType}")
   public ResponseEntity<List<ProductViewDto>> viewByCategory(@PathVariable("categoryType")String categoryName)
   {
	   return new ResponseEntity<>(productService.viewByCategory(categoryName),HttpStatus.OK);
   }
   
   @GetMapping("/view/{productName}")
   public ResponseEntity<ProductViewDto> viewProductByName(@PathVariable String productName)
   {
	   return new ResponseEntity<>(productService.viewProductByName(productName),HttpStatus.OK);
   }

}
