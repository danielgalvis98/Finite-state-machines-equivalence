package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class ControllerMachineState implements Initializable{

	int automataNumber;
	

	
	@FXML
	AnchorPane rootPane;
	
	@FXML
	public void saveAutomata (ActionEvent e) throws IOException {
		automataNumber++;
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/FrameParameters.fxml"));
		rootPane.getChildren().setAll(pane);
		System.out.println(automataNumber);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		automataNumber++;
	}
	
}
