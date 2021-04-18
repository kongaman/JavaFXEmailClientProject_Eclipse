package com.ck.controller.services;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import com.ck.model.EmailMessage;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;

public class MessageRendererService extends Service<EmailMessage>{
	
	private EmailMessage emailMessage;
	private WebEngine webEngine;
	private StringBuffer stringBuffer;
	
	
	
	public MessageRendererService(WebEngine webEngine) {
		this.webEngine = webEngine;
		this.stringBuffer = new StringBuffer();
	}

	public void setEmailMessage(EmailMessage emailMessage) {
		this.emailMessage = emailMessage;
	}



	@Override
	protected Task<EmailMessage> createTask() {
		return new Task<EmailMessage>() {
			@Override
			protected EmailMessage call() throws Exception {
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
			for (int i = multipart.getCount() - 1; i >= 0; i--) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				String bodyPartContentType = bodyPart.getContentType();
				if(isSimpleType(bodyPartContentType)) {
					stringBuffer.append(bodyPart.getContent().toString());
				}
			}
		}
	}
	
	private boolean isSimpleType(String contentType) {
		return (contentType.contains("TEXT/HTML") || contentType.contains("mixed") || contentType.contains("text"));
	}

	private boolean isMultipartType(String contentType) {
		return contentType.contains("multipart");
	}
}
