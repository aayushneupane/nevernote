package neupane.me.errorHandler;

public class IdNotFoundException extends RuntimeException {

	public IdNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IdNotFoundException(String arg0) {
		super(arg0);
	}

	public IdNotFoundException(Throwable arg0) {
		super(arg0);
	}

}
