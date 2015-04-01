package organizer.gui;

import java.util.stream.Collectors;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
	private TableColumn<TaskItem, String> taskTableStartTimeColumn;
	@FXML
	private TableColumn<TaskItem, String> taskTableEndTimeColumn;
	@FXML
	private TableColumn<TaskItem, Number> taskTableIndexColumn;
	@FXML
	private TableColumn<TaskItem, String> taskTablePriorityColumn;
	@FXML
	private TextField commandText;
	@FXML
	private Label commandStatus;
	
	@FXML
	private TabPane taskPane;
	@FXML
	private Tab showTaskTab;
	@FXML
	private ListView<String> dueTodayTaskList;
	@FXML
	private ListView<String> dueWeekTaskList;
	@FXML
	private ListView<String> dueMonthTaskList;
	
	public MainAppController() {
	}
	
	private void setupEventHandlers() {
		taskTable
			.getSelectionModel()
			.selectedItemProperty()
			.addListener(new ChangeListener<TaskItem>() {
				@Override
				public void changed(
						ObservableValue<? extends TaskItem> observable,
						TaskItem oldValue, TaskItem newValue) {
					System.out.println("Select: ");
					final ObservableList<TaskItem> selectedTaskItems = taskTable.getSelectionModel().getSelectedItems();
					if (selectedTaskItems.size() == 0) {
					} else if (selectedTaskItems.size() == 1) {
					}
				}
			});
	}
	
	@FXML
	private void initialize() {
		// set selection model
		taskTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		setupEventHandlers();
		
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
		taskTableStartTimeColumn.setCellValueFactory(
				cellData ->
					cellData.getValue().taskStartTimeProperty().get() == null ?
							new SimpleStringProperty("-") :
							cellData.getValue().taskDueDateProperty().asString());
		taskTableEndTimeColumn.setCellValueFactory(
				cellData ->
					cellData.getValue().taskEndTimeProperty().get() == null ?
							new SimpleStringProperty("-") :
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

	private void setCommandStatus() {
		commandStatus.setText(mainApp.getCurrentCommandStatus());
	}
	
	private void updateTaskList() {
		taskTable.setItems(this.mainApp.getTaskData());
		displayDueTasksInSidePanel();
	}
	
	private void displayDueTasksInSidePanel() {
		final ObservableList<TaskItem> list = this.mainApp.getTaskData();
		dueTodayTaskList.setItems(
				FXCollections.observableArrayList(
					list.stream()
						.map(task -> task.getTaskName())
						.collect(Collectors.toList())));
	}
}
