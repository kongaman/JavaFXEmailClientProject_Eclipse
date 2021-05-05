package com.ck.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.ck.EmailManager;
import com.ck.controller.services.MessageRendererService;
import com.ck.model.EmailMessage;
import com.ck.view.ViewFactory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

public class EmailDetailsController extends BaseController implements Initializable {

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
		
		MessageRendererService messageRendererService = new MessageRendererService(webView.getEngine());
		messageRendererService.setEmailMessage(emailMessage);
		messageRendererService.restart();
		
	}

}
