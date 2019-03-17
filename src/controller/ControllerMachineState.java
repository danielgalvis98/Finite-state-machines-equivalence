package controller;

import java.awt.GridBagLayoutInfo;
import java.awt.List;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
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
	GridPane gridMachineState;

	
	@FXML
	AnchorPane rootPane;
	
	public void setMundo(Management mun) {
		mundo = mun;
	}
	
	@FXML
	public void saveAutomata (ActionEvent e) throws IOException {
		automataNumber++;
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/FrameParameters.fxml"));
		rootPane.getChildren().setAll(pane);
		System.out.println(automataNumber);
		pintarTitulos(mundo.getMachine1());
	}

	//juanma
	
	public void pintarTitulos (FiniteStateMachine machine) {
		for (int i = 0; i < machine.getInputAlphabetArray().length; i++) {
			Label lb = new Label("    "+(machine.getInputAlphabetArray()[i])+"    ");
			
			gridMachineState.add(lb,i+1,0);	
		}
		/* - A B C D
		 * 
		 */
		for (int i = 0; i < machine.getTotalStates(); i++) {
			Label lb = new Label("  "+((i+1) + "")+"  ");
			gridMachineState.add(lb,0,i+1);	
		}
		/* - A B C D
		 * 1
		 * 2
		 */
	}
	
	public ObservableList<String> aux (String[] alfabeto) {
		ObservableList<String> list = FXCollections.observableArrayList();
		for (int i = 0; i < alfabeto.length; i++) {
			list.add(alfabeto[i]);
		}
		return list;
	}

	public void pintarCbInternosMoore(FiniteStateMachine machine) {
		ArrayList<ArrayList<ComboBox<String>>> ListaCbEntradas = new ArrayList<ArrayList<ComboBox<String>>>();
		
		//machine.getStates().size()
		//machine.getInputAlphabetArray().length
		
		for (int i = 0; i < 10; i++) {
			ListaCbEntradas.add(new ArrayList<ComboBox<String>>());
			for (int j = 0; j < 5; j++) {
				ListaCbEntradas.get(i).add(new ComboBox<String>(aux(machine.getInputAlphabetArray())));
			}
		}
				
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				gridMachineState.add(ListaCbEntradas.get(i).get(j),j+1,i+1);
			}
		}
		
		//machine.getStates().size()
		ArrayList<ComboBox<String>> ListaCbSalidas = new ArrayList<ComboBox<String>>();
		for (int i = 0; i < 10; i++) {
			ListaCbSalidas.add(new ComboBox<String>(aux(machine.getOutputAlphabetArray())));
			gridMachineState.add(ListaCbSalidas.get(i),machine.getInputAlphabetArray().length+1,i+1);
		}
	}
	
	public void pintarCbInternosMealy(FiniteStateMachine machine) {
		ArrayList<ArrayList<ComboBox<String>>> ListaCbEntradas = new ArrayList<ArrayList<ComboBox<String>>>();
		ArrayList<ArrayList<ComboBox<String>>> ListaCbSalidas = new ArrayList<ArrayList<ComboBox<String>>>();
		
		//machine.getStates().size()
		//machine.getInputAlphabetArray().length
		for (int i = 0; i < 10; i++) {
			ListaCbEntradas.add(new ArrayList<ComboBox<String>>());
			ListaCbSalidas.add(new ArrayList<ComboBox<String>>());
			for (int j = 0; j < 5; j++) {
				ListaCbEntradas.get(i).add(new ComboBox<String>(aux(machine.getInputAlphabetArray())));
				ListaCbSalidas.get(i).add(new ComboBox<String>(aux(machine.getOutputAlphabetArray())));
			}
		}
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				
				GridPane gp = new GridPane();
				gp.add(ListaCbEntradas.get(i).get(j),0,0);
				gp.add(ListaCbSalidas.get(i).get(j),1,0);
				
				gridMachineState.add(gp,j+1,i+1);
			}
		}
		
	}
	
	public void pintarAutomataMoore (FiniteStateMachine machine) {		
		pintarTitulos(machine);
		/* - A B C D 
		 * 1
		 * 2
		 */
		Label lb = new Label("   Salida  ");
		gridMachineState.add(lb,machine.getInputAlphabetArray().length+1,0);
		/* - A B C D Salida
		 * 1
		 * 2
		 */
		pintarCbInternosMoore(machine);
		/* -  A B C D Salida
		 *  1 + + + +    '
		 *  2 + + + +    '
		 */	
	}
	
	public void pintarAutomataMealy (FiniteStateMachine machine) {
		pintarTitulos(machine);
		/* - A B C D 
		 * 1
		 * 2
		 */
		pintarCbInternosMealy(machine);
		
		/* -  A  B  C  D
		 * 1  +' +' +' +'
		 * 2  +' +' +' +'
		 */
		
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		automataNumber++;
	}
	
}
