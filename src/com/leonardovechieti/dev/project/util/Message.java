package com.leonardovechieti.dev.project.util;

import com.leonardovechieti.dev.project.views.MessageView;

public class Message {
    private String title;
    private String message;
    private String type;

    public Message(String title, String message, String type) {
        this.title = title;
        this.message = message;
        this.type = type;
        execute();
    }
    private void execute() {
        switch (type) {
            case "error":
                MessageView messageView = new MessageView(title, message, type);
                break;
            case "warning":

                break;
            case "info":
                //JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
                break;
            case "plain":
                //JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                break;
            default:
                //JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                break;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
