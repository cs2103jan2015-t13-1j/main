package organizer.gui;

/*@author */

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class TaskCardController extends Region {
	private TaskItem task;
	
	@FXML
	private Label nameLabel;
	@FXML
	private Label priorityLabel;
	@FXML
	private Label statusLabel;
	@FXML
	private Label startTimeLabel;
	@FXML
	private Label endTimeLabel;
	@FXML
	private Label idLabel;
	
	public TaskCardController() throws IOException {
		this(true);	// default size is small
	}

	public TaskCardController(boolean largeSize) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
        		largeSize ? "TaskCardLarge.fxml" : "TaskCardSmall.fxml"));
        fxmlLoader.setController(this);
        this.getChildren().add(fxmlLoader.load());
	}
	
	@FXML
	private void initialize() {
	}
	
	public void loadTask(TaskItem task) {
		this.task = task;
		nameLabel.textProperty().bind(task.taskNameProperty());
		idLabel.setText(Integer.toString(task.getTaskIndex()));
		statusLabel.textProperty().bind(task.taskStatusProperty());
		priorityLabel.textProperty().bind(task.taskPriorityProperty());
		if (task.getTaskStartDate() != null || task.getTaskStartTime() != null)
			startTimeLabel.setText("from " + formatDateString(task.getTaskStartDate(), task.getTaskStartTime()));
		else
			startTimeLabel.setText("");
		if (task.getTaskEndDate() != null || task.getTaskEndTime() != null)
			endTimeLabel.setText("to " + formatDateString(task.getTaskEndDate(), task.getTaskEndTime()));
		else
			endTimeLabel.setText("");
	}

	private static String formatDateString(
			LocalDate date,
			LocalTime time) {
		StringBuilder strb = new StringBuilder();
		if (date != null) {
			strb.append(date.getDayOfMonth());
			strb.append(' ');
			strb.append(date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
			strb.append(',');
			strb.append(date.getYear());
		}
		if (date != null && time != null) {
			strb.append(' ');
		}
		if (time != null) {
			strb.append(time.getHour());
			strb.append(':');
			strb.append(time.getMinute());
		}
		return strb.toString();
	}
}
