package me.andy5.util.concurrent.test;

/**
 * 日志处理类
 *
 * @author andy(Andy)
 * @datetime 2018-08-27 16:46 GMT+8
 * @email 411086563@qq.com
 */
public class Log {

    /**
     * 获取写日志类
     *
     * @param clazz
     * @return
     */
    public static final Log getLog(Class clazz) {
        Log log = new Log();
        if (clazz != null) {
            log.prefix = clazz.getName();
        }
        return log;
    }

    private String prefix = "";

    private Log() {
    }

    /**
     * 调试信息
     *
     * @param msg
     */
    public void debug(String msg) {
        System.out.println(prefix + " - " + msg);
    }
}
