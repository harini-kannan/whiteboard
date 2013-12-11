package server.messaging;

import java.util.*;

/**
 * Used to simplify the MessageBus so that we don't end up having
 * several nested layers of generics.
 * 
 * This isn't threadsafe, but it's only ever used as private state
 * for the message bus, so that isn't important.
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
