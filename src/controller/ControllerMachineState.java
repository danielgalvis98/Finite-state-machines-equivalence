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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.FiniteStateMachine;
import model.Management;
import model.MealyState;
import model.MooreState;
import model.State;

public class ControllerMachineState implements Initializable{

	private int automataNumber;

	private Management mundo;
	
	@FXML
	private GridPane gridMachineState;

	private char type;
	
	@FXML
	private AnchorPane rootPane;
	
	private ArrayList<ArrayList<ComboBox<String>>> listaCbEntradas;
	private ArrayList<ComboBox<String>> listaCbSalidasMoore;
	private ArrayList<ArrayList<ComboBox<String>>> listaCbSalidasMealy;
	
	public void setMundo(Management mun) {
		mundo = mun;
	}
	
	@FXML
	public void saveAutomata (ActionEvent e) throws IOException {
		boolean added;
		if (automataNumber == 1) {
			added = addStates(mundo.getMachine1());
		} else {
			added = addStates(mundo.getMachine2());
		}
		if (added) {
			if (automataNumber == 1) {
				added = addTransitions(mundo.getMachine1());
			} else {
				added = addTransitions(mundo.getMachine2());
			}
		}
		if (added) {
			advance();
		}
	}
	
	public void advance() throws IOException {
		automataNumber++;
		if (automataNumber <= 2) {
			gridMachineState.getChildren().setAll();
			gridMachineState.setGridLinesVisible(true);
			if (automataNumber == 1) {
				if (type == FiniteStateMachine.MEALY) {
					pintarAutomataMealy(mundo.getMachine1());
				} else {
					pintarAutomataMoore(mundo.getMachine1());
				}
			} else {
				if (type == FiniteStateMachine.MEALY) {
					pintarAutomataMealy(mundo.getMachine2());
				} else {
					pintarAutomataMoore(mundo.getMachine2());
				}
			}
		} else {
			boolean equivalents = mundo.verifyEquivalenceByMachine();
			Alert alert;
			if (equivalents) {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("EQUIVALENTES");
				alert.setHeaderText("Felicidades!");
				alert.setContentText("LOS AUTOMATAS SON EQUIVALENTES!!");
			} else {
				alert = new Alert(AlertType.WARNING);
				alert.setTitle("NO EQUIVALENTES");
				alert.setHeaderText(":(");
				alert.setContentText("LOS AUTOMATAS NO SON EQUIVALENTES :C");
			}
			alert.showAndWait();
			AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/FrameParameters.fxml"));
			rootPane.getChildren().setAll(pane);
		}
	}
	
	
	public boolean addStates(FiniteStateMachine machine) {
		boolean added = true;
		machine.getStates().clear();
		for (int i = 0; i < listaCbEntradas.size(); i++) {
			if (machine.getType() == FiniteStateMachine.MOORE) {
				String s = listaCbSalidasMoore.get(i).getValue();
				if (s == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error agregando estado");
					alert.setContentText("El estado " + (i+1) + " no tiene salida asignada");
					alert.showAndWait();
					added = false;
				} else 
				machine.addState(new MooreState(i + "", s));
			} else {
				machine.addState(new MealyState(i + ""));
			}
		}
		return added;
	}
	
	public boolean addTransitions(FiniteStateMachine machine) {
		boolean added = true;
		for (int i = 0; i < listaCbEntradas.size(); i++) {
			for (int j = 0; j < listaCbEntradas.get(i).size(); j++) {
				try {
					int toState = Integer.parseInt(listaCbEntradas.get(i).get(j).getValue());
					machine.addStateTransition(machine.getInputAlphabetArray()[j], i, toState-1);
					if (machine.getType() == FiniteStateMachine.MEALY) {
						String out = listaCbSalidasMealy.get(i).get(j).getValue();
						machine.addOutputTransition(machine.getInputAlphabetArray()[j], i, out);
					}
					
				} catch (Exception e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error agregando transiciones");
					alert.setContentText("Asegurese que para el estado " + (i+1) + " tenga una transición y una salida"
							+ "asignada para la entrada " + machine.getInputAlphabetArray()[j]);
					alert.showAndWait();
					added = false;
				}
			}
		}
		return added;
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
		listaCbEntradas = new ArrayList<ArrayList<ComboBox<String>>>();
		
		//machine.getStates().size()
		//machine.getInputAlphabetArray().length
		
		String [] states = new String [machine.getTotalStates()];
		for (int i = 0; i < machine.getTotalStates(); i++) {
			states[i] = (i+1) + "";
		}
		for (int i = 0; i < machine.getTotalStates(); i++) {
			listaCbEntradas.add(new ArrayList<ComboBox<String>>());
			for (int j = 0; j < machine.getInputAlphabetArray().length; j++) {
				listaCbEntradas.get(i).add(new ComboBox<String>(aux(states)));
				gridMachineState.add(listaCbEntradas.get(i).get(j),j+1,i+1);
			}
		}
				
		//machine.getStates().size()
		listaCbSalidasMoore = new ArrayList<ComboBox<String>>();
		for (int i = 0; i < machine.getTotalStates(); i++) {
			listaCbSalidasMoore.add(new ComboBox<String>(aux(machine.getOutputAlphabetArray())));
			gridMachineState.add(listaCbSalidasMoore.get(i),machine.getInputAlphabetArray().length+1,i+1);
		}
	}
	
	public void pintarCbInternosMealy(FiniteStateMachine machine) {
		listaCbEntradas = new ArrayList<ArrayList<ComboBox<String>>>();
		listaCbSalidasMealy = new ArrayList<ArrayList<ComboBox<String>>>();
		
		String [] states = new String [machine.getTotalStates()];
		for (int i = 0; i < machine.getTotalStates(); i++) {
			states[i] = (i+1) + "";
		}
		
		//machine.getStates().size()
		//machine.getInputAlphabetArray().length
		for (int i = 0; i < machine.getTotalStates(); i++) {
			listaCbEntradas.add(new ArrayList<ComboBox<String>>());
			listaCbSalidasMealy.add(new ArrayList<ComboBox<String>>());
			for (int j = 0; j < machine.getInputAlphabetArray().length; j++) {
				listaCbEntradas.get(i).add(new ComboBox<String>(aux(states)));
				listaCbSalidasMealy.get(i).add(new ComboBox<String>(aux(machine.getOutputAlphabetArray())));
			}
		}
		
		for (int i = 0; i < machine.getTotalStates(); i++) {
			for (int j = 0; j < machine.getInputAlphabetArray().length; j++) {
				
				GridPane gp = new GridPane();
				gp.add(listaCbEntradas.get(i).get(j),0,0);
				gp.add(listaCbSalidasMealy.get(i).get(j),1,0);
				
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
	
	public void setType (char type) {
		this.type = type;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		automataNumber = 0;
	}
	
}
