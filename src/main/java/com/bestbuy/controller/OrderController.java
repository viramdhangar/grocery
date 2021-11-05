package com.bestbuy.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bestbuy.model.Address;
import com.bestbuy.model.CancelItem;
import com.bestbuy.model.DAOUser;
import com.bestbuy.model.EmailParam;
import com.bestbuy.model.ImageModel;
import com.bestbuy.model.Order;
import com.bestbuy.model.OrderItem;
import com.bestbuy.model.ProductDTO;
import com.bestbuy.model.TrackOrder;
import com.bestbuy.repository.ImageRepository;
import com.bestbuy.repository.OrderItemRepository;
import com.bestbuy.repository.OrderRepository;
import com.bestbuy.repository.ProductRepository;
import com.bestbuy.repository.TrackOrderRepository;
import com.bestbuy.repository.UserRepository;
import com.bestbuy.service.IEmailService;
import com.bestbuy.service.IImageModelService;

@CrossOrigin (origins = {"*"}, maxAge = 3600)
@RestController
public class OrderController {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	IImageModelService imageModelService;
	
	@Autowired
	private TrackOrderRepository trackOrderRepository;
	
	@Autowired
	private AddressController addCtrl;
	
	@Autowired
	IEmailService emailService;
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	public Optional<Order> saveOrder(Long userId, Order orders) {
		return userRepository.findById(userId).map(user -> {
			orders.setUser(user);
            return orderRepository.save(orders);
        });
		
	}
	
	@GetMapping("getOrders/{userId}/{offset}/{limit}")
	public List<Order> getOrders(@PathVariable Long userId,@PathVariable int offset,@PathVariable int limit){
		List<Order> orderList = orderRepository.findByUserIdOrderByCreatedDescLimited(userId, offset, limit);
		/*orderList.forEach(order->{
			Optional<Address> billling = addCtrl.address(order.getBillingAddressId());
			if(billling.isPresent()) {
				order.setBillingAddress(billling.get());
			}
			
			Optional<Address> delivery = addCtrl.address(order.getDeliveryAddressId());
			if(delivery.isPresent()) {
				order.setDeliveryAddress(delivery.get());
			}
		});*/
		return orderList;
	}
	
	@GetMapping("getStatusOrders/{offset}/{limit}")
	public List<Order> getAllOrders(@PathVariable int offset,@PathVariable int limit){
		List<Order> orderList = orderRepository.findAllOrderedLimited(offset, limit);
		/*orderList.forEach(order->{
			Optional<Address> billling = addCtrl.address(order.getBillingAddressId());
			if(billling.isPresent()) {
				order.setBillingAddress(billling.get());
			}
			
			Optional<Address> delivery = addCtrl.address(order.getDeliveryAddressId());
			if(delivery.isPresent()) {
				order.setDeliveryAddress(delivery.get());
			}
		});*/
		return orderList;
	}
	
	@GetMapping("getStatusOrders/{userId}/{offset}/{limit}")
	public List<Order> getAllOrders(@PathVariable Long userId, @PathVariable int offset,@PathVariable int limit){
		List<Order> orderList = orderRepository.findAllOrderedLimited(userId, offset, limit);
		/*orderList.forEach(order->{
			Optional<Address> billling = addCtrl.address(order.getBillingAddressId());
			if(billling.isPresent()) {
				order.setBillingAddress(billling.get());
			}
			
			Optional<Address> delivery = addCtrl.address(order.getDeliveryAddressId());
			if(delivery.isPresent()) {
				order.setDeliveryAddress(delivery.get());
			}
		});*/
		return orderList;
	}
	
	@GetMapping("getOrderItems/{orderId}")
	public List<OrderItem> getOrderItems(@PathVariable Long orderId){
		List<OrderItem> orderList= orderItemRepository.findByOrderIdOrderByCreatedDesc(orderId);
		orderList.forEach(product->{
			List<ImageModel> list = imageModelService.getImages(product.getProductId());
			list.forEach(img->{
				try {
					/*String data = DatatypeConverter.printBase64Binary(decompressBytes(img.getPicByte()));
			        String imageString = "data:"+img.getType()+";base64," + data;
			        img.setImage(imageString);
			        if(null == product.getImage()) {
						product.setImage(imageString);
					}*/
					//img.setPicByte(decompressBytes(img.getPicByte()));
					String decodedString = new String(decompressBytes(img.getPicByte()));
					img.setDecodedBase64(decodedString);
					img.setPicByte(null);
				}catch(Exception e) {
					System.out.println("image currupted");
				}
			});
			
			product.setImages(list);
		});
		return orderList;
	}
	
	@GetMapping("orderTracking/{orderItemId}")
	public List<TrackOrder> orderTracking(@PathVariable Long orderItemId){
		List<TrackOrder> trackList= trackOrderRepository.findByOrderItemId(orderItemId);
		return trackList;
	}
	
	@PostMapping("placeOrder/{userId}")
	public Order order(@PathVariable Long userId, @RequestBody Order orders) {
		
		Order order = null;
		
		if(orders.getValidStatus().equalsIgnoreCase("ERROR")) {
			Optional<Order> orderOptional = saveOrder(userId, orders);	
			if(orderOptional.isPresent()) {
				order = orderOptional.get();
			}
			
			final Order orderNew = order;
			
			List<ProductDTO> productList = orders.getProducts();
			
			List<OrderItem> OrderItemList = new ArrayList<>();
			productList.forEach(product->{
				OrderItem orderItem= new OrderItem();	
				orderItem.setProductId(product.getId());
				orderItem.setName(product.getName());
				orderItem.setDelivery(product.getDelivery());
				orderItem.setDescription(product.getDescription());
				orderItem.setAvailability(product.getAvailability());
				orderItem.setDeliveryType(product.getDeliveryType());
				orderItem.setMrp(product.getMrp());
				orderItem.setPopularity(product.getPopularity());
				orderItem.setQuantity(product.getQuantity());
				orderItem.setSize(product.getSize());
				orderItem.setStatus(orders.getStatus()+" error");
				orderItem.setWeight(product.getWeight());
				orderItem.setType(product.getType());
				orderItem.setSellingPrize(product.getSellingPrize());
				orderItem.setDeliveryCharges(product.getDeliveryCharges());
				orderItem.setFreeDelivery(product.getFreeDelivery());
				orderItem.setPaidDelivery(product.getPaidDelivery());
				orderItem.setIsCashOn(product.getIsCashOn());
				orderItem.setCanCancel(product.getCanCancel());
				orderItem.setOrder(orderNew);
				OrderItemList.add(orderItem);
			});
			orderItemRepository.saveAll(OrderItemList);
		}else if(orders.getValidStatus().equalsIgnoreCase("SUCCESS")) {
			Optional<Order> orderOptional = saveOrder(userId, orders);
			if(orderOptional.isPresent()) {
				order = orderOptional.get();
			}
			
			final Order orderNew = order;
			
			List<ProductDTO> productList = orders.getProducts();
			
			List<OrderItem> OrderItemList = new ArrayList<>();
			List<TrackOrder> tarckOrderList = new ArrayList<>();
			productList.forEach(product->{
				OrderItem orderItem= new OrderItem();	
				orderItem.setProductId(product.getId());
				orderItem.setName(product.getName());
				orderItem.setDelivery(product.getDelivery());
				orderItem.setDescription(product.getDescription());
				orderItem.setAvailability(product.getAvailability());
				orderItem.setDeliveryType(product.getDeliveryType());
				orderItem.setMrp(product.getMrp());
				orderItem.setPopularity(product.getPopularity());
				orderItem.setQuantity(product.getQuantity());
				orderItem.setSelQuantity(product.getSelQuantity());
				orderItem.setSize(product.getSize());
				orderItem.setStatus(orders.getStatus());
				orderItem.setWeight(product.getWeight());
				orderItem.setType(product.getType());
				orderItem.setSellingPrize(product.getSellingPrize());
				orderItem.setDeliveryCharges(product.getDeliveryCharges());
				orderItem.setFreeDelivery(product.getFreeDelivery());
				orderItem.setPaidDelivery(product.getPaidDelivery());
				orderItem.setIsCashOn(product.getIsCashOn());
				orderItem.setCanCancel(product.getCanCancel());
				orderItem.setOrder(orderNew);
				OrderItemList.add(orderItem);
				trackEntry(orderItem, tarckOrderList);
			});
			orderItemRepository.saveAll(OrderItemList);
			
			trackOrderRepository.saveAll(tarckOrderList);
			
			// get user information
			DAOUser userDTO = null;
			Optional<DAOUser> user = userRepository.findById(userId);
			if(user.isPresent()) {
				userDTO = user.get();
			}
			
			// update product quantity
			productList.forEach(prod->{
				prod.setQuantity(prod.getQuantity()-prod.getSelQuantity());
				productRepository.updateProductQuantity(prod.getQuantity(), prod.getId());
			});
			
			// prepare email body
			Map<String, Object> model = new HashMap<>();
			model.put("salutation", "Hi "+userDTO.getFirstName()+",");
			model.put("orderId", orderNew.getId());
			model.put("orderList", OrderItemList);
			
			// send email notification
			EmailParam emailParam = new EmailParam();
			emailParam.setEmail(userDTO.getEmail());
			emailParam.setSubject("Order made successfully!");
			try {
				emailService.sendmail(emailParam, model);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		return order;
	}

	private void trackEntry(final OrderItem orderNew, List<TrackOrder> tarckOrderList) {
		// save tracking details
		TrackOrder trackOrder = new TrackOrder();
		trackOrder.setOrderItem(orderNew);
		trackOrder.setStatus("Ordered");
		trackOrder.setAction("Y");
		trackOrder.setMessage("Your order has been placed successfully.");
		tarckOrderList.add(trackOrder);
		
		TrackOrder packed = new TrackOrder();
		packed.setOrderItem(orderNew);
		packed.setStatus("Packed");
		packed.setAction("N");
		packed.setMessage("Your item yet to be packed."); 
		tarckOrderList.add(packed);
		
		TrackOrder shipped = new TrackOrder();
		shipped.setOrderItem(orderNew);
		shipped.setStatus("Shipped");
		shipped.setAction("N");
		shipped.setMessage("Your item yet to be shipped.");
		tarckOrderList.add(shipped);
		
		TrackOrder dilivery = new TrackOrder();
		dilivery.setOrderItem(orderNew);
		dilivery.setStatus("Delivered");
		dilivery.setAction("N");
		dilivery.setMessage("Your order yet to be delivered.");
		tarckOrderList.add(dilivery);
	}
	
	@PostMapping("updateStatus/{orderId}/{status}")
	public int cancelOrder(@PathVariable Long orderId, @PathVariable String status) {
		return orderRepository.updateStatus(status, orderId);
	}
	
	// uncompress the image bytes before returning it to the angular application
	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
	
	@RequestMapping(value = "/validateRazorPayment" , method=RequestMethod.POST)
	public Order validateRazorPayment(@RequestBody Order order) {
			
		String message = "SUCCESS";
		try {		
			if(order.getTotalAmount() != null && StringUtils.isNumeric(order.getTotalAmount()+"")){
				if(order.getTotalAmount() != null && Integer.parseInt(order.getTotalAmount()) > 0) {
					order.setValidStatus(message);
					return order;
				}
			}else {
				message = "Amount is not valid";
				order.setValidStatus(message);
				return order;
			}
						
			/*message = matchService.validateBonusCode(accountDTO);
			if(StringUtils.isNotEmpty(message)) {
				accountDTO.setStatus(message);
				return accountDTO;
			}else {
				accountDTO.setStatus("SUCCESS");
				return accountDTO;
			}*/
		}catch(Exception e) {
			order.setValidStatus("Error");
			return order;
		}
		return order;
	}
	@PutMapping("/updateOrderStatus")
	public int updateOrder(@RequestBody OrderItem orderItem) {
		if(!orderItem.getOldStatus().equalsIgnoreCase("Canceled") && orderItem.getStatus().equalsIgnoreCase("Refunded")) {
			return 0;
		}
		orderItemRepository.updateStatusAndComment(orderItem.getStatus(), orderItem.getComment(), orderItem.getCancelReason(), orderItem.getRefundStatus(), orderItem.getId());
		if("Canceled".equalsIgnoreCase(orderItem.getStatus())) {
			cancelOrderTrack(orderItem);
			return 1;
		}else {	
			List<TrackOrder> trackList = updateOrderTracker(orderItem);
			return trackList.size();
		}	
	}

	private List<TrackOrder> updateOrderTracker(OrderItem orderItem) {
		List<TrackOrder> trackList = new ArrayList<>();
		trackList = orderItem.getTrackList();	
		trackList.forEach(t->{
			t.setOrderItem(orderItem);
		});
		trackOrderRepository.saveAll(trackList);
		return trackList;
	}
	
	private int cancelOrderTrack(OrderItem orderItem) {
		List<TrackOrder> trackList = new ArrayList<>();
		trackList = orderItem.getTrackList();	
		boolean flag = false;
		TrackOrder trackOrder = new TrackOrder();
		if(trackList != null && trackList.size()>0) {
			for(TrackOrder t : trackList) {
				if(t.getStatus().equalsIgnoreCase("Canceled")) {
					flag = true;
					trackOrder = t;
					break;
				}
			}			
		}
		
		if((!orderItem.getOldStatus().equalsIgnoreCase("Canceled") || !orderItem.getOldStatus().equalsIgnoreCase("Refunded")) && flag == false) {
			TrackOrder track = new TrackOrder();
			track.setOrderItem(orderItem);
			track.setStatus(orderItem.getStatus());
			track.setAction("Y");
			track.setMessage(orderItem.getCancelReason());
			track.setComment(orderItem.getComment());
			trackOrderRepository.save(track);
			
			if(orderItem.getIsCashOn().equalsIgnoreCase("N")) {
				TrackOrder trackRefund = new TrackOrder();
				trackRefund.setOrderItem(orderItem);
				trackRefund.setStatus("Refunded");
				trackRefund.setAction("N");
				trackRefund.setMessage("Will verify your cancel request and update you.");
				trackRefund.setComment(orderItem.getComment());
				trackOrderRepository.save(trackRefund);
			}	
		}
		if(flag) {
			trackOrder.setOrderItem(orderItem);
			trackOrder.setStatus(orderItem.getStatus());
			trackOrder.setAction("Y");
			trackOrder.setMessage(orderItem.getCancelReason());
			trackOrder.setComment(orderItem.getComment());
			trackOrderRepository.save(trackOrder);
		}
		return 1;
	}
	
	
}
