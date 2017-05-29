package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ResultController {

	@FXML
	Label resultLabel;
	
	private String resultado;
	
	public void setResultado(String resultado){
		this.resultado = resultado;
		resultLabel.setText("Pontuação\n"+resultado);
	}
}
