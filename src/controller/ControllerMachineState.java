package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.FiniteStateMachine;
import model.Management;

public class ControllerMachineState implements Initializable{

	int automataNumber;

	Management mundo;
	
	@FXML
	GridPane gridMachineStade;

	
	@FXML
	AnchorPane rootPane;
	
	@FXML
	public void saveAutomata (ActionEvent e) throws IOException {
		automataNumber++;
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/FrameParameters.fxml"));
		rootPane.getChildren().setAll(pane);
		System.out.println(automataNumber);
	}

	//juanma
	public void pintarAutomataMoore (FiniteStateMachine machine) {

	}
	
	public void pintarAutomataMealy (FiniteStateMachine machine) {
		
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		automataNumber++;
	}
	
}
