package server.logging;

/**
 * Used so that threads don't step on each other's toes when logging
 */
public interface ThreadsafeLogger {
	/**
	 * Writes a line to the output stream
	 * @param id Id of who is logging
	 * @param message The message to write
	 */
	public void writeLine(String id, String message);
	
    /**
     * Prints an exception using the ThreadsafeLogger
     * @param id Id of who is logging
     * @param e The exception
     * @param where Where the exception occurred
     */
	public void handleException(String id, Exception e, String where);
}
