package organizer.gui;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class MainAppController {
	private static final int ITEMS_PER_PAGE = 6;
	private MainApp mainApp;
	
	@FXML
	private TextField commandText;
	@FXML
	private Label commandStatus;
	
	@FXML
	private FlowPane mainPane;
	@FXML
	private FlowPane sidePane;
	@FXML
	private Label pageStatus;
	
	@FXML
	private AnchorPane mainPaneHintLayer;
	@FXML
	private Label sidePaneHintLabel;
	
	
	private int pageStart = 0;
	private int pageCount = 0;
	private List<TaskItem> taskData;
	
	public MainAppController() {
	}
	
	private void setupEventHandlers() {
	}
	
	@FXML
	private void initialize() {
		setupEventHandlers();
		commandStatus.setText("");
		commandText.requestFocus();

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
		restoreSidePane();
	}

	private void setCommandStatus() {
		commandStatus.setText(mainApp.getCurrentCommandStatus());
	}
	
	private void updateTaskList() {
		taskData = this.mainApp.getTaskData();
		if (taskData.size() == 0) {
			pageCount = 1;
		} else if (taskData.size() % ITEMS_PER_PAGE == 0) {
			pageCount = taskData.size() / ITEMS_PER_PAGE;
		} else {
			pageCount = taskData.size() / ITEMS_PER_PAGE + 1;
		}
		pageStart = 0;
		updatePage();
	}
	
	private void updatePage() {
		final ObservableList<Node> children = mainPane.getChildren();
		children.clear();
		final List<TaskCardController> controllers = 
				taskData.stream()
				.skip(pageStart * ITEMS_PER_PAGE)
				.limit(ITEMS_PER_PAGE)
				.map(task -> {
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
		pageStatus.setText(String.format("%d of %d", pageStart + 1, pageCount));
	}
	
	@FXML
	public void openDialogHelp() throws IOException {
		mainApp.showHelpDialog();
	}
	
	@FXML
	public void keyPressHandler(KeyEvent e) throws Exception {
		if (e.getCode() == KeyCode.PAGE_DOWN) {
			System.out.println("NEXT");
			if (pageStart + 1 < pageCount) {
				++pageStart;
				updatePage();
			}
		} else if (e.getCode() == KeyCode.PAGE_UP) {
			System.out.println("PREV");
			if (pageStart > 0) {
				--pageStart;
				updatePage();
			}
		} else if (e.isControlDown()) {
			hideControlKeyHint();
			if (e.getCode() == KeyCode.BACK_QUOTE)
				restoreSidePane();
			else if (e.getCode() == KeyCode.DIGIT1)
				displayTaskDetailSidePane(0);
			else if (e.getCode() == KeyCode.DIGIT2)
				displayTaskDetailSidePane(1);
			else if (e.getCode() == KeyCode.DIGIT3)
				displayTaskDetailSidePane(2);
			else if (e.getCode() == KeyCode.DIGIT4)
				displayTaskDetailSidePane(3);
			else if (e.getCode() == KeyCode.DIGIT5)
				displayTaskDetailSidePane(4);
			else if (e.getCode() == KeyCode.DIGIT6)
				displayTaskDetailSidePane(5);
			else
				showControlKeyHint();
		}
	}
	
	@FXML
	public void keyReleaseHandler(KeyEvent e) {
		if (!e.isControlDown()) {
			hideControlKeyHint();
		}
	}
	
	private void displayTaskDetailSidePane(int index) throws IOException {
		sidePane.getChildren().clear();
		final TaskCardController controller = new TaskCardController(TaskCardController.CardSize.XLARGE);
		if (pageStart * ITEMS_PER_PAGE + index < taskData.size()) {
			controller.loadTask(taskData.get(pageStart * ITEMS_PER_PAGE + index));
			sidePane.getChildren().add(controller);
		}
	}
	
	private void restoreSidePane() {
		sidePane.getChildren().clear();
	}
	
	private void showControlKeyHint() {
		mainPaneHintLayer.setVisible(true);
		for (int i = 0, start = pageStart * ITEMS_PER_PAGE, size = taskData.size();
				i < 6;
				++i, ++start)
			mainPaneHintLayer.getChildren().get(i).setVisible(start < size);
		sidePaneHintLabel.setVisible(true);
	}
	private void hideControlKeyHint() {
		mainPaneHintLayer.setVisible(false);
		sidePaneHintLabel.setVisible(false);
	}
}
