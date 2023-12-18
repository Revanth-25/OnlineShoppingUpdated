package com.onlineshopping.servicetest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineshopping.dto.OrderDto;
import com.onlineshopping.dto.ProductDto;
import com.onlineshopping.dto.UserDto;
import com.onlineshopping.dto.UserViewDto;
import com.onlineshopping.entity.Address;
import com.onlineshopping.entity.Order;
import com.onlineshopping.entity.OrderDetails;
import com.onlineshopping.entity.Product;
import com.onlineshopping.entity.User;
import com.onlineshopping.exceptions.InvalidEmailException;
import com.onlineshopping.exceptions.InvalidPasswordException;
import com.onlineshopping.exceptions.OrderNotFoundException;
import com.onlineshopping.exceptions.PasswordMissmatchException;
import com.onlineshopping.exceptions.UserCreationException;
import com.onlineshopping.exceptions.UserNotFoundException;
import com.onlineshopping.repository.UserRepository;
import com.onlineshopping.serviceimpl.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;

@SpringBootTest
class TestUserService {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Test
	void insertUser() {
		UserDto userDto = new UserDto();
		userDto.setUserName("revanth");
		userDto.setUserEmail("revanth.pasupula@gmail.com");
		userDto.setPassword("revanth");
		userDto.setConfirmPassword("revanth");
		userDto.setPhonenumber(7766554488l);
		userDto.setUserType('U');
		userDto.setAddress(new Address("3/4", "umanagar", "hyderabad", "telangana", 501401));
		User user = new User();
		user.setUserName(userDto.getUserName());
		user.setEmail(userDto.getUserEmail());
		user.setPassword(userDto.getUserEmail());
		user.setPhonenumber(userDto.getPhonenumber());
		user.setUserType(userDto.getUserType());
		user.setAddress(userDto.getAddress());
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

		String savedUser = userService.insertUser(userDto);
		System.out.println(savedUser);

		Assertions.assertEquals("Registration Successful", savedUser);
	}

	@Test
	void insertUser_UserAlreadyExists() {
		UserDto userDto = new UserDto();
		userDto.setUserName("revanth");
		userDto.setUserEmail("revanth.pasupula@gmail.com");
		User user = new User();
		user.setUserName(userDto.getUserName());
		user.setEmail(userDto.getUserEmail());
		Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(user));
		Assertions.assertThrows(UserCreationException.class, () -> userService.insertUser(userDto));
	}

	@Test
	void insertUser_InvalidUserName() {
		UserDto userDto = new UserDto();
		userDto.setUserName(null);
		Assertions.assertThrows(UserCreationException.class, () -> userService.insertUser(userDto));
	}

	@Test
	void insertUser_InvalidPassword() {
		// Create a userDto with an invalid password
		UserDto userDto = new UserDto();
		userDto.setUserName("revanth");
		userDto.setPassword(null);
		Assertions.assertThrows(InvalidPasswordException.class, () -> userService.insertUser(userDto));
	}

	@Test
	void insertUser_PasswordMismatch() {
		// Create a userDto with mismatched passwords
		UserDto userDto = new UserDto();
		userDto.setUserName("revanth");
		userDto.setPassword("newpassword");
		userDto.setConfirmPassword("oldpassword");

		Assertions.assertThrows(PasswordMissmatchException.class, () -> userService.insertUser(userDto));
	}

	@Test
	void insertUser_InvalidEmail() {
		// Create a userDto with an invalid email
		UserDto userDto = new UserDto();
		userDto.setUserName("revanth");
		userDto.setPassword("newpassword");
		userDto.setConfirmPassword("newpassword");
		userDto.setUserEmail(null);
		Assertions.assertThrows(InvalidEmailException.class, () -> userService.insertUser(userDto));
	}

	@Test
	void insertUser_InvalidMobileNumber() {
		// Create a userDto with an invalid mobile number
		UserDto userDto = new UserDto();
		userDto.setUserName("revanth");
		userDto.setPassword("newpassword");
		userDto.setConfirmPassword("newpassword");
		userDto.setUserEmail("revanth@gmail.com");
		userDto.setPhonenumber(112233);
		Assertions.assertThrows(UserCreationException.class, () -> userService.insertUser(userDto));
	}

	@Test
	void insertUser_InvalidUserType() {
		// Create a userDto with an invalid user type
		UserDto userDto = new UserDto();
		userDto.setUserName("revanth");
		userDto.setPassword("newpassword");
		userDto.setConfirmPassword("newpassword");
		userDto.setUserEmail("revanth@gmail.com");
		userDto.setPhonenumber(7766554488l);
		userDto.setUserType('B');
		Assertions.assertThrows(UserCreationException.class, () -> userService.insertUser(userDto));
	}

	@Test
	void userLogin_SuccessfulLogin() {
		UserDto userDto = new UserDto();
		userDto.setUserEmail("revanth.pasupula@gmail.com"); // Set the email for retrieval
		userDto.setPassword("revanth");

		User user = new User();
		user.setEmail(userDto.getUserEmail());
		user.setPassword(userDto.getPassword());
		user.setUserName("initialname");
		// Mock the behavior for user retrieval by email
		Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(user));

		String userLogin = userService.userLogin(userDto);
		System.out.println(userLogin);

		Assertions.assertEquals("Logged in successfully. Welcome " + user.getUserName(), userLogin);
	}

	@Test
	void userLogin_InvalidPassword() {
		// Create a userDto with a valid email and invalid password
		UserDto userDto = new UserDto();
		userDto.setUserEmail("revanth@gmail.com");
		userDto.setPassword("Wrongpassword");
		User user = new User();
		user.setEmail(userDto.getUserEmail());
		user.setPassword("CorrectPassword");
		Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(user));
		Assertions.assertThrows(InvalidEmailException.class, () -> userService.userLogin(userDto));
	}

	@Test
	void userLogin_UserNotFound() {
		// Create a userDto with a non-existent email
		UserDto userDto = new UserDto();
		userDto.setUserEmail("notexistinguser@gmail.com");
		Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
		Assertions.assertThrows(UserNotFoundException.class, () -> userService.userLogin(userDto));
	}
	
	@Test
	void getUserProfileByEmail() throws JsonProcessingException {
		UserViewDto userDto = new UserViewDto();
		userDto.setUserName("revanth");
		userDto.setUserEmail("revanth.pasupula@gmail.com"); // Set the email for retrieval
		userDto.setPassword("revanth");
		userDto.setPhonenumber(7766554488l);
		userDto.setUserType('U');
		userDto.setAddress(new Address("3/4", "umanagar", "hyderabad", "telangana", 501401));

		User user = new User();
		user.setUserName(userDto.getUserName());
		user.setEmail(userDto.getUserEmail());
		user.setPassword(userDto.getPassword());
		user.setPhonenumber(userDto.getPhonenumber());
		user.setUserType(userDto.getUserType());
		user.setAddress(userDto.getAddress());

		Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(user));

		UserViewDto retrivedUsersDto = userService.getUserProfileByEmail(userDto.getUserEmail());
		System.out.println(retrivedUsersDto);

		// Using Jackson ObjectMapper to convert UserDto instances to JSON strings
		ObjectMapper objectMapper = new ObjectMapper();
		String expectedJson = objectMapper.writeValueAsString(userDto);
		String retrievedJson = objectMapper.writeValueAsString(retrivedUsersDto);

		Assertions.assertEquals(expectedJson, retrievedJson);
	}
	
	@Test
    void getUserProfileByEmail_UserNotFound()
	{
	    // Create a userDto for a non-existent user
		UserDto userDto = new UserDto();
		userDto.setUserEmail("notexistinguser@gmail.com");
		Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
		Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserProfileByEmail("notexistinguser@gmail.com"));
	}

	@Test
	void updateUserDetails() {
		UserDto userDto = new UserDto();
		userDto.setUserName("revanth");
		userDto.setUserEmail("revanth.pasupula@gmail.com"); // Set the email for retrieval
		userDto.setPhonenumber(7766554488l);
		userDto.setAddress(new Address("3/4", "umanagar", "hyderabad", "telangana", 501401));

		User user = new User();
		user.setUserName("initialusername");
		user.setEmail(userDto.getUserEmail());
		user.setPassword("initialpassword");
		user.setPhonenumber(1234567890l);
		user.setUserType('A');
		user.setAddress(new Address("initialHno", "initialStreet", "initialCity", "initialState", 123456));

		Mockito.when(userRepository.findByEmailIgnoreCase("revanth.pasupula@gmail.com")).thenReturn(Optional.of(user));

		String output = userService.updateUserDetails(userDto);
		System.out.println(output);

		// Verify that userRepository.save is called with the modified userDb
		Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));

		Assertions.assertEquals("Successfully Updated Profile", output);
	}
	
	@Test
	void updateUserDetails_UserNotFound() {
	    // Create a userDto for a non-existent user
		UserDto userDto = new UserDto();
		userDto.setUserEmail("notexistinguser@gmail.com");
		Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
		Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserProfileByEmail("notexistinguser@gmail.com"));
	}
	
	@Test
	void updateUserDetails_InvalidMobileNumber() {
		// Create a userDto with an invalid mobile number
		UserDto userDto = new UserDto();
		userDto.setUserEmail("revanth@gmail.com");
		userDto.setPhonenumber(112233);
		User user = new User();
		user.setEmail(userDto.getUserEmail());
		Mockito.when(userRepository.findByEmailIgnoreCase(userDto.getUserEmail())).thenReturn(Optional.of(user));
		Assertions.assertThrows(UserCreationException.class, () -> userService.updateUserDetails(userDto));
	}

	@Test
	void updateUserDetails_InvalidUserName() {
		// Create a userDto with an invalid mobile number
		UserDto userDto = new UserDto();
		userDto.setUserEmail("revanth@gmail.com");
		userDto.setUserName("99aBcd0");
		
		User user = new User();
		user.setEmail(userDto.getUserEmail());
		Mockito.when(userRepository.findByEmailIgnoreCase("revanth@gmail.com")).thenReturn(Optional.of(user));
		
		Assertions.assertThrows(UserCreationException.class, () -> userService.updateUserDetails(userDto));
	}

	@Test
	void updatePassword() {

		UserDto userDto = new UserDto();
		userDto.setUserEmail("revanth.pasupula@gmail.com");
		userDto.setOldPassword("revanth");
		userDto.setPassword("revanthnew");
		userDto.setConfirmPassword("revanthnew");

		User user = new User();
		user.setUserName("initialusername");
		user.setEmail(userDto.getUserEmail());
		user.setPassword("revanth");
		user.setPhonenumber(1234567890l);
		user.setUserType('A');
		user.setAddress(new Address("initialHno", "initialStreet", "initialCity", "initialState", 123456));

		Mockito.when(userRepository.findByEmailIgnoreCase("revanth.pasupula@gmail.com")).thenReturn(Optional.of(user));

		String output = userService.updatePassword(userDto);
		System.out.println(output);

		Assertions.assertEquals("Password Updated", output);
	}
	
    @Test
    void updatePassword_IncorrectOldPassword() {
        // Create a userDto with an incorrect old password
        UserDto userDto = new UserDto();
		userDto.setUserEmail("revanth.pasupula@gmail.com");
		userDto.setOldPassword("wrongpassword");  // Set a different old password
        
		User user = new User();
		user.setUserName("initialusername");
		user.setEmail(userDto.getUserEmail());
		user.setPassword("revanth");
        Mockito.when(userRepository.findByEmailIgnoreCase(userDto.getUserEmail())).thenReturn(Optional.of(user));
        Assertions.assertThrows(PasswordMissmatchException.class, () -> userService.updatePassword(userDto));
    }

    @Test
    void updatePassword_InvalidNewPassword() {
        // Create a userDto with an invalid new password
    	UserDto userDto = new UserDto();
  		userDto.setUserEmail("revanth.pasupula@gmail.com");
  		userDto.setOldPassword("revanth");  // Set a different old password
  		userDto.setPassword(null);
  		userDto.setConfirmPassword(null);
  		
		User user = new User();
		user.setUserName("initialusername");
		user.setEmail(userDto.getUserEmail());
		user.setPassword("revanth");
        

        Mockito.when(userRepository.findByEmailIgnoreCase(userDto.getUserEmail())).thenReturn(Optional.of(user));

        Assertions.assertThrows(InvalidPasswordException.class, () -> userService.updatePassword(userDto));
    }
    
    @Test
    void updatePassword_UserNotFound() {
        // Create a userDto for a non-existent user
        UserDto userDto = new UserDto();
        userDto.setUserName("notexistinguser@gmail.com"); 
        Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updatePassword(userDto));
    }
    
	@Test
	void deleteUser() {
		UserDto userDto = new UserDto();
		userDto.setUserEmail("revanth.pasupula@gmail.com");
		userDto.setPassword("revanth");

		User user = new User();
		user.setUserName("initialusername");
		user.setEmail(userDto.getUserEmail());
		user.setPassword("revanth");
		user.setPhonenumber(1234567890l);
		user.setUserType('A');
		user.setAddress(new Address("initialHno", "initialStreet", "initialCity", "initialState", 123456));

		Mockito.when(userRepository.findByEmailIgnoreCase("revanth.pasupula@gmail.com")).thenReturn(Optional.of(user));

		String output = userService.deleteUser(userDto);
		System.out.println(output);

		// Verify that userRepository.delete(user) is called once
		Mockito.verify(userRepository, Mockito.times(1)).delete(user);

		Assertions.assertEquals("Account deleted successfully", output);
	}
	
	@Test
	void deleteUser_InvalidPassword()
	{
		UserDto userDto = new UserDto();
		userDto.setUserEmail("gmail.com");
		userDto.setPassword("wrongpassword");		
		User user = new User();
		user.setEmail(userDto.getUserEmail());
		user.setPassword("correctpassword");		
		Mockito.when(userRepository.findByEmailIgnoreCase(userDto.getUserEmail())).thenReturn(Optional.of(user));		
		Assertions.assertThrows(InvalidPasswordException.class, () -> userService.deleteUser(userDto));

	}
	
	@Test
	void deleteUser_UserNotFound()
	{
	    // Create a userDto for a non-existent user
		UserDto userDto = new UserDto();
		userDto.setUserEmail("gmail.com");
		Mockito.when(userRepository.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
		Assertions.assertThrows(UserNotFoundException.class,() -> userService.deleteUser(userDto));		
	}
	

	@Test
	void viewOrders() {
		// Mock data
		ProductDto productDto = new ProductDto(101, "Product1", 100.0, "Description1", 10, null, null, "Category1");
	    productDto.setUserEmail("test.com");
		User user = new User();
		user.setEmail("test.com");
		user.setOrder(new ArrayList<>());
		Product product = new Product();
		product.setProductId(101);
		product.setProductName("Product1");
		Order order = new Order();
		order.setOrederId(0);		
		LocalDate date = LocalDate.now();
		order.setOrderDate(date);
		order.setOrderTotal(2000);
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setId(order.getOrederId());
		orderDetails.setOrder(order);
		orderDetails.setProduct(product);
		orderDetails.setQuantity(4);
		orderDetails.setTotal(4000);
		order.getOrderDetails().add(orderDetails);
		user.getOrder().add(order);
		
		// Mock repository responses
		Mockito.when(userRepository.findByEmailIgnoreCase(productDto.getUserEmail())).thenReturn(Optional.of(user));
		// Execute the service method
		List<OrderDto> result = userService.viewOrders(productDto);
		Assertions.assertEquals(1, result.size());
	}
	
	@Test
	void viewOrders_OrdersNotFound() {
		// Mock data
		ProductDto productDto = new ProductDto(101, "Product1", 100.0, "Description1", 10, null, null, "Category1");
	    productDto.setUserEmail("test.com");
		User user = new User();
		user.setEmail("test.com");
		
		// Mock repository responses
		Mockito.when(userRepository.findByEmailIgnoreCase(productDto.getUserEmail())).thenReturn(Optional.of(user));
		// Execute the service method
		Assertions.assertThrows(OrderNotFoundException.class, () -> userService.viewOrders(productDto));
	}
	
	@Test
	void viewOrders_UserNotFound() {
		// Mock data
		ProductDto productDto = new ProductDto();		
		// Mock repository responses
		Mockito.when(userRepository.findByEmailIgnoreCase(productDto.getUserEmail())).thenReturn(Optional.empty());
		// Execute the service method
		Assertions.assertThrows(UserNotFoundException.class, () -> userService.viewOrders(productDto));
	}
}
