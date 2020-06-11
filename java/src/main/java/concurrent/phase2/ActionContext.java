package concurrent.phase2;

public final class ActionContext {

    private static final ThreadLocal<Context> threadLocal = ThreadLocal.withInitial(Context::new);

    //单例
    private static class ContextHolder {
        private final static ActionContext actionContext = new ActionContext();
    }

    public static ActionContext getActionContext() {
        return ContextHolder.actionContext;
    }

    public Context getContext() {
        return threadLocal.get();
    }
}
