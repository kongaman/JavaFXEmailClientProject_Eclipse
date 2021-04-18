package com.ck.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.ck.EmailManager;
import com.ck.controller.services.MessageRendererService;
import com.ck.model.EmailMessage;
import com.ck.model.EmailTreeItem;
import com.ck.model.SizeInteger;
import com.ck.view.ViewFactory;

public class MainWindowController extends BaseController implements Initializable {

    @FXML
    private TreeView<String> emailsTreeView;

    @FXML
    private TableView<EmailMessage> emailsTableView;
    
    @FXML
    private TableColumn<EmailMessage, String> senderCol;

    @FXML
    private TableColumn<EmailMessage, String> subjectCol;

    @FXML
    private TableColumn<EmailMessage, String> recipientCol;

    @FXML
    private TableColumn<EmailMessage, SizeInteger> sizeCol;

    @FXML
    private TableColumn<EmailMessage, Date> dateCol;

    @FXML
    private WebView emailWebView;
    
    private MessageRendererService messageRendererService;

    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void optionsAction() {
        viewFactory.showOptionsWindow();
    }
    @FXML
    void addAccountAction() {
        viewFactory.showLoginWindow();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpEmailsTreeView();
        setUpEmailsTableView();
        setUpFolderSelection();
        setUpBoldRows();
        setUpMessageRendererService();
        setUpMessageSelection();
    }

    private void setUpMessageSelection() {
    	emailsTableView.setOnMouseClicked(event -> {
    		EmailMessage emailMessage = emailsTableView.getSelectionModel().getSelectedItem();
    		if( emailMessage != null) {
    			messageRendererService.setEmailMessage(emailMessage);
    			messageRendererService.restart();
    		}
    	});
	}

	private void setUpMessageRendererService() {
    	messageRendererService = new MessageRendererService(emailWebView.getEngine());
	}

	private void setUpBoldRows() {
    	emailsTableView.setRowFactory(new Callback<TableView<EmailMessage>, TableRow<EmailMessage>>() {
			@Override
			public TableRow<EmailMessage> call(TableView<EmailMessage> arg0) {
				return new TableRow<EmailMessage>() {
					@Override
					protected void updateItem(EmailMessage item, boolean empty) {
						super.updateItem(item, empty);
						if(item != null) {
							String style = (item.isRead()) ? "" : "-fx-font-weight: bold";
							setStyle(style);
						}
					}
				};
			}
		});
	}

	private void setUpFolderSelection() {
    	emailsTreeView.setOnMouseClicked(event -> {
    		EmailTreeItem<String> item = (EmailTreeItem<String>) emailsTreeView.getSelectionModel().getSelectedItem();
    		if(item != null) {
    			emailsTableView.setItems(item.getEmailMessages());
    		}
    	});
	}

	private void setUpEmailsTableView() {
    	senderCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("sender"));
    	subjectCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("subject"));
    	recipientCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("recipient"));
    	sizeCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, SizeInteger>("size"));
    	dateCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, Date>("date"));
	}

	private void setUpEmailsTreeView() {
        emailsTreeView.setRoot(emailManager.getFoldersRoot());
        emailsTreeView.setShowRoot(false);
    }
}
