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

	/**
	 * The connection with the model
	 */
	private Management management;
	
	/**
	 * The main pane of the Frame
	 */
	@FXML
	AnchorPane rootPane;
	
	/**
	 * The button to define that the machine is a Moore machine
	 */
	@FXML
	private RadioButton radioMoore;
	
	/**
	 * The button to define that the machine is a Mealy machine
	 */
	@FXML
	private RadioButton radioMealy;
	
	/**
	 * The text field for indicate the input alphabet of the machines
	 */
	@FXML
	private TextField txtInputAlphabet;
	
	/**
	 * The text field for indicate the output alphabet of the machines
	 */
	@FXML
	private TextField txtOutputAlphabet;

	/**
	 * The text field for indicate the number of automates that will have the first machine
	 */
	@FXML
	private TextField txtStatesFirstAutomata;

	/**
	 * The text field for indicate the number of automates that will have the second machine
	 */
	@FXML
	private TextField txtStatesSecondAutomata;
	
	/**
	 * The controller of the frame that allows the user to define the transitions of the machines
	 */
	private ControllerMachineState controlMachine;
	

	/**
	 * The method that is invoked when the button Save Parameters is pressed. It passes the common parameters
	 * of the 2 machines indicated on this frame to the model. If everything is right, it changes to the frame where
	 * the transitions of the machines are specified
	 * @param e The event that is happening
	 * @throws IOException If there is a problem finding the path of the new frame
	 */
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/FrameMachineState.fxml"));
			AnchorPane pane = loader.load();
			controlMachine = loader.getController();
			controlMachine.setMundo(management);
			controlMachine.setType(type);
			controlMachine.advance();
			rootPane.getChildren().setAll(pane);
		} catch (NumberFormatException ex) {
			Alert al = new Alert(Alert.AlertType.WARNING);
			al.setContentText("El número de estados de los autómatas debe de ser un entero mayor a 0");
			al.showAndWait();
		}
	}




	/**
	 * Method that is executed when the controller is initialized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		management = null;
		
	}

}
