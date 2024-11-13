package de.unitrier;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.stage.Stage;
import javafx.scene.Scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextBubbleTest extends ApplicationTest {

    @Test
    void testMessageContent() {
        String message = "Hello, World!";
        int messageID = 1;
        int ownID = 1;
        
        TextBubble textBubble = new TextBubble(message, messageID, ownID);
        Label displayedText = (Label) textBubble.getChildren().get(0);
        
        assertEquals("Hello, World!", displayedText.getText(), "The message content should match the input text.");
    }
}
