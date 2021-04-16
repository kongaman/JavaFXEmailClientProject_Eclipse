package com.ck.controller.services;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

import com.ck.model.EmailTreeItem;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class FetchFoldersService extends Service<Void> {
	
	private Store store;
	private EmailTreeItem<String> foldersRoot;
	
	public FetchFoldersService(Store store, EmailTreeItem<String> foldersRoot) {
		this.store = store;
		this.foldersRoot = foldersRoot;
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				fetchFolders();
				return null;
			}
		};
	}

	protected void fetchFolders() throws MessagingException {
		Folder[] folders = store.getDefaultFolder().list();
		handleFolders(folders, foldersRoot);
	}

	private void handleFolders(Folder[] folders, EmailTreeItem<String> foldersRoot) throws MessagingException {
		for(Folder folder : folders) {
			EmailTreeItem<String> emailTreeItem = new EmailTreeItem<String>(folder.getName());
			foldersRoot.getChildren().add(emailTreeItem);
			foldersRoot.setExpanded(true);
			if(folder.getType() == Folder.HOLDS_FOLDERS) {
				Folder[] subfolders = folder.list();
				handleFolders(subfolders, emailTreeItem);
			}
		}
		
	}

	
}
