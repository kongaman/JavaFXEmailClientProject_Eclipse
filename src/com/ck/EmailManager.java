package com.ck;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;

import com.ck.controller.services.FetchFoldersService;
import com.ck.controller.services.FolderUpdaterService;
import com.ck.model.EmailAccount;
import com.ck.model.EmailTreeItem;

public class EmailManager {
	
	private FolderUpdaterService folderUpdaterService;

    //Folder handling:
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<String>("");

    private List<Folder> foldersList = new ArrayList<>();
    
    public EmailManager() {
		this.folderUpdaterService = new FolderUpdaterService(foldersList);
		folderUpdaterService.start();
	}

	public void addEmailAccount(EmailAccount emailAccount){
    	EmailTreeItem<String> treeItem = new EmailTreeItem<String>(emailAccount.getAddress());
        FetchFoldersService fetchFoldersService = new FetchFoldersService(emailAccount.getStore(), treeItem, foldersList);
        fetchFoldersService.start();    
        foldersRoot.getChildren().add(treeItem);
    }
    
    public EmailTreeItem<String> getFoldersRoot(){
        return foldersRoot;
    }

	public List<Folder> getFoldersList() {
		return foldersList;
	}
    
}
