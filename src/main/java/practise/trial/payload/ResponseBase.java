package practise.trial.payload;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseBase implements Serializable {

	private boolean status = false;
	private String message;
	private String userType;

	// This will be helpful when any generic object needs to sent in response
	private Object responseObject;

	public ResponseBase(boolean successFlag, String messageParam) {
		this.status = successFlag;
		this.message = messageParam;
	}

	public ResponseBase(boolean status, String message, String userType) {
		this.status = status;
		this.message = message;
		this.userType = userType;
	}

}
