package concurrent.phase2;

import concurrent.phase2.Subject2.RunnableEvent;

public interface Observer2 {
    void onEvent(RunnableEvent event);
}
