package application.controller.thread;

import com.sockets.FabricaSocket;
import com.sockets.Pacote;
import com.sockets.SocketGenerico;

import application.controller.JoinGameController;

public class EsperaInicioPartidaThread implements Runnable{

	private JoinGameController controller;
	
	public EsperaInicioPartidaThread(JoinGameController controller) {
		// TODO Auto-generated constructor stub
		this.controller = controller;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		boolean ok = false;
		while(!ok){
			try{
				SocketGenerico socket = new FabricaSocket().getSocket(FabricaSocket.SOCKET_TCP);
				Pacote pacote = socket.receber(9999);
				if(pacote.getConteudo().toString().equalsIgnoreCase("start")){
					ok = true;
					System.out.println("recebido ordem de inicio");
					controller.start();
					
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		System.out.println("thread morreu");
	}

}
