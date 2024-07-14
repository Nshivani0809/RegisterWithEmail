package practise.trial.service;

import practise.trial.dto.EmployeeDto;
import practise.trial.dto.LoginDto;
import practise.trial.payload.ResponseBase;

public interface EmployeeService {

	//EmployeeDto createEmployee(EmployeeDto employeeDto);

	ResponseBase register(EmployeeDto employeeDto);

	ResponseBase verifyAccount(String email, String otp);

	ResponseBase regenerateOtp(String email);

	ResponseBase login(LoginDto loginDto);

}
