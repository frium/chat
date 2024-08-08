package top.frium.context;


public class BaseContext {
    // 使用 ThreadLocal 存储当前线程的用户 ID
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的用户 ip
     */
    public static void setCurrentIp(String ip) {
        threadLocal.set(ip);
    }

    public static String getCurrentIp()  {
        return threadLocal.get();
    }

}
