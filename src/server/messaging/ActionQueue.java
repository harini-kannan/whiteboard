package server.messaging;

/**
 * This is similar to the IComparable paradigm. A sample class definition is:
 * ClientHandler implements ActionQueue<ClientHandler> 
 * (in other words, ClientHandlers can queue ClientHandler actions)
 * 
 * see http://stackoverflow.com/questions/14083918/java-generic-comparable-idiom
 * for more about IComparable
 */
public interface ActionQueue<T extends ActionQueue<?>> {
    public void enqueue(Action<T> action);
}
