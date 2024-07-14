package practise.trial.mapper;

import practise.trial.dto.EmployeeDto;
import practise.trial.entity.Employee;

public class EmployeeMapper {

	public static EmployeeDto mapEmpToDto(Employee employee) {

		return new EmployeeDto(employee.getFirstName(), employee.getUserName(),
				employee.getPassword(), employee.getEmail(),employee.getStatus(),employee.getOtp(),employee.getOtpGeneratedTiming());
	}

	public static Employee mapEmpToEntity(EmployeeDto employeedto) {

		return new Employee( 0, employeedto.getFirstName(), employeedto.getUserName(),
			 employeedto.getPassword(), employeedto.getEmail(),employeedto.getStatus() , employeedto.getOtp(), employeedto.getOtpGeneratedTiming());

	}

}
