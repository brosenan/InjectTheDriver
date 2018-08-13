package injectthedriver;

public class DriverFactoryException extends Exception {
	private static final long serialVersionUID = 1L;

	public DriverFactoryException(Exception e) {
		super(e);
	}

	public DriverFactoryException(String s) {
		super(s);
	}

}
