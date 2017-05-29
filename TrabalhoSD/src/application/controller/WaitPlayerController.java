package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.sockets.FabricaSocket;
import com.sockets.Pacote;
import com.sockets.SocketGenerico;

import application.controller.thread.WaitingPlayerThread;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WaitPlayerController implements Initializable  {
	
	@FXML
	private Label ipPlayerLabel;
	
	@FXML
	private Button startButton;
	
	private boolean havePlayer;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		esperaJogador();
		iniciaThread();
	}
	
	public void addPlayer(Pacote p){
		havePlayer = true;
		startButton.setDisable(false);
		Platform.runLater(() -> {
			ipPlayerLabel.setText(p.getIp());
		});
	}
	
	public void startButtonAction(){
		try{
			SocketGenerico socket = new FabricaSocket().getSocket(FabricaSocket.SOCKET_TCP);
			Pacote pacote = new Pacote("start",ipPlayerLabel.getText().toString(),9999);
			socket.enviar(pacote);
			URL url = new URL("file:" + System.getProperty("user.dir") + 
					"/src/application/view/GameView.fxml");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(url);
			Parent game = loader.load();
			GameController controller = loader.getController();
			controller.setConfig(true,ipPlayerLabel.getText().toString());
			Scene scene = new Scene(game);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
			Stage s = (Stage) ipPlayerLabel.getScene().getWindow();
			s.close();
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public boolean isPlayer(){
		return havePlayer;
	}
	
	public void removePlayer(Pacote p){
		havePlayer = false;
		esperaJogador();
	}
	
	private void iniciaThread(){
		Thread t = new Thread(new WaitingPlayerThread(this));
		t.start();
	}
	
	private void esperaJogador(){
		havePlayer = false;
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				while(!havePlayer){
					if (ipPlayerLabel.getText().toString().length() > 3){
						Platform.runLater(()->{
							ipPlayerLabel.setText("");
						});
					}
					else{
						Platform.runLater(()->{
							ipPlayerLabel.setText(ipPlayerLabel.getText().toString()+".");
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
	

}
