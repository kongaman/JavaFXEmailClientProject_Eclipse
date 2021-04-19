module JavaFxEmailClient {
	requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;
    requires activation;
    requires java.mail;
	requires javafx.base;

    opens com.ck;
    opens com.ck.view;
    opens com.ck.controller;
    opens com.ck.model;
}