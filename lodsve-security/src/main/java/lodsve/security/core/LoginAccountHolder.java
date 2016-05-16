package lodsve.security.core;

/**
 * 在当前请求（即一个线程中）中保存当前登录者.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @createTime 2014-12-7 16:50
 */
public class LoginAccountHolder {
    private static ThreadLocal<Account> accountThreadLocal = new ThreadLocal<>();

    public static Account getCurrentAccount() {
        return accountThreadLocal.get();
    }

    public static void setCurrentAccount(Account account) {
        accountThreadLocal.set(account);
    }
}
