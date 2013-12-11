package server.requesthandlers;

/**
 *  Piece of a state machine that handles requests.
 *  
 *  OnEnter should be called when the state machine first changes to this object;
 *  OnLeave should be called when the state machine switches (or exits).
 *  
 *  These will have a ClientHandler in their constructor as well as a MessageBus.
 *  The MessageBus is used e.g. when a user draws a stroke--the MessageBus tells
 *  the all clients connected to the user's current whiteboard that a stroke has
 *  been drawn.
 *  
 *  These do not need to be threadsafe because they are only ever used as private
 *  state for the ClientHandler.
 */
public interface RequestHandler {
    // note: this may mutate the server and pass messages to the message bus
    // to send to the server
	public void handle(String request);
	
	public void onEnter();
	public void onLeave();
}
