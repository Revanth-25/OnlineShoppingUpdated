package com.onlineshopping.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlineshopping.dto.OrderDetailsDto;
import com.onlineshopping.dto.OrderDto;
import com.onlineshopping.dto.ProductDto;
import com.onlineshopping.dto.UserDto;
import com.onlineshopping.dto.UserViewDto;
import com.onlineshopping.entity.Order;
import com.onlineshopping.entity.OrderDetails;
import com.onlineshopping.entity.User;
import com.onlineshopping.exceptions.InvalidEmailException;
import com.onlineshopping.exceptions.InvalidPasswordException;
import com.onlineshopping.exceptions.OrderNotFoundException;
import com.onlineshopping.exceptions.PasswordMissmatchException;
import com.onlineshopping.exceptions.UserCreationException;
import com.onlineshopping.exceptions.UserNotFoundException;
import com.onlineshopping.repository.UserRepository;

@Service
public class UserService{

	@Autowired
	UserRepository userRepository;

	public String insertUser(UserDto userDto) {
		Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(userDto.getUserEmail());
		if (optionalUser.isPresent()) {
			throw new UserCreationException("USER_ALREADY_EXISTS");
		} else {

			if (userDto.getUserName() == null || !userDto.getUserName().matches("^[a-zA-Z][\\sa-zA-Z0-9]+$")) {
				throw new UserCreationException("INVALID_USER_NAME");
			}
			if (userDto.getPassword() == null || !userDto.getPassword().matches("^[a-zA-Z0-9@_-]{7,15}$")) {
				throw new InvalidPasswordException("INVALID_PASSWORD");
			} else if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
				throw new PasswordMissmatchException("PASSWORD_MISMATCH");
			}
			if (userDto.getUserEmail() == null
					|| !userDto.getUserEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
				throw new InvalidEmailException("INVALID_EMAIL");
			}
			if (!(Long.toString(userDto.getPhonenumber()).matches("^(?:\\+91|0)?[6789]\\d{9}$"))) {
				throw new UserCreationException("INVALID_MOBILE_NUMBER");
			}
			if (userDto.getUserType() == 0 || (userDto.getUserType() != 'A' && userDto.getUserType() != 'U')) {
				throw new UserCreationException("INVALID USER TYPE");
			}
		}
		User user = new User();
		user.setUserName(userDto.getUserName());
		user.setEmail(userDto.getUserEmail());
		user.setPhonenumber(userDto.getPhonenumber());
		user.setPassword(userDto.getPassword());
		user.setUserType(userDto.getUserType());
		user.setAddress(userDto.getAddress());

		userRepository.save(user);

		return "Registration Successful";
	}

	public String userLogin(UserDto userDto) {
		Optional<User> user = userRepository.findByEmailIgnoreCase(userDto.getUserEmail());
		if (user.isPresent()) {
			if (user.get().getPassword().equals(userDto.getPassword())) {
				return "Logged in successfully. Welcome " + user.get().getUserName();
			}
			throw new InvalidEmailException("INVALID PASSWORD");
		}
		throw new UserNotFoundException();
	}

	public UserViewDto getUserProfileByEmail(String email) {
		Optional<User> user = userRepository.findByEmailIgnoreCase(email);
		if (user.isPresent()) {
			User dbUser = user.get();
			UserViewDto userDto = new UserViewDto();
			userDto.setUserName(dbUser.getUserName());
			userDto.setUserEmail(dbUser.getEmail());
			userDto.setPassword(dbUser.getPassword());
			userDto.setPhonenumber(dbUser.getPhonenumber());
			userDto.setUserType(dbUser.getUserType());
			userDto.setAddress(dbUser.getAddress());

			return userDto;

		} else
			throw new UserNotFoundException();
	}

	public String updateUserDetails(UserDto userDto) {
		Optional<User> userOptional = userRepository.findByEmailIgnoreCase(userDto.getUserEmail());
		if (userOptional.isPresent()) {
			User userDb = userOptional.get();
			if (userDto.getUserName() != null) {
				if (userDto.getUserName().matches("^[a-zA-Z][\\sa-zA-Z0-9]+$")) {
					userDb.setUserName(userDto.getUserName());
				} else {
					throw new UserCreationException("INVALID_USER_NAME");
				}
			}
			if (userDto.getPhonenumber() != 0) {
					if (Long.toString(userDto.getPhonenumber()).matches("^(?:\\+91|0)?[6789]\\d{9}$")) {
				userDb.setPhonenumber(userDto.getPhonenumber());
			} else {
				throw new UserCreationException("INVALID_MOBILE_NUMBER");
			       }
			}
			if (userDto.getAddress() != null) {
				userDb.setAddress(userDto.getAddress());
			}
			userRepository.save(userDb);
			return "Successfully Updated Profile";
		}
		throw new UserNotFoundException();
	}

	public String updatePassword(UserDto userDto) {
		Optional<User> userOptional = userRepository.findByEmailIgnoreCase(userDto.getUserEmail());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (user.getPassword().trim().equals(userDto.getOldPassword().trim())) {
				if (userDto.getPassword() != null && userDto.getConfirmPassword() != null
						&& userDto.getPassword().matches("^[a-zA-Z0-9@_-]{7,15}$")
						&& userDto.getPassword().equals(userDto.getConfirmPassword())) {
					user.setPassword(userDto.getPassword());
					return "Password Updated";
				}
				throw new InvalidPasswordException("INVALID PASSWORD . FAILED TO UPDATE PASSWORD");
			}
			throw new PasswordMissmatchException("OLD PASSWORD IS INCORRECT");
		}
		throw new UserNotFoundException();
	}

	public String deleteUser(UserDto userDto) {
		Optional<User> userOptional = userRepository.findByEmailIgnoreCase(userDto.getUserEmail());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if ((userDto.getPassword() != null) && (user.getPassword().equals(userDto.getPassword()))) {
				userRepository.delete(user);
				return "Account deleted successfully";
			}
			throw new InvalidPasswordException("INVALID PASSWORD . FAILED TO DELETE ACCOUNT");
		}
		throw new UserNotFoundException();
	}

	public List<OrderDto> viewOrders(ProductDto productDto) {
		Optional<User> userOptional = userRepository.findByEmailIgnoreCase(productDto.getUserEmail());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			List<Order> userOrders = user.getOrder();
			if (userOrders == null || userOrders.isEmpty()) {
				throw new OrderNotFoundException("User has no orders to show");
			}
			List<OrderDto> orderDtoList = new ArrayList<>();
			for (Order orders : user.getOrder()) {
				List<OrderDetailsDto> orderDetailsList = new ArrayList<>();
				for (OrderDetails orderDetails : orders.getOrderDetails()) {
					OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
					orderDetailsDto.setName(orderDetails.getProduct().getProductName());
					orderDetailsDto.setQuantity(orderDetails.getQuantity());
					orderDetailsDto.setTotal(orderDetails.getTotal());
					orderDetailsList.add(orderDetailsDto);
				}
				if (!orderDetailsList.isEmpty()) {
					OrderDto orderDto = new OrderDto();
					orderDto.setOrderDate(orders.getOrderDate());
					orderDto.setOrderTotal(orders.getOrderTotal());
					orderDto.setOrderDetails(orderDetailsList);
					orderDtoList.add(orderDto);
				}
			}
			return orderDtoList;
		}
		throw new UserNotFoundException("Please login or register");
	}

}
