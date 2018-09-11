package injectthedriver.interfaces;

/**
 * An interface for allowing a service to be stopped.
 */
public interface Stoppable {
	/**
	 * Stop the service. In case of the object returned from register(),
	 * stops receiving tasks.
	 */
	void stop();
}