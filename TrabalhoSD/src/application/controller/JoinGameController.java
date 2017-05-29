package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.controller.thread.EsperaInicioPartidaThread;
import application.controller.thread.JoinGameThread;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class JoinGameController implements Initializable {

	@FXML
	private Label msgLabel;
	
	@FXML
	private Label waitLabel;
	
	private boolean joinedGame;
	
	private String ipServer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try{
			esperaConexao();
			msgLabel.setText("Conectando Servidor ...");
			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void setjoinedGame(boolean join){
		joinedGame = join;
		Platform.runLater(() -> {
			msgLabel.setText("Esperando Inicio da Partida");
		});
		esperarInicioPartida();
	}
	
	private void esperarInicioPartida(){
		Thread t = new Thread(new EsperaInicioPartidaThread(this));
		t.start();
	}

	public void start(){
		Platform.runLater(() -> {
			try{
				URL url = new URL("file:" + System.getProperty("user.dir") + 
						"/src/application/view/GameView.fxml");
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(url);
				Parent game = loader.load();
				GameController controller = loader.getController();
				controller.setConfig(false, null);
				Scene scene = new Scene(game);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.show();
				Stage s = (Stage) msgLabel.getScene().getWindow();
				s.close();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
		});
		
	}
	
	private void esperaConexao(){
		joinedGame = false;
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				boolean ok = true;
				while(ok){
					if (waitLabel.getText().toString().length() > 3){
						Platform.runLater(()->{
							waitLabel.setText("");
						});
					}
					else{
						Platform.runLater(()->{
							waitLabel.setText(waitLabel.getText().toString()+".");
						});
					}
					Thread.sleep(1000);
				}
				return null;
			}
		};
		Thread t = new Thread(task);
		t.start();		
	}
	
	public void setIpServer(String ipServer){
		this.ipServer = ipServer;
		System.out.println("controller ip: "+ipServer);
		startThreadJoin();
	}
	
	private void startThreadJoin(){
		Thread t = new Thread(new JoinGameThread(this, ipServer));
		t.start();
	}
	
	
}
