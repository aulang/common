package cn.aulang.common.auth;

/**
 * @author wulang
 */
public class UserContext {

    private static final ThreadLocal<CurrentUser> THREAD_LOCAL = ThreadLocal.withInitial(CurrentUser::new);

    public static void set(CurrentUser user) {
        THREAD_LOCAL.set(user);
    }

    public static CurrentUser get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
