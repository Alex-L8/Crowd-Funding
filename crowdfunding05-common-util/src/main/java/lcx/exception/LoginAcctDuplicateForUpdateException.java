package lcx.exception;

/**
 *  更新Admin时如果检测到账号重复抛出这个异常
 * Create by LCX on 2/7/2022 10:10 PM
 */
public class LoginAcctDuplicateForUpdateException extends RuntimeException{
    private static final long serialVersionUID = -403487111576693L;
    public LoginAcctDuplicateForUpdateException() {
        super();
    }

    public LoginAcctDuplicateForUpdateException(String message) {
        super(message);
    }

    public LoginAcctDuplicateForUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctDuplicateForUpdateException(Throwable cause) {
        super(cause);
    }

    protected LoginAcctDuplicateForUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
