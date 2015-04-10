package organizer.gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.stream.Collectors;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

//@author A0113627L
public class MainAppController {
	private static final int ITEMS_PER_PAGE = 6;
	private static final String URL_HELP_MANUAL = "src/resources/help_manual/Mnemonical User Manual.html";
	private static final double FADE_TRANSLATION_IN_TASKCARD_MILLIS = 300;
	private static final double FADE_TRANSLATION_IN_TASKCARD_START_ALPHA = 0.1;
	private static final double FADE_TRANSLATION_IN_TASKCARD_END_ALPHA = 1;
	private static final double FADE_TRANSLATION_IN_TASKCARD_SEPARATION_MILLIS = 15;

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

	@FXML
	private void initialize() {
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

		// grab the user view info before each command
		int lastVisitedPage = pageStart;

		boolean isAddCommand = mainApp.performCommand(commandString);
		updateTaskList();

		if (isAddCommand) {
			pageStart = pageCount - 1;
		} else if (commandString.startsWith("view ")) {
			pageStart = 0;
		} else {
			pageStart = lastVisitedPage;
		}

		updatePage();
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
		final List<TaskCardController> controllers = taskData
				.stream()
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
				}).collect(Collectors.toList());
		children.addAll(controllers);
		pageStatus.setText(String.format("%d of %d", pageStart + 1, pageCount));
		animateMainTaskCards(controllers);
	}
	
	private void animateMainTaskCards(List<TaskCardController> controllers) {
		double delay = 0;
		for(TaskCardController controller : controllers) {
			final Duration durationDelay = Duration.millis(delay);
			final TranslateTransition translation = new TranslateTransition(
					Duration.millis(FADE_TRANSLATION_IN_TASKCARD_MILLIS),
					controller);
			translation.setDelay(durationDelay);
			translation.setCycleCount(0);
			translation.setFromX(100);
			translation.setToX(controller.getLayoutX());
			translation.play();
			final FadeTransition fade = new FadeTransition(
					Duration.millis(FADE_TRANSLATION_IN_TASKCARD_MILLIS),
					controller);
			fade.setDelay(durationDelay);
			fade.setFromValue(FADE_TRANSLATION_IN_TASKCARD_START_ALPHA);
			fade.setToValue(FADE_TRANSLATION_IN_TASKCARD_END_ALPHA);
			fade.play();
			delay += FADE_TRANSLATION_IN_TASKCARD_SEPARATION_MILLIS;
		}
	}

	@FXML
	public void openDialogHelp() throws IOException {
		mainApp.showHelpDialog();
	}

	private void flipNextPage() {
		System.out.println("NEXT");
		if (pageStart + 1 < pageCount) {
			++pageStart;
			updatePage();
		}
	}

	private void flipPrevPage() {
		System.out.println("PREV");
		if (pageStart > 0) {
			--pageStart;
			updatePage();
		}
	}

	private void processQuickAction(KeyCode code) throws IOException {
		if (code == KeyCode.BACK_QUOTE) {
			restoreSidePane();
		} else if (code == KeyCode.DIGIT1) {
			displayTaskDetailSidePane(0);
		} else if (code == KeyCode.DIGIT2) {
			displayTaskDetailSidePane(1);
		} else if (code == KeyCode.DIGIT3) {
			displayTaskDetailSidePane(2);
		} else if (code == KeyCode.DIGIT4) {
			displayTaskDetailSidePane(3);
		} else if (code == KeyCode.DIGIT5) {
			displayTaskDetailSidePane(4);
		} else if (code == KeyCode.DIGIT6) {
			displayTaskDetailSidePane(5);
		} else
			showControlKeyHint();
	}

	@FXML
	public void keyPressHandler(KeyEvent e) throws Exception {
		if (e.getCode() == KeyCode.PAGE_DOWN) {
			flipNextPage();
		} else if (e.getCode() == KeyCode.PAGE_UP) {
			flipPrevPage();
		} else if (e.isControlDown()) {
			hideControlKeyHint();
			processQuickAction(e.getCode());
		} else if (e.getCode() == KeyCode.F1) {
			if (Desktop.isDesktopSupported()) {
				URI helpManual = new File(URL_HELP_MANUAL).toURI();
				Desktop.getDesktop().browse(helpManual);
			}
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
		final TaskCardController controller = new TaskCardController(
				TaskCardController.CardSize.XLARGE);
		if (pageStart * ITEMS_PER_PAGE + index < taskData.size()) {
			controller.loadTask(taskData
					.get(pageStart * ITEMS_PER_PAGE + index));
			sidePane.getChildren().add(controller);
		}
	}

	private void restoreSidePane() {
		sidePane.getChildren().clear();
	}

	private void showControlKeyHint() {
		mainPaneHintLayer.setVisible(true);
		for (int i = 0, start = pageStart * ITEMS_PER_PAGE, size = taskData
				.size(); i < 6; ++i, ++start)
			mainPaneHintLayer.getChildren().get(i).setVisible(start < size);
		sidePaneHintLabel.setVisible(true);
	}

	private void hideControlKeyHint() {
		mainPaneHintLayer.setVisible(false);
		sidePaneHintLabel.setVisible(false);
	}
}
