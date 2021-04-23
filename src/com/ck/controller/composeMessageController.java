package com.ck.controller;

import com.ck.EmailManager;
import com.ck.view.ViewFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

public class ComposeMessageController extends BaseController {
	
	@FXML
    private TextField recipientTextField;
    @FXML
    private TextField subjectTextField;
    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private Label errorLabel;

    @FXML
    void sendButtonAction(ActionEvent event) {

    }

	public ComposeMessageController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
		super(emailManager, viewFactory, fxmlName);
		// TODO Auto-generated constructor stub
	}

}
