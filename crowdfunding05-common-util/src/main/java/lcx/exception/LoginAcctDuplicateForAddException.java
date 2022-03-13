package lcx.exception;

/**
 *  新增Admin时如果检测到账号重复抛出这个异常
 * Create by LCX on 2/7/2022 10:10 PM
 */
public class LoginAcctDuplicateForAddException extends RuntimeException{
    private static final long serialVersionUID = -40348719057666693L;
    public LoginAcctDuplicateForAddException() {
        super();
    }

    public LoginAcctDuplicateForAddException(String message) {
        super(message);
    }

    public LoginAcctDuplicateForAddException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctDuplicateForAddException(Throwable cause) {
        super(cause);
    }

    protected LoginAcctDuplicateForAddException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
