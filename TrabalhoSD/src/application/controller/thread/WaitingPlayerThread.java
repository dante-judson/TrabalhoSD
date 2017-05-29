package application.controller.thread;



import com.sockets.FabricaSocket;
import com.sockets.Pacote;
import com.sockets.SocketGenerico;

import application.controller.WaitPlayerController;

public class WaitingPlayerThread implements Runnable{

	private WaitPlayerController controller;
	
	public WaitingPlayerThread(WaitPlayerController controller) {
		// TODO Auto-generated constructor stub
		this.controller = controller;
	}
	
	@Override
	public void run() {
				// TODO Auto-generated method stub
				try{
					boolean ok = false;
					System.out.println("thread iniciada");
					SocketGenerico socket = new FabricaSocket().getSocket(FabricaSocket.SOCKET_TCP);
					while(!ok){
					Pacote pacote = socket.receber(9999);
						if(pacote.getConteudo().toString().equals("join")){
							controller.addPlayer(pacote);
							pacote.setConteudo("ok");
							Thread.sleep(500);
							socket.enviar(pacote);
							ok = true;
						}
						if(pacote.getConteudo().toString().equals("leave")){
							controller.removePlayer(pacote);
						}


						Thread.sleep(500);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			System.out.println("wait thread morreu");
		
		
	}

}
