import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

public class ChatClient extends Application {
	
	private boolean isServer = false;
	
	private TextArea displayArea;
	private TextField entryField;
	private Connection connection = createConnection();
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		connection.startConnection();
		
		Scene scene = new Scene(createScene());
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("ChatterBox");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	@Override
	public void stop() {
		connection.endConnection();
	}
	
	private Parent createScene() {
		displayArea = new TextArea();
		displayArea.setPrefHeight(300);
		displayArea.setEditable(false);
		
		entryField = new TextField();
		entryField.setPrefHeight(30);
		entryField.setOnAction(new Listener(displayArea, entryField, connection));
		
		VBox root = new VBox(20, displayArea, entryField);
		root.setPrefSize(330, 345);
		return root;
	}
	
	private Connection createConnection() {
		if (isServer) {
			return new Connection(true, message -> {
				Platform.runLater(() -> {
					displayArea.appendText(message);
				});
			});
		} else {
			return new Connection(false, message -> {
				Platform.runLater(() -> {
					displayArea.appendText(message);
				});
			});
		}
	}
}