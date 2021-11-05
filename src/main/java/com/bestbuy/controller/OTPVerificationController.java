package com.bestbuy.controller;

/**
 * 
 */

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bestbuy.model.DAOUser;
import com.bestbuy.model.EmailParam;
import com.bestbuy.model.MailResponse;
import com.bestbuy.repository.UserRepository;
import com.bestbuy.service.IEmailService;
import com.bestbuy.service.IFast2sms;

/**
 * @author Viramm
 *
 */

@CrossOrigin(origins = {"*"}, maxAge = 3600)
@RestController
public class OTPVerificationController {
	
	@Autowired
	IEmailService emailService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	IFast2sms fast2sms;
	
	//@Autowired
	//private IUserService userService;
	
	private Map<String, EmailParam> otpData = new HashMap<>();
	//private final static String ACCOUNT_SID= "AC49760cfa045068324aa0cf3fce89bc62";
	//private final static String AUTH_TOKEN= "1f6a835b5f9859d7cf5feefea879b00a";
	/*
	@PostMapping(value="/sendMobileOTP/{mobileNumber}/otp")
	public ResponseEntity<Object> sendMobileOTP(@PathVariable("mobileNumber") String mobileNumber){
		if(StringUtils.isNotEmpty(mobileNumber)) {
			if(mobileNumber.length() != 10) {
				return new ResponseEntity<>("Mobile Number is not valid", HttpStatus.BAD_REQUEST);
			}else {
				UserDTO userDTO = new UserDTO();
				userDTO.setUserName(mobileNumber);
				int i = userService.validateDuplicateUser(userDTO);	
				if(i > 0) {
					if(StringUtils.isNotEmpty(userService.checkIfCompleteDetailExist(mobileNumber))) {
						return new ResponseEntity<>("Mobile number allready verified, please login now", HttpStatus.BAD_REQUEST);						
					}else {
						return new ResponseEntity<>("Mobile number allready verified, Please fill up all the details before login", HttpStatus.CONFLICT);
					}
				}
			}
		}
		
		if(otpData.containsKey(mobileNumber)) {
			OTPSystem otpSystemExisting = otpData.get(mobileNumber);
			if(otpSystemExisting.getExpiryTime() >= System.currentTimeMillis()) {
				return new ResponseEntity<>("OTP is already sent, resend OTP after 3 minute", HttpStatus.BAD_REQUEST);
			}
		}
		
		OTPSystem otpSystem = new OTPSystem();
		otpSystem.setMobileNumber(mobileNumber);
		otpSystem.setOtp(String.valueOf(((int)(Math.random()*(10000 - 1000))) + 1000));
		otpSystem.setExpiryTime(System.currentTimeMillis() + 180000);
		otpData.put(mobileNumber, otpSystem);
		String response = fast2sms.sendOTP(mobileNumber, otpSystem.getOtp());
		System.out.println("message sent :: "+response);
		//Message.creator(new PhoneNumber("+91"+mobileNumber+""), new PhoneNumber("+13342316476"), "Your OTP is "+otpSystem.getOtp()+" Please use this OTP to verify your mobile number with Striker11, OTP will expire in 5 minutes.").create();
		return new ResponseEntity<>("OTP sent successfully. OTP will expire in 3 minutes.", HttpStatus.OK);
	}
	
	@PostMapping(value="/verifyMobileOTP/{mobileNumber}/otp")
	public ResponseEntity<Object> verifyMobileOTP(@PathVariable("mobileNumber") String mobileNumber, @RequestBody OTPSystem requestOTPSystem){
		
		if(StringUtils.isNotEmpty(mobileNumber)) {
			if(mobileNumber.length() != 10) {
				return new ResponseEntity<>("Mobile Number is not valid", HttpStatus.BAD_REQUEST);
			}else {
				UserDTO userDTO = new UserDTO();
				userDTO.setUserName(mobileNumber);
				int i = userService.validateDuplicateUser(userDTO);	
				if(i > 0) {
					if(StringUtils.isNotEmpty(userService.checkIfCompleteDetailExist(mobileNumber))) {
						return new ResponseEntity<>("Mobile number allready verified, please login now", HttpStatus.BAD_REQUEST);						
					}else {
						return new ResponseEntity<>("Mobile number allready verified, Please fill up all the details before login", HttpStatus.CONFLICT);
					}
				}
			}
		}
		
		if(StringUtils.isEmpty(requestOTPSystem.getOtp())){
			return new ResponseEntity<>("Please provide OTP", HttpStatus.BAD_REQUEST);
		}
		if (otpData.containsKey(mobileNumber)) {
			OTPSystem otpSystem = otpData.get(mobileNumber);
			if (otpSystem != null) {
				if(otpSystem.getExpiryTime() >= System.currentTimeMillis()) {
					if(requestOTPSystem.getOtp().equalsIgnoreCase(otpSystem.getOtp())) {
						int userInserted = userService.createUserInitially(mobileNumber);
						System.out.println("user inserted: "+userInserted);
						return new ResponseEntity<>("OTP verified successfully", HttpStatus.OK);
					}
					return new ResponseEntity<>("Invalid OTP", HttpStatus.BAD_REQUEST);
				}
				return new ResponseEntity<>("OTP is expired", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("Somthing went wrong..!!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Mobile number not found", HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping(value="/sendMobileOTPForPass/{mobileNumber}/otp")
	public ResponseEntity<Object> sendMobileOTPForPass(@PathVariable("mobileNumber") String mobileNumber){
		if(StringUtils.isNotEmpty(mobileNumber)) {
			if(mobileNumber.length() != 10) {
				return new ResponseEntity<>("Mobile Number is not valid", HttpStatus.BAD_REQUEST);
			}
		}
		
		if(otpData.containsKey(mobileNumber)) {
			OTPSystem otpSystemExisting = otpData.get(mobileNumber);
			if(otpSystemExisting.getExpiryTime() >= System.currentTimeMillis()) {
				return new ResponseEntity<>("OTP is already sent, resend OTP after 3 minute", HttpStatus.BAD_REQUEST);
			}
		}
		
		OTPSystem otpSystem = new OTPSystem();
		otpSystem.setMobileNumber(mobileNumber);
		otpSystem.setOtp(String.valueOf(((int)(Math.random()*(10000 - 1000))) + 1000));
		otpSystem.setExpiryTime(System.currentTimeMillis() + 180000);
		otpData.put(mobileNumber, otpSystem);
		String response = fast2sms.sendOTP(mobileNumber, otpSystem.getOtp());
		System.out.println("message sent :: "+response);
		//Message.creator(new PhoneNumber("+91"+mobileNumber+""), new PhoneNumber("+13342316476"), "Your OTP is "+otpSystem.getOtp()+" Please use this OTP to verify your mobile number with Striker11, OTP will expire in 5 minutes.").create();
		return new ResponseEntity<>("OTP sent successfully. OTP will expire in 3 minutes.", HttpStatus.OK);
	}
	
	@PostMapping(value="/verifyMobileOTPForPass/{mobileNumber}/otp")
	public ResponseEntity<Object> verifyMobileOTPForPass(@PathVariable("mobileNumber") String mobileNumber, @RequestBody OTPSystem requestOTPSystem){
		
		if(StringUtils.isNotEmpty(mobileNumber)) {
			if(mobileNumber.length() != 10) {
				return new ResponseEntity<>("Mobile Number is not valid", HttpStatus.BAD_REQUEST);
			}
		}
		
		if(StringUtils.isEmpty(requestOTPSystem.getOtp())){
			return new ResponseEntity<>("Please provide OTP", HttpStatus.BAD_REQUEST);
		}
		if (otpData.containsKey(mobileNumber)) {
			OTPSystem otpSystem = otpData.get(mobileNumber);
			if (otpSystem != null) {
				if(otpSystem.getExpiryTime() >= System.currentTimeMillis()) {
					if(requestOTPSystem.getOtp().equalsIgnoreCase(otpSystem.getOtp())) {
						return new ResponseEntity<>("OTP verified successfully", HttpStatus.OK);
					}
					return new ResponseEntity<>("Invalid OTP", HttpStatus.BAD_REQUEST);
				}
				return new ResponseEntity<>("OTP is expired", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("Somthing went wrong..!!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Mobile number not found", HttpStatus.BAD_REQUEST);
	}*/
	
	@PostMapping("/sendEmailOTP/{email}")
	public MailResponse sendEmailOTP(@PathVariable String email) {
		
		DAOUser user = userRepository.findByUsername(email);
		if(user == null || user.getEmail() == null) {
			return new MailResponse("Email is required", HttpStatus.NOT_FOUND);
		}
		
		if(StringUtils.isNotEmpty(email)) {
			if(email.contains("@") && email.contains(".")) {
			}else {
				return new MailResponse("Email is not valid", HttpStatus.BAD_REQUEST);
			}
		}else{
			return new MailResponse("Email is not valid", HttpStatus.BAD_REQUEST);
		}
		
		if(otpData.containsKey(email)) {
			EmailParam otpSystemExisting = otpData.get(email);
			if(otpSystemExisting.getExpiryTime() >= System.currentTimeMillis()) { 
				return new MailResponse("OTP is already sent to "+email+", resend OTP after 3 minute", HttpStatus.BAD_REQUEST);
			}
		}
		
		EmailParam emailParam = new EmailParam();
		emailParam.setEmail(email);
		emailParam.setOtp(String.valueOf(((int)(Math.random()*(10000 - 1000))) + 1000));
		emailParam.setExpiryTime(System.currentTimeMillis() + 180000);
		emailParam.setEmailBody("Please use this OTP for verification: "+emailParam.getOtp()+"");
		emailParam.setSubject("OTP for eMart Password reset");
		otpData.put(email, emailParam);
		try {
			emailService.sendmailOTP(emailParam);
			return new MailResponse("Email sent to : " + email, HttpStatus.OK);
		} catch (Exception e) {
			return new MailResponse("exception while sending email to : " + email+" error:"+e.getMessage(), HttpStatus.BAD_REQUEST);
        }
	}
	
	@PostMapping(value="/verifyEmailOTP/{email}/{otp}")
	public MailResponse verifyEmailOTP(@PathVariable("email") String email,@PathVariable("otp") String otp){
		
		if(StringUtils.isEmpty(otp)){
			return new MailResponse("Please provide OTP", HttpStatus.BAD_REQUEST);
		}
		if (otpData.containsKey(email)) {
			EmailParam emailParam = otpData.get(email);
			if (emailParam != null) {
				if(emailParam.getExpiryTime() >= System.currentTimeMillis()) {
					if(otp.equalsIgnoreCase(emailParam.getOtp())) {
						return new MailResponse("OTP is verified successfully", HttpStatus.OK);
					}
					return new MailResponse("Invalid OTP", HttpStatus.BAD_REQUEST);
				}
				return new MailResponse("OTP is expired", HttpStatus.BAD_REQUEST);
			}
			return new MailResponse("Somthing went wrong..!!", HttpStatus.BAD_REQUEST);
		}
		return new MailResponse("Email not found", HttpStatus.BAD_REQUEST);
	}
}
