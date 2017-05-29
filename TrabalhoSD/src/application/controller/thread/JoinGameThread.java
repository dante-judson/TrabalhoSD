package application.controller.thread;

import com.sockets.FabricaSocket;
import com.sockets.Pacote;
import com.sockets.SocketGenerico;

import application.controller.JoinGameController;

public class JoinGameThread implements Runnable{

	private JoinGameController controller;
	private String ipServer;
	
	public JoinGameThread(JoinGameController controller, String ipServer) {
		// TODO Auto-generated constructor stub
		this.controller = controller;
		this.ipServer = ipServer;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try{
			System.out.println("thread iniciada");
			System.out.println("ip: "+ipServer);
			SocketGenerico socket = new FabricaSocket().getSocket(FabricaSocket.SOCKET_TCP);
			Pacote pacote = new Pacote("join", ipServer, 9999);
			socket.enviar(pacote);
			pacote = socket.receber(9999);
			if(pacote.getConteudo().toString().equalsIgnoreCase("ok"));
				controller.setjoinedGame(true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println("join thread morreu");
	}

}
