package com.ck.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.ck.EmailManager;
import com.ck.controller.services.EmailSenderService;
import com.ck.model.EmailAccount;
import com.ck.view.ViewFactory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class ComposeMessageController extends BaseController implements Initializable {
	
	@FXML
    private TextField recipientTextField;
    @FXML
    private TextField subjectTextField;
    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private Label errorLabel;
    @FXML
    private ChoiceBox<EmailAccount> emailAccountChoice;

    @FXML
    void sendButtonAction() {
    	EmailSenderService emailSenderService = new EmailSenderService(emailAccountChoice.getSelectionModel().getSelectedItem(),
    			subjectTextField.getText(), recipientTextField.getText(), htmlEditor.getHtmlText());
    	emailSenderService.start();
    	emailSenderService.setOnSucceeded(e -> {
    		EmailSendingResult emailSendingResult = emailSenderService.getValue();
    		switch (emailSendingResult) {
			case SUCCESS:
				Stage stage = (Stage) recipientTextField.getScene().getWindow();
				viewFactory.closeStage(stage);
				break;
			case FAILED_BY_PROVIDER:
				errorLabel.setText("provider error");
				break;
			case FAILED_BY_UNEXPECTED_ERROR:
				errorLabel.setText("unexpected error");
				break;
			default:
				break;
			}
    	});
    }

	public ComposeMessageController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
		super(emailManager, viewFactory, fxmlName);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		emailAccountChoice.setItems(emailManager.getEmailAccounts());
		emailAccountChoice.setValue(emailManager.getEmailAccounts().get(0));
	}

}
