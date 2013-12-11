package server.messaging;

import java.util.*;

// this is here to simplify the MessageBus. It's nice not to have
// several nested layers of generics
/**
 * Used to simplify the MessageBus so that we don't end up having
 * several nested layers of generics.
 *
 * @param <T> The type of the list (e.g. ClientHandler)
 */
public class ActionQueueList<T extends ActionQueue<T>> {
    private final List<T> queues;
    
    public ActionQueueList() {
        queues = new ArrayList<>();
    }
    
    public void publish(Action<T> action) {
        for (T queue : queues) 
            queue.enqueue(action);
    }
    
    public int size() {
    	return queues.size();
    }
    
    public void subscribe(T queue) {
        queues.add(queue);
    }
    
    public void unsubscribe(T queue) {
    	queues.remove(queue);
    }
}
