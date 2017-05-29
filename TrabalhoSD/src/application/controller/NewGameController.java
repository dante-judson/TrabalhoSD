package application.controller;

import java.io.IOException;
import java.net.URL;

import javax.xml.bind.annotation.XmlList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class NewGameController {

	@FXML
	private CheckBox entrarCheckBox;

	@FXML
	private CheckBox iniciarCheckBox;

	@FXML
	private TextField ipServidorField;

	@FXML
	private Button startButton;

	public void entrarAction() {
		iniciarCheckBox.setSelected(false);
		ipServidorField.setEditable(true);
	}

	public void iniciarAction() {
		entrarCheckBox.setSelected(false);
		ipServidorField.setEditable(false);
		ipServidorField.setText("");
	}

	public void startButtonAction() {
			try {
				URL url;
				if(iniciarCheckBox.isSelected()){
						url = new URL("file:" + System.getProperty("user.dir") + 
								"/src/application/view/WaitPlayerView.fxml");
					AnchorPane waitPlayer = (AnchorPane) FXMLLoader.load(url);
					
					Scene scene = new Scene(waitPlayer);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.show();
					Stage s = (Stage) startButton.getScene().getWindow();
					s.close();
				} else {
					url = new URL("file:" + System.getProperty("user.dir") + 
								"/src/application/view/JoinGameView.fxml");
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(url);
					Parent joinGame = loader.load();
					JoinGameController controller = (JoinGameController) loader.getController();
					controller.setIpServer(ipServidorField.getText().toString());
					Scene scene = new Scene(joinGame);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.show();
					Stage s = (Stage) startButton.getScene().getWindow();
					s.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
}
