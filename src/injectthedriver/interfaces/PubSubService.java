package injectthedriver.interfaces;

import java.io.IOException;

import injectthedriver.interfaces.PubSubService.Callback;

/**
 * A service implenting this interface provides publish/subscribe capabilities.
 * 
 * Publishing and subscribing are done on topics, which are identified as strings.
 * Topics do not need to be defined prior to usage.
 */
public interface PubSubService {
	public interface Callback {
		/**
		 * Handle a message, provided by a subscription.
		 * 
		 * @param topic The topic on which the message has been received.
		 * @param message The message itself, as a byte array.
		 * @throws RecoverableError In case there was an error processing the message, one that a retry (on the same subscriber) can solve.
		 * @throws UnrecoverableError In case an of an error processing the message, one that is unlikely to be fixed when retried.
		 */
		void handleMessage(String topic, byte[] message) throws RecoverableError, UnrecoverableError;
	}

	/**
	 * This method publishes a message on a topic.
	 * It returns after the message has been successfully been published, 
	 * but potentially before all subscribers have received it.
	 * 
	 * @param topic The topic on which the message is to be published.
	 * @param message The message to publish.
	 * @throws IOException In case the message could not be published for some reason, or lack of certainty that the message has been published.
	 */
	void publish(String topic, byte[] message) throws IOException;
	
	/**
	 * Starts subscribing to a topic. 
	 * Subscription is done on a different thread, so this function returns immediately.
	 * 
	 * @param topic The topic to subscribe to.
	 * @param callback A callback to be invoked on each received message.
	 * @return A Stoppable object, allowing for the subscription to be stopped.
	 * @throws IOException In case the subscription failed.
	 */
	Stoppable subscribe(String topic, Callback callback) throws IOException;
}
