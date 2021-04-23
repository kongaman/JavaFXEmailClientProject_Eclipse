package com.ck;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Flags;
import javax.mail.Folder;

import com.ck.controller.services.FetchFoldersService;
import com.ck.controller.services.FolderUpdaterService;
import com.ck.model.EmailAccount;
import com.ck.model.EmailMessage;
import com.ck.model.EmailTreeItem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmailManager {
	
	private EmailMessage selectedMessage;
	private EmailTreeItem<String> selectedFolder;
	private FolderUpdaterService folderUpdaterService;
	private ObservableList<EmailAccount> emailAccounts = FXCollections.observableArrayList();
    //Folder handling:
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<String>("");
    private List<Folder> foldersList = new ArrayList<>();
    
    public EmailManager() {
		this.folderUpdaterService = new FolderUpdaterService(foldersList);
		folderUpdaterService.start();
	}

	public void addEmailAccount(EmailAccount emailAccount){
		emailAccounts.add(emailAccount);
    	EmailTreeItem<String> treeItem = new EmailTreeItem<String>(emailAccount.getAddress());
        FetchFoldersService fetchFoldersService = new FetchFoldersService(emailAccount.getStore(), treeItem, foldersList);
        fetchFoldersService.start();    
        foldersRoot.getChildren().add(treeItem);
    }
	
	public void setRead() {
		try {
			selectedMessage.setRead(true);
			selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
			selectedFolder.decrementMessageCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setUnRead() {
		try {
			selectedMessage.setRead(false);
			selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, false);
			selectedFolder.incrementMessageCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteSelectedMessage() {
		try {
			selectedMessage.getMessage().setFlag(Flags.Flag.DELETED, true);
			selectedFolder.getEmailMessages().remove(selectedMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public EmailTreeItem<String> getFoldersRoot(){
        return foldersRoot;
    }

	public List<Folder> getFoldersList() {
		return foldersList;
	}

	public EmailMessage getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(EmailMessage selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

	public EmailTreeItem<String> getSelectedFolder() {
		return selectedFolder;
	}

	public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
		this.selectedFolder = selectedFolder;
	}

	public ObservableList<EmailAccount> getEmailAccounts() {
		return emailAccounts;
	}
	

}
