package com.ck;

import java.util.ArrayList;
import java.util.List;

import com.ck.controller.persistence.PersistenceAccess;
import com.ck.controller.persistence.ValidAccount;
import com.ck.controller.services.LoginService;
import com.ck.model.EmailAccount;
import com.ck.view.ViewFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Launcher extends Application {
	
	private PersistenceAccess persistenceAccess = new PersistenceAccess();
	private EmailManager emailManager = new EmailManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ViewFactory viewFactory = new ViewFactory(emailManager);
        List<ValidAccount> validAccountList = persistenceAccess.loadFromPersistence();
		if(validAccountList.size() > 0) {
			viewFactory.showMainWindow();
			for (ValidAccount validAccount : validAccountList) {
				EmailAccount emailAccount = new EmailAccount(validAccount.getAdress(), validAccount.getPassword());
				LoginService loginService = new LoginService(emailAccount, emailManager);
				loginService.start();
			}
		} else {
			viewFactory.showLoginWindow();
		}
        viewFactory.updateStyles();
    }

	@Override
	public void stop() throws Exception {
		List<ValidAccount> validAccountList = new ArrayList<>();
		for (EmailAccount emailAccount : emailManager.getEmailAccounts()) {
			validAccountList.add(new ValidAccount(emailAccount.getAddress(), emailAccount.getPassword()));
		}
		persistenceAccess.saveToPersistence(validAccountList);
	}
    
    
}
