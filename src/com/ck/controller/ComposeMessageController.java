package com.ck.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import com.ck.EmailManager;
import com.ck.controller.services.EmailSenderService;
import com.ck.model.EmailAccount;
import com.ck.view.ViewFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ComposeMessageController extends BaseController implements Initializable {
	
	private List<File> attachments = new ArrayList<>();
	
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
    private HBox hBoxAttachments;
    @FXML
    void attachButtonAction() {
    	FileChooser fileChooser = new FileChooser();
    	File selectedFile = fileChooser.showOpenDialog(null);
    	if(selectedFile != null) {
    		attachments.add(selectedFile);
    		showAttachment(selectedFile);
    	}
    }

    private void showAttachment(File selectedFile) {
    	AttachmentButton button;
		try {
			button = new AttachmentButton(selectedFile);
			hBoxAttachments.getChildren().add(button);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
    	
		
	}

	@FXML
    void sendButtonAction() {
    	EmailSenderService emailSenderService = new EmailSenderService(emailAccountChoice.getSelectionModel().getSelectedItem(),
    			subjectTextField.getText(), recipientTextField.getText(), htmlEditor.getHtmlText(), attachments);
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
	
	private class AttachmentButton extends Button  {
		
		private File attachment;

		public AttachmentButton(File attachedFile) throws MessagingException {
			this.attachment = attachedFile;
			this.setText("Delete: " + attachedFile.getName());
			
			this.setOnAction(event -> deleteAttachment(attachedFile));
		}
		
		private void deleteAttachment(File attachedFile) {
			attachments.remove(attachedFile);
			hBoxAttachments.getChildren().remove(this);
		}
		
	}

}
