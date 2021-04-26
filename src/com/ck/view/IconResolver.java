package com.ck.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconResolver {
	
	public Node getIconForFolder(String folderName ) {
		String lowerCaseFolderName = folderName.toLowerCase();
		ImageView imageView;
		try {
			if(lowerCaseFolderName.contains("@")) {
				imageView = new ImageView(new Image(getClass().getResourceAsStream("icons/email.png")));
			} else {
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return imageView;
	}

}
