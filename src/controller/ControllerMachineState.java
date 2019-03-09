package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ControllerMachineState {
	
	@FXML
	AnchorPane rootPane;
	
	public void changePane (ActionEvent e) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/FrameParameters.fxml"));
		rootPane.getChildren().setAll(pane);
	}

}
