package de.unitrier;

import de.unitrier.common.Handshake;
import de.unitrier.common.Message;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatWindowClient extends Thread {

    public final VBox messageContainer = new VBox(5);
    public ObjectOutputStream clientOutputStream;
    public ObjectInputStream clientInputStream;
    public int chatWindowID;
    public Socket socket;

    public void createChatWindow() {
        BorderPane root = new BorderPane();

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("File");
        menuBar.getMenus().add(menu);

        menu.getItems().add(new MenuItem("Backup"));
        menu.getItems().add(new MenuItem("Restore"));
        root.setTop(menuBar);
        ScrollPane messageScroller = new ScrollPane(messageContainer);
        messageScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        messageScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageScroller.setPrefHeight(300);
        messageScroller.setFitToWidth(true);
        messageContainer.setPrefHeight(373);
        messageContainer.setStyle("-fx-background-color: #f0e7de;");

        HBox hBox = new HBox();
        TextField msgField = new TextField();
        Button sendButton = new Button("Send");
        sendButton.setMinWidth(50);

        sendButton.setOnAction(actionEvent -> {
            if (!msgField.getText().equals(""))
                sendMessage(msgField.getText());
            msgField.clear();
        });

        msgField.setPrefWidth(250);
        hBox.getChildren().add(msgField);
        hBox.getChildren().add(sendButton);
        root.setCenter(messageScroller);
        root.setBottom(hBox);

        Scene scene = new Scene(root, 300, 400);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (!msgField.getText().equals("")) {
                    sendMessage(msgField.getText());
                }
                msgField.clear();
            }
        });

        connect();
        Stage stage = new Stage();
        stage.setTitle("User " + chatWindowID);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && socket.isConnected()) {
				System.out.println("reading");
                Message message = (Message) clientInputStream.readObject();
                Platform.runLater(() -> displayReceiveMessage(message));
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

    public void displayReceiveMessage(Message msg) {
        String message;
        if (msg.getId() != chatWindowID) {
            message = "User " + msg.getId() + ": " + msg.getText();
        } else {
            message = msg.getText();
        }
        TextBubble msgBubble = new TextBubble(message, msg.getId(), chatWindowID);
        messageContainer.getChildren().add(msgBubble);
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

    public void connect() {
        try {
            //socket = new Socket("localhost", 3321);
            socket = new Socket("frodo.uni-trier.de", 3321);
            clientOutputStream = new ObjectOutputStream(socket.getOutputStream());
            clientInputStream = new ObjectInputStream(socket.getInputStream());
            Handshake handshake = (Handshake) clientInputStream.readObject();
            chatWindowID = handshake.getId();
            for (Message message : handshake.getMessagesList()) {
                displayReceiveMessage(message);
            }
            start();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
