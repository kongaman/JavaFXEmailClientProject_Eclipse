package com.ck.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import com.ck.EmailManager;
import com.ck.controller.services.MessageRendererService;
import com.ck.model.EmailMessage;
import com.ck.view.ViewFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

public class EmailDetailsController extends BaseController implements Initializable {
	
	private String LOCATION_OF_DOWNLOADS = System.getProperty("user.home") + "/Downloads/";

	@FXML
    private Label attachmentLabel;

    @FXML
    private WebView webView;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label senderLabel;

    @FXML
    private HBox hBoxDownloads;


	public EmailDetailsController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
		super(emailManager, viewFactory, fxmlName);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		EmailMessage emailMessage = emailManager.getSelectedMessage();
		subjectLabel.setText(emailMessage.getSubject());
		senderLabel.setText(emailMessage.getSender());
		loadAttachments(emailMessage);
		
		MessageRendererService messageRendererService = new MessageRendererService(webView.getEngine());
		messageRendererService.setEmailMessage(emailMessage);
		messageRendererService.restart();
		
	}


	private void loadAttachments(EmailMessage emailMessage) {
		if(emailMessage.hasAttachments()) {
			for (MimeBodyPart mimeBodyPart : emailMessage.getAttachmentList()) {
				try {
					AttachmentButton button = new AttachmentButton(mimeBodyPart);
					hBoxDownloads.getChildren().add(button);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			attachmentLabel.setText("");
		}
	}
	
	private class AttachmentButton extends Button  {
		
		private MimeBodyPart mimeBodyPart;
		private String downloadedFilePath;

		public AttachmentButton(MimeBodyPart mimeBodyPart) throws MessagingException {
			this.mimeBodyPart = mimeBodyPart;
			this.setText(mimeBodyPart.getFileName());
			this.downloadedFilePath = LOCATION_OF_DOWNLOADS + mimeBodyPart.getFileName();
			
			this.setOnAction(event -> downloadAttachment());
		}
		
		private void downloadAttachment() {
			colorBlue();
			Service service = new Service() {
				@Override
				protected Task createTask() {
					return new Task() {
						@Override
						protected Object call() throws Exception {
							mimeBodyPart.saveFile(downloadedFilePath);
							return null;
						}
					};
				}
			};
			service.restart();
			service.setOnSucceeded(event -> colorGreen());
		}
		
		private void colorBlue() {
			this.setStyle("-fx-background-color: Blue");
		}
		
		private void colorGreen() {
			this.setStyle("-fx-background-color: Green");
		}
		
	}

}
