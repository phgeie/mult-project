package de.unitrier;

import de.unitrier.common.Message;
import de.unitrier.common.Handshake;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

@Service
public class ServerConnection extends Thread {
    private final SimpMessagingTemplate messagingTemplate;
    private ObjectOutputStream clientOutputStream;
    private ObjectInputStream clientInputStream;
    private int chatWindowID;
    private Socket socket;
    private ArrayList<Message> messagesList;

    public ServerConnection(SimpMessagingTemplate messagingTemplate) {
	System.out.println("connection");
        this.messagingTemplate = messagingTemplate;
    }

    public void connect() {
        try {
            //socket = new Socket("localhost", 3321);
            socket = new Socket("frodo.uni-trier.de", 3321);
            clientOutputStream = new ObjectOutputStream(socket.getOutputStream());
            clientInputStream = new ObjectInputStream(socket.getInputStream());
            Handshake handshake = (Handshake) clientInputStream.readObject();
            chatWindowID = handshake.getId();

			messagesList = handshake.getMessagesList();
            start();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
	    @Override
	    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && socket.isConnected()) {
				System.out.println("reading");
				Object o = clientInputStream.readObject();
				if(o instanceof Message) {
					Message message = (Message) o;
					if(chatWindowID != message.getId()) {
						if(message.getId() == -2) {
							messagingTemplate.convertAndSend("/topic/clear", "");
						} else {
							messagingTemplate.convertAndSend("/topic/messages", message);
						}
					}
				} else {
					Handshake h = (Handshake) o;
					messagesList = h.getMessagesList();
					System.out.println("RECEIVE OLD MESSAGE LIST OF SIZE " + h.getMessagesList());
					System.out.println(h.getId());
					messagingTemplate.convertAndSend("/topic/clear", "");
					//restoreMessages();
					for (Message message : h.getMessagesList()) {
						messagingTemplate.convertAndSend("/topic/messages", message);
					}
				}

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && socket.isConnected()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	public void restoreMessages() {
	    System.out.println("SEND REQUEST");
	    messagingTemplate.convertAndSend("/topic/messages", new Message("###REQUEST_ALL_MESSAGES###", chatWindowID));
	}


    public void sendMessage(String text) {
        try {
            System.out.println(text);
            clientOutputStream.writeObject(new Message(text, chatWindowID));
            clientOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
