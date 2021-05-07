package com.ck.controller.services;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

import com.ck.model.EmailMessage;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.web.WebEngine;

public class MessageRendererService extends Service {
	
	private EmailMessage emailMessage;
	private WebEngine webEngine;
	private StringBuffer stringBuffer;
	
	
	
	public MessageRendererService(WebEngine webEngine) {
		this.webEngine = webEngine;
		this.stringBuffer = new StringBuffer();
		this.setOnSucceeded(event -> {
			displayMessage();
		});
	}

	public void setEmailMessage(EmailMessage emailMessage) {
		this.emailMessage = emailMessage;
	}

	private void displayMessage() {
		webEngine.loadContent(stringBuffer.toString());
	}

	@Override
	protected Task createTask() {
		return new Task() {
			@Override
			protected Object call() throws Exception {
				try {
					loadMessage();
				} catch (Exception e) {
					// TODO: handle exception
				}
				return null;
			}
		};
	}
	
	private void loadMessage() throws MessagingException, IOException {
		stringBuffer.setLength(0); //clear stringbuffer
		Message message = emailMessage.getMessage();
		String contentType = message.getContentType();
		if(isSimpleType(contentType)) {
			stringBuffer.append(message.getContent().toString());
		} else if (isMultipartType(contentType)) {
			Multipart multipart = (Multipart) message.getContent();
			loadMultipart(multipart, stringBuffer);
		}
	}
	
	private void loadMultipart(Multipart multipart, StringBuffer stringBuffer) throws MessagingException, IOException {
		for (int i = multipart.getCount() - 1; i >= 0; i--) {
			BodyPart bodyPart = multipart.getBodyPart(i);
			String bodyPartContentType = bodyPart.getContentType();
			if(isSimpleType(bodyPartContentType)) {
				stringBuffer.append(bodyPart.getContent().toString());
			} else if(isMultipartType(bodyPartContentType) ) {
				Multipart multipart2 = (Multipart) bodyPart.getContent();
				loadMultipart(multipart2, stringBuffer);
			} else if (!isTextPlain(bodyPartContentType)) {
				// here we get the attachments
				MimeBodyPart mimeBodyPart = (MimeBodyPart) bodyPart;
				if(!emailMessage.getAttachmentList().contains(mimeBodyPart)) {
					emailMessage.addAttachment(mimeBodyPart);
				}
				
			}
		}
		
	}
	
	private boolean isTextPlain(String contentType) {
		return (contentType.contains("TEXT/PLAIN"));
	}
	
	private boolean isSimpleType(String contentType) {
		return (contentType.contains("TEXT/HTML") || contentType.contains("mixed") || contentType.contains("text"));
	}

	private boolean isMultipartType(String contentType) {
		return contentType.contains("multipart");
	}
}
