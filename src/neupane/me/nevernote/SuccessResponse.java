package neupane.me.nevernote;

public class SuccessResponse {

	private int statusCode;
	private String status;

	public SuccessResponse() {

	}

	public SuccessResponse(int statusCode, String status) {
		this.statusCode = statusCode;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}


}
