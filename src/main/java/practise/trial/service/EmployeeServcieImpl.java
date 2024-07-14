package practise.trial.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import practise.trial.Mail.Email;
import practise.trial.dto.EmployeeDto;
import practise.trial.dto.LoginDto;
import practise.trial.entity.Employee;
import practise.trial.payload.ResponseBase;
import practise.trial.repository.EmployeeRepo;
import practise.trial.utils.OtpUtils;

@Service
public class EmployeeServcieImpl implements EmployeeService {

	@Autowired
	private OtpUtils otpUtil;

	@Autowired
	private Email emailService;

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public ResponseBase register(EmployeeDto employeeDto) {
	    // Check if email already exists
		if (employeeDto.getEmail() == null || employeeDto.getEmail().isEmpty()) {
	        return new ResponseBase(false, "Email is required", "Admin");
	    }

	    // Check if email already exists
	    Optional<Employee> existingEmployeeOpt = employeeRepo.findByEmail(employeeDto.getEmail());
	    if (existingEmployeeOpt.isPresent()) {
	        return new ResponseBase(false, "Email already exists in the database", "Admin");
	    }

	    String otp = otpUtil.generateOTP();
	    try {
	        sendOtpEmail(employeeDto.getEmail(), otp);
	    } catch (MessagingException e) {
	        return new ResponseBase(false, "Unable to send OTP, please try again", "Admin", e.getMessage());
	    }

	    try {
	        Employee user = new Employee();
	        user.setFirstName(employeeDto.getFirstName());
	        user.setUserName(employeeDto.getUserName());
	        user.setEmail(employeeDto.getEmail());
	        String encryptedPassword = passwordEncoder.encode(employeeDto.getPassword());
	        user.setPassword(encryptedPassword);
	        user.setStatus(false);
	        user.setOtp(otp);
	        user.setOtpGeneratedTiming(LocalDateTime.now());
	        employeeRepo.save(user);
	        return new ResponseBase(true, "User registration successful", "Admin", user);
	    } catch (Exception e) {
	        return new ResponseBase(false, "User registration failed", "Admin", e.getMessage());
	    }
	}
	@Override
	public ResponseBase verifyAccount(String email, String otp) {
		try {
			Employee user = employeeRepo.findByEmail(email)
					.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
			if (user.getOtp().equals(otp)
					&& Duration.between(user.getOtpGeneratedTiming(), LocalDateTime.now()).getSeconds() < (5 * 60)) {
				user.setStatus(true);
				employeeRepo.save(user);
				return new ResponseBase(true, "OTP verified, you can login", "Admin", user);
			}
			return new ResponseBase(false, "Please regenerate OTP and try again", "Admin");
		} catch (Exception e) {
			return new ResponseBase(false, "Failed to verify account", "Admin", e.getMessage());
		}
	}

	@Override
	public ResponseBase regenerateOtp(String email) {
		try {
			Employee user = employeeRepo.findByEmail(email)
					.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
			String otp = otpUtil.generateOTP();
			try {
				sendOtpEmail(email, otp);
			} catch (MessagingException e) {
				return new ResponseBase(false, "Unable to send OTP, please try again", "Admin", e.getMessage());
			}
			user.setOtp(otp);
			user.setOtpGeneratedTiming(LocalDateTime.now());
			employeeRepo.save(user);
			return new ResponseBase(true, "OTP regenerated, please verify account within 1 minute", "Admin");
		} catch (Exception e) {
			return new ResponseBase(false, "Failed to regenerate OTP", "Admin", e.getMessage());
		}
	}

	@Override
	public ResponseBase login(LoginDto loginDto) {
		try {
			Employee user = employeeRepo.findByEmailOrUserName(loginDto.getEmail(), loginDto.getUserName())
					.orElseThrow(() -> new RuntimeException("User not found with this email or userName"));
			if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
				return new ResponseBase(false, "Password is incorrect", "Admin");
			}
			if (!user.getStatus()) {
				return new ResponseBase(false, "Your account is not verified", "Admin");
			}
			return new ResponseBase(true, "Login successful", "Admin", user);
		} catch (Exception e) {
			return new ResponseBase(false, "Failed to login", "Admin", e.getMessage());
		}
	}

	private void sendOtpEmail(String email, String otp) throws MessagingException {
		String subject = "Your OTP Code";
		String body = "Your OTP code is: " + otp;

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(body, "text/plain");

		MimeMultipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);

		try {
			emailService.sendTo(email, subject, multipart);
		} catch (Exception e) {
			throw new MessagingException("Failed to send email", e);
		}
	}

}
