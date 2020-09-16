import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Listener implements EventHandler<ActionEvent> {
	private TextArea displayArea;
	private TextField entryField;
	private Connection connection;
	
	public Listener(TextArea displayArea, TextField entryField, Connection connection) {
		this.displayArea = displayArea;
		this.entryField = entryField;
		this.connection = connection;
	}
	
	public void handle(ActionEvent event) {
		try {
		String message = "";
		
		if (connection.isServer()) {
			message += "Server: ";
		} else {
			message += "Client: ";
		}
		
		message += entryField.getText();
		entryField.clear();
		
		displayArea.appendText(message + "\n");
		connection.sendMessage(message);
		} catch (Exception e) {
			System.err.println("Exception in handle() method of Listener class");
		}
	}
}