package util;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String errorCode;
	private final String reason;

	private ApplicationException(String errorCode, String reason) {
		super(reason);
		this.errorCode = errorCode;
		this.reason = reason;
	}

	public static ApplicationException of(String errorCode, String reason) {
		return new ApplicationException(errorCode, reason);
	}

	public static ApplicationException of(String reason) {
		return new ApplicationException("", reason);
	}

	public static boolean is(Throwable throwable) {
		return throwable instanceof ApplicationException;
	}
}