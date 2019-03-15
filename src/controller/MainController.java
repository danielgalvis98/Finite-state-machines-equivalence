package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.FiniteStateMachine;
import model.Management;

public class MainController implements Initializable{

	
	private Management management;
	
	@FXML
	AnchorPane rootPane;
	
	@FXML
	private RadioButton radioMoore;
	
	@FXML
	private RadioButton radioMealy;
	
	@FXML
	private TextField txtInputAlphabet;
	
	@FXML
	private TextField txtOutputAlphabet;

	@FXML
	private TextField txtStatesFirstAutomata;

	@FXML
	private TextField txtStatesSecondAutomata;
	
	private ControllerMachineState controlMachine;
	

	
	@FXML
	public void saveInitialParameters (ActionEvent e) throws IOException {
		char type;
		if (radioMoore.isSelected()) {
			type = FiniteStateMachine.MOORE;
		} else {
			type = FiniteStateMachine.MEALY;
		}
		String [] inputAlphabet = txtInputAlphabet.getText().split(",");
		String [] outputAlphabet = txtOutputAlphabet.getText().split(",");
		if (txtInputAlphabet.getText().equals("") || txtOutputAlphabet.getText().equals("")) {
			Alert al = new Alert(Alert.AlertType.WARNING);
			al.setContentText("Los alfabetos no pueden estar vacíos");
			al.showAndWait();
			return;
		}
		try {
			int statesFirst = Integer.parseInt(txtStatesFirstAutomata.getText());
			int statesSecond = Integer.parseInt(txtStatesSecondAutomata.getText());
			if (statesFirst <= 0 || statesSecond <= 0) throw new NumberFormatException();
			management = new Management(inputAlphabet, outputAlphabet, type, statesFirst, statesSecond);
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/FrameMachineState.fxml"));
			rootPane.getChildren().setAll(pane);
			
			//juanma
			controlMachine = new ControllerMachineState();
			
			if(type == FiniteStateMachine.MOORE) {
				controlMachine.pintarAutomataMoore(management.getMachine1());	
				controlMachine.pintarAutomataMoore(management.getMachine2());	
			}else {
				controlMachine.pintarAutomataMealy(management.getMachine1());
				controlMachine.pintarAutomataMealy(management.getMachine2());
			}
			
			
			
			
		} catch (NumberFormatException ex) {
			Alert al = new Alert(Alert.AlertType.WARNING);
			al.setContentText("El número de estados de los autómatas debe de ser un entero mayor a 0");
			al.showAndWait();
		}
	}




	@Override
	public void initialize(URL location, ResourceBundle resources) {
		management = null;
		
	}

}
