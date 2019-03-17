package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.cell.ComboBoxListCell;
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
	
	public void pintarTitulos (FiniteStateMachine machine) {
		gridMachineStade = new GridPane();
		for (int i = 0; i < machine.getInputAlphabetArray().length; i++) {
			Label lb = new Label(machine.getInputAlphabetArray()[i]);
			gridMachineStade.add(lb,0,i+1);	
		}
		/* - A B C
		 * 
		 */
		for (int i = 0; i < machine.getStates().size(); i++) {
			Label lb = new Label(machine.getStates().get(i).getName());
			gridMachineStade.add(lb,i+1,0);	
		}
		/* - A B C D
		 * E
		 * F
		 */
	}
	
	public void pintarAutomataMoore (FiniteStateMachine machine) {
		
		pintarTitulos(machine);
		
		ComboBox<String>cbEntradas = new ComboBox<String>();		
		/*
		ArrayList<String> alfabetoEntrada = (ArrayList<String>) Arrays.asList(machine.getInputAlphabetArray());
		cbEntradas.setItems((ObservableList<String>) alfabetoEntrada);

		 */
		ComboBox<String>cbSalidas = new ComboBox<String>();
		/*
		ArrayList<String> alfabetoSalida = (ArrayList<String>) Arrays.asList(machine.getOutputAlphabetArray()); 
		cbSalidas.setItems((ObservableList<String>) alfabetoSalida);
		*/
		Label lb = new Label("Salida");
		gridMachineStade.add(lb,0,machine.getInputAlphabetArray().length+1);
		/* - A B C D Salida
		 * q1
		 * q2
		 */
		
		for (int i = 1; i < machine.getStates().size(); i++) {
			for (int j = 1; j <  machine.getInputAlphabetArray().length; j++) {
				gridMachineStade.add(cbEntradas,i,j);
			}
			gridMachineStade.add(cbSalidas,i,machine.getInputAlphabetArray().length+1);
		}
		/* -  A B C D Salida
		 * q1 + + + +    '
		 * q2 + + + +    '
		 */
		
	}
	
	public void pintarAutomataMealy (FiniteStateMachine machine) {
		pintarTitulos(machine);
		
		ComboBox<String>cbEntradas = new ComboBox<String>();		
		ArrayList<String> alfabetoEntrada = (ArrayList<String>) Arrays.asList(machine.getInputAlphabetArray());
		cbEntradas.setItems((ObservableList<String>) alfabetoEntrada);

		ComboBox<String>cbSalidas = new ComboBox<String>();
		ArrayList<String> alfabetoSalida = (ArrayList<String>) Arrays.asList(machine.getOutputAlphabetArray()); 
		cbSalidas.setItems((ObservableList<String>) alfabetoSalida);
		
		for (int i = 1; i < machine.getStates().size(); i++) {
			for (int j = 0; j < machine.getInputAlphabetArray().length; j++) {
				GridPane gp = new GridPane();
				gp.add(cbEntradas,0,0);
				gp.add(cbSalidas,0,1);
				gridMachineStade.add(gp,i,j);
			}
		}
		/* -  A  B  C  D
		 * q1 +' +' +' +'
		 * q2 +' +' +' +'
		 */
		
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		automataNumber++;
	}
	
}
