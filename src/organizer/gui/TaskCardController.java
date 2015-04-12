package organizer.gui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

//@author A0113627L
public class TaskCardController extends Region {
	private final static Logger LOGGER = Logger.getLogger(TaskCardController.class.getName());
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
	@FXML
	private Label typeLabel;
	
	public enum CardSize {
		SMALL,
		LARGE,
		XLARGE;
		public String getResourceName() {
			switch (this) {
			case SMALL: return "TaskCardSmall.fxml";
			case LARGE: return "TaskCardLarge.fxml";
			case XLARGE: return "TaskCardXLarge.fxml";
			default: throw new RuntimeException("Unimplememented size: " + this);
			}
		}
	}
	
	public TaskCardController() throws IOException {
		this(CardSize.LARGE);	// default size is large
	}

	public TaskCardController(CardSize size) throws IOException {
		try {
	        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(size.getResourceName()));
	        fxmlLoader.setController(this);
	        this.getChildren().add(fxmlLoader.load());
		} catch (IOException e) {
			LOGGER.throwing(FXMLLoader.class.getName(), "load", e);
			throw e;
		}
	}
	
	@FXML
	private void initialize() {
	}
	
	public void loadTask(TaskItem task) {
		nameLabel.textProperty().bind(task.taskNameProperty());
		idLabel.setText(Integer.toString(task.getTaskIndex()));
		statusLabel.textProperty().bind(task.taskStatusProperty());
		priorityLabel.textProperty().bind(task.taskPriorityProperty());
		typeLabel.setText(task.getTaskType());
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
			// String has date field
			strb.append(date.getDayOfMonth())
				.append(' ')
				.append(date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()))
				.append(',')
				.append(date.getYear());
		}
		if (date != null && time != null) {
			// String has both date and time field
			// We need separator
			strb.append(' ');
		}
		if (time != null) {
			// String has time field
			final int hour = time.getHour(), minute = time.getMinute();
			if (hour < 10)
				strb.append('0');	// Fill missing 0
			strb
				.append(time.getHour())
				.append(':');
			if (minute < 10)
				strb.append('0');
			strb.append(time.getMinute());
		}
		return strb.toString();
	}
}
