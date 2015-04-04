package organizer.gui;

import java.io.IOException;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class HelpDialogController {
	
	@FXML
	private TextArea manualTextArea;

	public HelpDialogController() {
	}
	
	@FXML
	private void initialize() {
		try {
			manualTextArea.setText(
					new Scanner(
							getClass()
							.getResource("Manual.txt")
							.openStream())
					.useDelimiter("\\A")
					.next());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void closeDialog() {
		((Stage)manualTextArea.getScene().getWindow()).close();
	}
}
