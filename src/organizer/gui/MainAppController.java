package organizer.gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class MainAppController {
	private MainApp mainApp;
	
	@FXML
	private TextField commandText;
	@FXML
	private Label commandStatus;
	
	@FXML
	private FlowPane deadlineTaskPane;
	
	
	public MainAppController() {
	}
	
	private void setupEventHandlers() {
	}
	
	@FXML
	private void initialize() {
		setupEventHandlers();
		commandStatus.setText("");

	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		updateTaskList();
	}
	
	@FXML
	public void performCommand() {
		final String commandString = commandText.textProperty().get();
		System.out.print("Command: ");
		System.out.println(commandString);
		commandText.clear();
		mainApp.performCommand(commandString);
		updateTaskList();
		setCommandStatus();
	}

	private void setCommandStatus() {
		commandStatus.setText(mainApp.getCurrentCommandStatus());
	}
	
	private void updateTaskList() {
		final ObservableList<Node> children = deadlineTaskPane.getChildren();
		children.clear();
		final List<TaskCardController> controllers = this.mainApp.getTaskData().stream().map(
				task -> {
					try {
						final TaskCardController controller = new TaskCardController();
						controller.loadTask(task);
						return controller;
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				})
				.collect(Collectors.toList());
		children.addAll(controllers);
		displayDueTasksInSidePanel();
	}
	
	private static boolean compareDateBefore(LocalDate thisDate, LocalDate reference) {
		return thisDate == null || reference == null || thisDate.compareTo(reference) < 0;
	}
	
	private static boolean compareDateEqual(LocalDate thisDate, LocalDate reference) {
		return thisDate == null || reference == null || thisDate.compareTo(reference) == 0;
	}
	
	private void displayDueTasksInSidePanel() {
		final List<TaskItem> list = this.mainApp.getTaskData();
		
		// This month
		final List<TaskItem> thisMonthList = list
				.stream()
				.filter(task -> task != null
					&& compareDateBefore(task.getTaskDueDate(), LocalDate.now().plusMonths(1))
					&& task.getTaskStatus().equals("INCOMPLETE"))
				.collect(Collectors.toList());
		// This week
		final List<TaskItem> thisWeekList = thisMonthList
				.stream()
				.filter(task -> task != null && compareDateBefore(task.getTaskDueDate(), LocalDate.now().plusWeeks(1)))
				.collect(Collectors.toList());
		// Today
		final List<TaskItem> todayList = thisWeekList
				.stream()
				.filter(task -> task != null && compareDateEqual(task.getTaskDueDate(), LocalDate.now()))
				.collect(Collectors.toList());
	}
	
	@FXML
	public void openDialogHelp() throws IOException {
		mainApp.showHelpDialog();
	}
}
