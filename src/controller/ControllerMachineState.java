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

	/**
	 * If specifies if the actual frame is for defining the transitions of the first or the second automata
	 */
	private int automataNumber;

	/**
	 * The connection with the model
	 */
	private Management mundo;
	
	/**
	 * The Pane where will be able the options to define the transitions
	 */
	@FXML
	private GridPane gridMachineState;

	/**
	 * It specifies if the current machine is a Mealy or Moore machine.
	 */
	private char type;
	
	/**
	 * The main pane of the frame
	 */
	@FXML
	private AnchorPane rootPane;
	
	/**
	 * A matrix containing all the Comboboxes defining a new state for every state and entry of the input alphabet.
	 */
	private ArrayList<ArrayList<ComboBox<String>>> listaCbEntradas;
	
	/**
	 * An ArrayList containing all the Comboboxes defining the output for every state (If the machine is a Moore machine).
	 */
	private ArrayList<ComboBox<String>> listaCbSalidasMoore;
	
	/**
	 * A matrix containing all the Comboboxes defining the output for every state and entry of the input alphabet (If the machine is a Mealy machine).
	 */
	private ArrayList<ArrayList<ComboBox<String>>> listaCbSalidasMealy;
	
	/**
	 * The method to define the instance in which are the machines that the frame will show
	 * @param mun
	 */
	public void setMundo(Management mun) {
		mundo = mun;
	}
	
	/**
	 * The method that is invoked when the Save Automata button is pressed. If the automata being completed was the first, if everything is correct,
	 * cleans the frame and put the options for completing the second machine. If the second machine is completed, says if the two are equivalent and
	 * goes back to the frame to define compare new machines.
	 * @param e The event of the Action
	 * @throws IOException If the path of the first frime is not finded.
	 */
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
	
	/**
	 * Is the method that prepares the new GridPane for the second machine (If it hasn't been filled yet) or to change
	 * to the first Frame if the 2 machines are completed.
	 * @throws IOException If the path to the first frame is not found.
	 */
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
	
	/**
	 * The method that add the states to the machines. If it is a Moore Machine, it saves also the output of the state
	 * specified by the user
	 * @param machine The machine where the states will be added.
	 * @return true if all the states where added, false if there is at least 1 state without an output defined.
	 */
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
	
	/**
	 * The method that add the transitions (and the outputs in the case of the Mealy machine) to the machine specified by the user.
	 * @param machine The machine were the transitions will be done.
	 * @return true if all the transitions and outputs where added. False if there is at least 1 transition or output who hasn't been specified.S
	 */
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
	
	/**
	 * The method that paints the alphabet input in the columns and the states numbers in the rows of the GridPane
	 * @param machine The machine that will be painted
	 */
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
	
	/**
	 * Passes the elements of a array of Strings into an ObservableList of Strings
	 * @param alfabeto
	 * @return
	 */
	public ObservableList<String> aux (String[] alfabeto) {
		ObservableList<String> list = FXCollections.observableArrayList();
		for (int i = 0; i < alfabeto.length; i++) {
			list.add(alfabeto[i]);
		}
		return list;
	}

	/**
	 * Paints the ComboBoxes of the Moore machine that will have the option of all the states of the machine in order
	 * to define the transitions of all the states.
	 * @param machine The machine where the transitions will be defined.
	 */
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
	
	/**
	 * Paints the ComboBoxes of the Moore machine that will have the option of all the states of the machine and all the
	 * Strings of the output alphabet in order to define the transitions of all the states/input.
	 * @param machine The machine where the transitions and outputs will be done.
	 */
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
	
	/**
	 * It puts on the screen all the elements that are necessary in order to specify a Moore Machine.
	 * @param machine The machine that will be painted
	 */
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
	
	/**
	 * It puts on the screen all the elements that are necessary in order to specify a Mealy Machine.
	 * @param machine The machine that will be painted
	 */
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
	
	/**
	 * States if the machine of the frame is a Mealy Machine or a Moore machine
	 * @param type FiniteStateMachine.MEALY if it is a Mealy machine and FiniteStateMachine.MOORE if it is a Moore machine
	 */
	public void setType (char type) {
		this.type = type;
	}

	/**
	 * Method that is executed when the controller is initialized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		automataNumber = 0;
	}
	
}
