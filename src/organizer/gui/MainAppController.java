package organizer.gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.List;

import organizer.logic.ResultSet;
import resources.ResourceUtil;
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
	private static final Logger LOGGER = Logger.getLogger(MainAppController.class.getName());
	private static final int ITEMS_PER_PAGE = 6;
	private static final String URL_HELP_MANUAL = "/resources/help_manual";
	private static final String URL_MANUAL_MAIN = "/Mnemonical User Manual.html";
	private static final double FADE_TRANSLATION_IN_TASKCARD_MILLIS = 300;
	private static final double FADE_TRANSLATION_IN_TASKCARD_START_ALPHA = 0.1;
	private static final double FADE_TRANSLATION_IN_TASKCARD_END_ALPHA = 1;
	private static final double FADE_TRANSLATION_IN_TASKCARD_SEPARATION_MILLIS = 15;

	private MainApp mainApp;
	private File tempDir;

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
	private void initialize() throws IOException, URISyntaxException {
		commandStatus.setText("");
		commandText.requestFocus();
		tempDir = ResourceUtil.makeTemporaryFromResourceFolder(URL_HELP_MANUAL);
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		updateTaskList();
	}

	@FXML
	public void performCommand() throws IOException {
		int lastVisitedPage = -1;
		final String commandString = commandText.textProperty().get();
		LOGGER.log(Level.INFO, "Command: ".concat(commandString));
		commandText.clear();
		
		final ResultSet rs = mainApp.performCommand(commandString);
		// grab the user view info before each command
		lastVisitedPage = pageStart;
		updateTaskList();
		
		if (rs.getCommandType() != null) {
			switch (rs.getCommandType()) {
			case ADD_TASK:
				pageStart = pageCount - 1;
				break;
			case VIEW_TASK:
			case CLEAR_TASK:
				pageStart = 0;
				break;
			default:
				pageStart = lastVisitedPage;
			}
		}
		
		lastVisitedPage = pageStart;

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
						LOGGER.throwing(getClass().getName(), "apply", e);
						LOGGER.severe("Initialising card controller failed");
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

	private void flipNextPage() {
		assert pageStart < pageCount;
		if (pageStart + 1 < pageCount) {
			++pageStart;
			LOGGER.info(String.format("NEXT => %d", pageStart));
			updatePage();
		}
	}

	private void flipPrevPage() {
		assert pageStart >= 0;
		if (pageStart > 0) {
			--pageStart;
			LOGGER.info(String.format("PREV => %d", pageStart));
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
			showHelpManual();
		}
	}

	@FXML
	public void keyReleaseHandler(KeyEvent e) {
		if (!e.isControlDown()) {
			hideControlKeyHint();
		}
	}
	
	private void showHelpManual() throws IOException {
		if(Desktop.isDesktopSupported()) {
			String url = tempDir.getAbsolutePath().concat(URL_MANUAL_MAIN);
			File tempPage = new File(url);
			Desktop.getDesktop().open(tempPage);
		}
	}

	private void displayTaskDetailSidePane(int index) throws IOException {
		LOGGER.info(String.format("MAINCONTROLLER: view detail for card %d", index));
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
		LOGGER.info("MAINCONTROLLER: clear detail view");
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
