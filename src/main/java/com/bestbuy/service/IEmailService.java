package com.bestbuy.service;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.bestbuy.model.EmailParam;
import com.bestbuy.model.MailResponse;
import com.bestbuy.model.OTPSystem;

public interface IEmailService {
	void sendmailOTP(EmailParam emailParam)
			throws AddressException, MessagingException, IOException;
	void sendmail(EmailParam emailParam, Map<String, Object> model)
			throws AddressException, MessagingException, IOException;
	MailResponse sendOTP(OTPSystem request, Map<String, Object> model);
}
