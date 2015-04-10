package organizer.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import organizer.logic.*;
import organizer.parser.*;

//@author A0113627L
public class MainApp extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;
    
    private List<TaskItem> taskData = new ArrayList<>();
    private String currentCommandStatus = "";
	
	//to check that it is a add successful operation, so page can jumped to the last one.
	private static final String ADD_SUCCESS = "View Tasks Filter: all \nStatus: Add task operation is successful!";
    
    private CommandParser commandParser = new CommandParser();
    private List<Task> tasks;
    
    private MainAppController controller;

    public MainApp() throws IOException {
        tasks = commandParser.loadStorage();
        fillTaskList();
    }
    
    private void fillTaskList() {
        taskData.clear();
        int counter = 0;
        for (Task task : tasks) {
        	taskData.add(new TaskItem(task, ++counter));
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MnemoniCal");
        this.primaryStage.setResizable(false);
        
        initRootLayout();
        
        showAllTasks();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void initRootLayout() {
        try {
            final FXMLLoader loader = new FXMLLoader();
            final URL url = MainApp.class.getResource("MainApp.fxml");
            loader.setLocation(url);
            rootLayout = (AnchorPane) loader.load();
            
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showAllTasks() {
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public List<TaskItem> getTaskData() {
        return taskData;
    }
    
    public String getCurrentCommandStatus() {
    	return currentCommandStatus;
    }
    
    public boolean performCommand(String commandString) {
    	boolean isAdd = false;
        try {
        	ResultSet returnResult = commandParser.executeCommand(commandString);
            tasks = returnResult.getReturnList();
            currentCommandStatus = returnResult.getOpStatus();
            isAdd = currentCommandStatus.equals(ADD_SUCCESS);
            fillTaskList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return isAdd;
    }
    
    
    public void showHelpDialog() throws IOException {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("HelpDialog.fxml"));
		final AnchorPane root = loader.load();
		final Scene scene = new Scene(root);
		final Stage dialog = new Stage();
		dialog.initStyle(StageStyle.UTILITY);
		dialog.setScene(scene);
		dialog.show();
    }
}
