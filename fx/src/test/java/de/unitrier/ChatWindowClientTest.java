package de.unitrier;

import de.unitrier.common.Handshake;
import de.unitrier.common.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ChatWindowClientTest {

    private ChatWindowClient chatWindowClient;
    private ObjectOutputStream mockOutputStream;
    private ObjectInputStream mockInputStream;
    private Socket mockSocket;


    @BeforeEach
    void setUp() throws IOException, ClassNotFoundException {
        // Erstelle Mock-Objekte
        mockSocket = mock(Socket.class);
        mockOutputStream = mock(ObjectOutputStream.class);
        mockInputStream = mock(ObjectInputStream.class);

        // Konfiguriere das Verhalten der Mocks
        when(mockSocket.isConnected()).thenReturn(true);
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
		when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("Test data".getBytes()));


        // Initialisiere ChatWindowClient und setze Mocks
        chatWindowClient = new ChatWindowClient();
        chatWindowClient.socket = mockSocket;
        chatWindowClient.clientOutputStream = mockOutputStream;
        chatWindowClient.clientInputStream = mockInputStream;
    }


    @Test
    void testSendMessage() throws IOException {
        // Nachrichteninhalt
        String messageText = "Test Message";

        // Sende die Nachricht
        chatWindowClient.sendMessage(messageText);

        // Überprüfen, ob die Nachricht korrekt über den OutputStream gesendet wurde
        verify(mockOutputStream, times(1)).writeObject(any(Message.class));
        verify(mockOutputStream, times(1)).flush();
    }
}
