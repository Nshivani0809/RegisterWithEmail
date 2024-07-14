package practise.trial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import practise.trial.dto.EmployeeDto;
import practise.trial.dto.LoginDto;
import practise.trial.payload.ResponseBase;
import practise.trial.service.EmployeeService;

@RestController
@RequestMapping("/api")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	/*
	 * @PostMapping("/register") public ResponseEntity<ResponseBase>
	 * registerEmployee(@RequestBody EmployeeDto employeeDto) { try { String Otp
	 * =utils.generateOTP(); // Register the employee EmployeeDto registeredEmployee
	 * = employeeService.CreateEmployee(employeeDto);
	 * 
	 * // Prepare email content String subject = "Registration Successful"; String
	 * body = "You have registered as a user in Shivani Enterprises.";
	 * 
	 * // Send the email using Email service
	 * emailService.sendSimpleMessage(registeredEmployee.getEmail(), subject, body);
	 * 
	 * // Create response ResponseBase response = new ResponseBase(true,
	 * "Registration successful", "user");
	 * response.setResponseObject(registeredEmployee);
	 * 
	 * return ResponseEntity.ok().body(response); } catch (Exception e) {
	 * e.printStackTrace(); ResponseBase response = new ResponseBase(false,
	 * "Registration failed: " + e.getMessage()); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); } }
	 */

	@PostMapping("/register")
	public ResponseEntity<ResponseBase> register(@RequestBody EmployeeDto employeeDto) {
		return new ResponseEntity<>(employeeService.register(employeeDto), HttpStatus.OK);
	}

	@PutMapping("/verify-account")
	public ResponseEntity<ResponseBase> verifyAccount(@RequestParam String email, @RequestParam String otp) {
		return new ResponseEntity<>(employeeService.verifyAccount(email, otp), HttpStatus.OK);
	}

	@PutMapping("/regenerate-otp")
	public ResponseEntity<ResponseBase> regenerateOtp(@RequestParam String email) {
		return new ResponseEntity<>(employeeService.regenerateOtp(email), HttpStatus.OK);
	}

	@PutMapping("/login")
	public ResponseEntity<ResponseBase> login(@RequestBody LoginDto loginDto) {
		return new ResponseEntity<>(employeeService.login(loginDto), HttpStatus.OK);
	}
}
