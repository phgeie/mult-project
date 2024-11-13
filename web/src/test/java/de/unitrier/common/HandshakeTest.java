package de.unitrier.common;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HandshakeTest {

    @Test
    void testConstructorAndGetId() {
        // Erzeuge ein Handshake-Objekt mit ID 1 und leerer Nachrichtenliste
        Handshake handshake = new Handshake(1, new ArrayList<>());

        // Überprüfe, ob die ID korrekt gesetzt wurde
        assertEquals(1, handshake.getId(), "The ID should be 1.");
    }

    @Test
    void testConstructorAndGetMessagesList() {
        // Erzeuge eine Beispielnachrichtenliste
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("Test message 1", 1));
        messages.add(new Message("Test message 2", 2));

        // Erzeuge das Handshake-Objekt mit der Nachrichtenliste
        Handshake handshake = new Handshake(1, messages);

        // Überprüfe, ob die Nachrichtenliste korrekt gesetzt wurde
        assertEquals(2, handshake.getMessagesList().size(), "The messages list should contain 2 messages.");
        assertEquals("Test message 1", handshake.getMessagesList().get(0).getText(), "The first message should be 'Test message 1'.");
        assertEquals("Test message 2", handshake.getMessagesList().get(1).getText(), "The second message should be 'Test message 2'.");
    }

    @Test
    void testEmptyMessagesList() {
        // Erzeuge ein Handshake-Objekt mit leerer Nachrichtenliste
        Handshake handshake = new Handshake(1, new ArrayList<>());

        // Überprüfe, ob die Nachrichtenliste leer ist
        assertNotNull(handshake.getMessagesList(), "The messages list should not be null.");
        assertEquals(0, handshake.getMessagesList().size(), "The messages list should be empty.");
    }
}
