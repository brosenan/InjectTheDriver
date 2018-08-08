package injectthedriver.interfaces;

import java.io.IOException;

public interface QueueService {
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
	/**
	 * A callback interface for receiving data from the queue
	 */
	public interface Callback {
		/**
		 * Handles a task coming from the queue.
		 * Successful completion of this function implies the task has been fully processed,
		 * and can be safely removed from the queue.
		 * @param data A binary representation of the task. It is up to the user to define the format.
		 * @throws RecoverableError Processing failed, but should be retried.
		 * @throws UnrecoverableError Processing failed and should not be retried.
		 */
		void handleTask(byte[] data) throws RecoverableError, UnrecoverableError;
	}
	/**
	 * An interface for allowing a service to be stopped.
	 */
	public interface Stopable {
		/**
		 * Stop the service. In case of the object returned from register(),
		 * stops receiving tasks.
		 */
		void stop();
	}
	/**
	 * Places a task (serialized in some arbitrary format as a byte array) in the queue.
	 * If successful, this guarantees that at one point handleTask() will be called 
	 * for at least one subscriber.
	 * @param data The task serialized as a byte array.
	 * @throws IOException Something went wrong, and the task may have not been enqueued.
	 */
	void enqueue(Byte[] data) throws IOException;
	
	/**
	 * Start subscribing to tasks.
	 * The given callback will be invoked with incoming tasks.
	 * @param cb Will be called for tasks fetched from the queue.
	 * @return A Stopable, allowing the subscription to be canceled.
	 * @throws IOException Something went wrong. The callback will not be called.
	 */
	Stopable register(Callback cb) throws IOException;
}
