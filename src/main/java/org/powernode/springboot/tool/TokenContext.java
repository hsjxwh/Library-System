package org.powernode.springboot.tool;
//获取并记录当前发送请求的管理员/用户的id
public final class TokenContext {
    private static final ThreadLocal<Long> currentId = new ThreadLocal<>();
    public static Long getCurrentId() {
        return currentId.get();
    }
    public static void setCurrentId(Long id) {
        currentId.set(id);
    }
    //清楚线程数据，以免干扰复用他的请求的结果
    public static void clear(){
        currentId.remove();
    }
}
