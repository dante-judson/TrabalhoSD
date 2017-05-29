package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.sockets.FabricaSocket;
import com.sockets.Pacote;
import com.sockets.SocketGenerico;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GameController implements Initializable{

	@FXML
	private TextField animalField;
	
	@FXML
	private TextField pessoaField;
	
	@FXML
	private TextField cidadeField;
	
	@FXML
	private TextField frutaField;
	
	@FXML
	private TextField paisField;
	
	@FXML
	private TextField marcaCarroField;
	
	@FXML
	private Label timeLabel;
	
	@FXML
	private Label letraLabel;
	
	private boolean isServer;
	private int time;
	private String letraEscolhida;
	private String ipCliente;
	private String ipServer;
	private String respostaLocal;
	private String respostaAdversario;
	private String vencedor;
	private int pontuacaoLocal;
	private int pontuacaoAdv;
	
	public void trocarRespostas(){
			try{
				if(isServer){
					SocketGenerico socket = new FabricaSocket().getSocket(FabricaSocket.SOCKET_TCP);
					Pacote p = socket.receber(9999);
					respostaAdversario = (String) p.getConteudo();
					
				} else {
					SocketGenerico socket = new FabricaSocket().getSocket(FabricaSocket.SOCKET_TCP);
					Pacote p = new Pacote(respostaLocal,ipServer,9999);
					Thread.sleep(2000);
					socket.enviar(p);
				}
			} catch (Exception e) {
				// TODO: handle exceptio
				e.printStackTrace();
			}
			System.out.println();
		gerarPontuacao();
	}
	
	private void gerarPontuacao(){
		try{
		if(isServer){
			System.out.println("local"+respostaLocal+"adv"+respostaAdversario);
			String[] local = respostaLocal.split(":");
			String[] adv = respostaAdversario.split(":");
			System.out.println("tamlocal: "+local.length);
			System.out.println("tamadv: "+adv.length);
			pontuacaoLocal = 0;
			pontuacaoAdv = 0;
			for (int i = 0; i < 6; i++){
				System.out.println("pontuacao Rodada"+i);
				System.out.println(pontuacaoLocal+"x"+pontuacaoAdv);
				if(local[i].equalsIgnoreCase(" ")){
					pontuacaoLocal += 0;
				}
				else {
					if(local[i].equalsIgnoreCase(adv[i])){
						pontuacaoLocal += 5;
						pontuacaoAdv += 5;
					} else {
						pontuacaoLocal += 10;
					}
						
				}
				if(adv[i].equalsIgnoreCase(" ")){
					pontuacaoAdv +=0;
				} else {
					if(!adv[i].equalsIgnoreCase(local[i])){
						pontuacaoAdv += 10;
					}
				}
			}
			if(pontuacaoLocal>pontuacaoAdv)
				vencedor = "server";
			else
				vencedor = "client";
			chamarTela();
			SocketGenerico socket = new FabricaSocket().getSocket(FabricaSocket.SOCKET_TCP);
			Pacote p = new Pacote(vencedor+":"+pontuacaoAdv,ipCliente,9999);
			Thread.sleep(2000);
			socket.enviar(p);
			chamarTela();
		} else {
			SocketGenerico socket = new FabricaSocket().getSocket(FabricaSocket.SOCKET_TCP);
			Pacote p = socket.receber(9999);
			String[] s = p.getConteudo().toString().split(":");
			vencedor = s[0];
			pontuacaoLocal = Integer.parseInt(s[1]);
			chamarTela();
		}
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
	public void setConfig(boolean isServer,String ipCliente){
		this.isServer = isServer;
		this.ipCliente = ipCliente;
		System.out.println("E servidor? "+isServer);
		escolheLetra();
		letraLabel.setText("Letra Escolhida: "+letraEscolhida);
		time = 30;
		updTime();
	}
	
	private void updTime(){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
					try{
						while(time>0){
							Platform.runLater(() -> 
							timeLabel.setText("Tempo: "+time));
							time --;
							Thread.sleep(1000);
					}
						montarResposta();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				
				
			}
		});
		t.start();
	}
	
	private void escolheLetra(){
		try{
			if(isServer){
				letraEscolhida = generateLetra();
				Pacote p = new Pacote(letraEscolhida, ipCliente, 9999);
				SocketGenerico socket = new FabricaSocket().getSocket(FabricaSocket.SOCKET_TCP);
				socket.enviar(p);
			} else {
				SocketGenerico socket = new FabricaSocket().getSocket(FabricaSocket.SOCKET_TCP);
				Pacote p = socket.receber(9999);
				ipServer = p.getIp();
				letraEscolhida = (String)  p.getConteudo();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private String generateLetra(){
		String alfa= "abcdefghijlmnopqrstuvxz";
		int r = (int) (Math.random()*24)+1;
		return alfa.charAt(r-1)+"";
	}
	
	
	private void montarResposta(){
		respostaLocal = "";
		respostaLocal = animalField.getText()+" :";
		respostaLocal += cidadeField.getText()+" :";
		respostaLocal += paisField.getText()+" :";
		respostaLocal += pessoaField.getText()+" :";
		respostaLocal += frutaField.getText()+" :";
		respostaLocal += marcaCarroField.getText()+" ";
		
		trocarRespostas();
	}
	
	private void chamarTela(){
		Platform.runLater(() ->{
			try{
				URL url = new URL("file:" + System.getProperty("user.dir") + 
						"/src/application/view/ResultView.fxml");
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(url);
				Parent resultView = loader.load();
				ResultController controller = loader.getController();
				controller.setResultado(pontuacaoLocal+" X "+pontuacaoAdv);
				Scene scene = new Scene(resultView);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.show();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		});
		
	}
}
