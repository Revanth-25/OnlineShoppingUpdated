package com.onlineshopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineshopping.dto.ProductDto;
import com.onlineshopping.serviceimpl.AdminService;

@RestController
@RequestMapping("api/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@PostMapping("/addProduct")
    public ResponseEntity<String> addProduct(@RequestBody ProductDto productDto){
    	return new ResponseEntity<>(adminService.addProducts(productDto), HttpStatus.OK);
    }
	
	@PutMapping("/updateProduct")
	public ResponseEntity<String> updateProduct(@RequestBody ProductDto productDto){
		return new ResponseEntity<>(adminService.updateProduct(productDto),HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteProduct")
	public ResponseEntity<String> deleteProduct(@RequestBody ProductDto productDto)
	{
		return new ResponseEntity<>(adminService.deleteProduct(productDto),HttpStatus.OK);
	}

}
