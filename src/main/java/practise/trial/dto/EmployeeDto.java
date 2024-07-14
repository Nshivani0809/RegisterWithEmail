package practise.trial.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

	private String firstName;
	private String userName;
	private String password;
	private String email;
	private Boolean status;
	private String otp;
	private LocalDateTime otpGeneratedTiming;
}
