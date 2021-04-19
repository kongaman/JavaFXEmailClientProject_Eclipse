package com.ck.controller.services;

import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

import com.ck.model.EmailTreeItem;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class FetchFoldersService extends Service<Void> {
	
	private Store store;
	private EmailTreeItem<String> foldersRoot;
	private List<Folder> foldersList;
	
	public FetchFoldersService(Store store, EmailTreeItem<String> foldersRoot, List<Folder> foldersList) {
		this.store = store;
		this.foldersRoot = foldersRoot;
		this.foldersList = foldersList;
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
			foldersList.add(folder);
			EmailTreeItem<String> emailTreeItem = new EmailTreeItem<String>(folder.getName());
			foldersRoot.getChildren().add(emailTreeItem);
			foldersRoot.setExpanded(true);
			fetchMessagesOnFolder(folder, emailTreeItem);
			addMessageListenertoFolder(folder, emailTreeItem);
			if(folder.getType() == Folder.HOLDS_FOLDERS) {
				Folder[] subfolders = folder.list();
				handleFolders(subfolders, emailTreeItem);
			}
		}
	}

	private void addMessageListenertoFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
		folder.addMessageCountListener(new MessageCountListener() {
			@Override
			public void messagesAdded(MessageCountEvent e) {
				for (int i = 0; i < e.getMessages().length; i++) {
					try {
						Message message = folder.getMessage(folder.getMessageCount() - i);
						emailTreeItem.addEmailToTop(message);
					} catch (MessagingException e1) {
						e1.printStackTrace();
					}
				}
				System.out.println("message added event " + e);
			}
			@Override
			public void messagesRemoved(MessageCountEvent e) {
				System.out.println("message removed event " + e);
			}
		});
	}

	private void fetchMessagesOnFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
		Service fetchMessagesService = new Service() {
			@Override
			protected Task createTask() {
				return new Task() {
					@Override
					protected Object call() throws Exception {
						if(folder.getType() != Folder.HOLDS_FOLDERS) {
							folder.open(Folder.READ_WRITE);
							int folderSize = folder.getMessageCount();
							for(int i = folderSize; i > 0; i--) {
								emailTreeItem.addEmail(folder.getMessage(i));
							}
						}
						return null;
					}
				};
			}
			
		};
		fetchMessagesService.start();
	}
}
