package com.ck.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class EmailMessage {
	
	private SimpleStringProperty subject;
	private SimpleStringProperty sender;
	private SimpleStringProperty recipient;
	private SimpleObjectProperty<SizeInteger> size;
	private SimpleObjectProperty<Date> date;
	private boolean isRead;
	private Message message;
	private List<MimeBodyPart> attachmentList = new ArrayList<>();
	private boolean hasAttachments = false;
	
	public EmailMessage(String subject, String sender, String recipient, int size, Date date, boolean isRead, Message message) {
		this.subject = new SimpleStringProperty(subject);
		this.sender = new SimpleStringProperty(sender);
		this.recipient = new SimpleStringProperty(recipient);
		this.size = new SimpleObjectProperty<SizeInteger>(new SizeInteger(size));
		this.date = new SimpleObjectProperty<Date>(date);
		this.isRead = isRead;
		this.message = message;
	}
	
	public void addAttachment(MimeBodyPart mimeBodyPart) {
		hasAttachments = true;
		attachmentList.add(mimeBodyPart);
		try {
			System.out.println("added attachment: " + mimeBodyPart.getFileName());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	public String getSubject() {
		return this.subject.get();
	}
	
	public String getSender() {
		return this.sender.get();
	}
	
	public String getRecipient() {
		return this.recipient.get();
	}
	
	public SizeInteger getSize() {
		return this.size.get();
	}
	
	public Date getDate() {
		return this.date.get();
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public Message getMessage() {
		return message;
	}

	public boolean hasAttachments() {
		return hasAttachments;
	}

	public List<MimeBodyPart> getAttachmentList() {
		return attachmentList;
	}

}
