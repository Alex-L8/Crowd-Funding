package lcx.exception;

/**
 * 登录失败时抛出的异常
 * Create by LCX on 2/6/2022 3:34 PM
 */
public class LoginFailedException extends RuntimeException {
    private static final long serialVersionUID = -340745764126939L;

    public LoginFailedException() {
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }

    public LoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
