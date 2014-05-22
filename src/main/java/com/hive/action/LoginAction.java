package com.hive.action;

/**
 * Created by mika on 14-5-13.
 */
public class LoginAction {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String execute() throws Exception{
        if(getUsername().equals("mika") && getPassword().equals("mika")){
            return "success";
        } else {
            return "error";
        }
    }
}
