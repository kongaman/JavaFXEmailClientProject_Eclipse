package com.ck;

import com.ck.model.EmailAccount;
import com.ck.model.EmailTreeItem;

import javafx.scene.control.TreeItem;

public class EmailManager {

    //Folder handling:
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<String>("");

    public EmailTreeItem<String> getFoldersRoot(){
        return foldersRoot;
    }

    public void addEmailAccount(EmailAccount emailAccount){
        TreeItem<String> treeItem = new TreeItem<String>(emailAccount.getAddress());
        treeItem.setExpanded(true);
            treeItem.getChildren().add(new TreeItem<String>("INBOX"));
            treeItem.getChildren().add(new TreeItem<String>("Sent"));
            treeItem.getChildren().add(new TreeItem<String>("Folder1"));
            treeItem.getChildren().add(new TreeItem<String>("Spam"));
        foldersRoot.getChildren().add(treeItem);
    }
}
