package com.ck.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.ck.EmailManager;
import com.ck.model.EmailAccount;
import com.ck.view.ViewFactory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

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
    	System.out.println(htmlEditor.getHtmlText());
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
