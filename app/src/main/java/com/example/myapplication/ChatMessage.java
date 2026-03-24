package com.example.myapplication;
public class ChatMessage {
    private String message;
    private String time;
    private boolean isUser; // true ise kullanıcı, false ise bot (PawBot)

    public ChatMessage(String message, String time, boolean isUser) {
        this.message = message;
        this.time = time;
        this.isUser = isUser;
    }

    // Getter metotları
    public String getMessage() { return message; }
    public String getTime() { return time; }
    public boolean isUser() { return isUser; }
}
