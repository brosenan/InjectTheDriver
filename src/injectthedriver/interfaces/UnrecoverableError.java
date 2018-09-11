package injectthedriver.interfaces;

public class UnrecoverableError extends Exception {
	private static final long serialVersionUID = 1L;

	public UnrecoverableError() {
		super();
	}

	public UnrecoverableError(String message, Throwable cause) {
		super(message, cause);
	}

	public UnrecoverableError(String message) {
		super(message);
	}

	public UnrecoverableError(Throwable cause) {
		super(cause);
	}

}