package ro.cloudSoft.cloudDoc.services.bpm.custom;

public class AutomaticTaskExecutionException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public AutomaticTaskExecutionException() {
		super();
	}
	
	public AutomaticTaskExecutionException(String message) {
		super(message);
	}
	
	public AutomaticTaskExecutionException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AutomaticTaskExecutionException(Throwable cause) {
		super(cause);
	}
}