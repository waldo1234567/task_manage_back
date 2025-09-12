package com.task_management.demo.dto;

public class CreateActivityLogRequest {
    private String action;
    private String details;

    public CreateActivityLogRequest(){}
    public CreateActivityLogRequest(String action, String details){
        this.action=action;
        this.details=details;
    }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
