package com.gcsun.bean;

/**
 * Created by 11981 on 2017/9/17.
 */
public class Request {


    private String requestMethod;
    private String requestPath;
    public Request(String requestMethod, String requestPath) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }
    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }
}
