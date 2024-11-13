package de.unitrier;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import de.unitrier.common.Message;
import de.unitrier.common.Handshake;
import de.unitrier.ServerConnection;


@Controller
public class HelloController {

	private final ServerConnection serverConnection;

        @Autowired
        public HelloController(ServerConnection serverConnection) {
			System.out.println("Loading connection");
            this.serverConnection = serverConnection;
            this.serverConnection.connect(); 
        }

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

   	@GetMapping("/chat")
    public String chatPage() {
        return "chat"; 
    }

    @MessageMapping("/send")
    public void send(String message) {
        serverConnection.sendMessage(message);
    }
	
	@MessageMapping("/requestMessages")
    public void send() {
        serverConnection.restoreMessages();
    }
}