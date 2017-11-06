package lodsve.security.core;

/**
 * 在当前请求（即一个线程中）中保存当前登录者.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-7 16:50
 */
public class LoginAccountHolder {
    private final static ThreadLocal<Account> ACCOUNT_THREAD_LOCAL = new ThreadLocal<>();

    public static Account getCurrentAccount() {
        return ACCOUNT_THREAD_LOCAL.get();
    }

    public static void setCurrentAccount(Account account) {
        ACCOUNT_THREAD_LOCAL.set(account);
    }

    public static void removeCurrentAccount() {
        ACCOUNT_THREAD_LOCAL.remove();
    }
}
