package application.controller;

import java.rmi.Remote;

public interface MyRemote extends Remote {
	
	public void setPlacar(String placar);
	
}
