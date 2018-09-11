package injectthedriver.interfaces;

public class RecoverableError extends Exception {
	private static final long serialVersionUID = 1L;

	public RecoverableError() {
		super();
	}

	public RecoverableError(String message, Throwable cause) {
		super(message, cause);
	}

	public RecoverableError(String message) {
		super(message);
	}

	public RecoverableError(Throwable cause) {
		super(cause);
	}

}