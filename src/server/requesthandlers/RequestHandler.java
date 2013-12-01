package server.requesthandlers;

public interface RequestHandler {
    // note: this may mutate the server and pass messages to the message bus
    // to send to the server
	public void handle(String request);
	
	public void onEnter();
	public void onLeave();
}
