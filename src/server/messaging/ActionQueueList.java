package server.messaging;

import java.util.*;

// this is here to simplify the MessageBus. It's nice not to have
// several nested layers of generics
public class ActionQueueList<T extends ActionQueue<T>> {
    private final List<T> queues;
    
    public ActionQueueList() {
        queues = new ArrayList<>();
    }
    
    public void publish(Action<T> action) {
        for (T queue : queues) 
            queue.enqueue(action);
    }
    
    public void subscribe(T queue) {
        queues.add(queue);
    }
}
