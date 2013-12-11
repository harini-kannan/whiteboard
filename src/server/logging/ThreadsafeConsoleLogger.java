package server.logging;

/**
 * This class is threadsafe because a single instance is passed around and
 * logged from.
 */
public class ThreadsafeConsoleLogger implements ThreadsafeLogger {
	@Override
	public void writeLine(String id, String message) {
		synchronized(this) {
			System.out.println(String.format("%s: %s", id, message));
		}
	}

	@Override
	public void handleException(String id, Exception e, String where) {
		synchronized(this) {
			System.out.println(String.format("%s: Exception %s", id, where));
			e.printStackTrace(System.out);
		}
	}
}
