package organizer.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class MainAppController {
	private MainApp mainApp;

	@FXML
	private TableView<TaskItem> taskTable;
	@FXML
	private TableColumn<TaskItem, String> taskTableNameColumn;
	@FXML
	private TableColumn<TaskItem, String> taskTableStatusColumn;
	@FXML
	private TableColumn<TaskItem, String> taskTableDueDateColumn;
	@FXML
	private TableColumn<TaskItem, Number> taskTableIndexColumn;
	@FXML
	private TableColumn<TaskItem, String> taskTablePriorityColumn;
	@FXML
	private TextField commandText;
	@FXML
	private Label commandStatus;
	
	public MainAppController() {
	}
	
	@FXML
	private void initialize() {
		taskTableIndexColumn.setCellValueFactory(
				cellData -> cellData.getValue().taskIndexProperty());
		taskTableNameColumn.setCellValueFactory(
				cellData -> cellData.getValue().taskNameProperty());
		taskTableStatusColumn.setCellValueFactory(
				cellData -> cellData.getValue().taskStatusProperty());
		taskTableDueDateColumn.setCellValueFactory(
				cellData ->
					cellData.getValue().taskDueDateProperty().get() == null ?
							new SimpleStringProperty("Not Applicable") :
							cellData.getValue().taskDueDateProperty().asString());
		taskTablePriorityColumn.setCellValueFactory(
				cellData ->
					cellData.getValue().taskPriorityProperty().get() == null ?
							new SimpleStringProperty("Not Applicable") :
							cellData.getValue().taskPriorityProperty());
		
		commandStatus.setText("");

	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		taskTable.setItems(this.mainApp.getTaskData());
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
	
	private void updateTaskList() {
		taskTable.setItems(this.mainApp.getTaskData());
	}
	
	private void setCommandStatus() {
		commandStatus.setText(mainApp.getCurrentCommandStatus());
	}
}
