package com.example.demo.common.vo;

public class ResultVo<T> {

    private T body;

    private String code;

    private String msg;

    public ResultVo(T body) {
        super();
        this.body = body;
    }

    public ResultVo(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public ResultVo(String code, String msg, T body) {
        super();
        this.code = code;
        this.msg = msg;
        this.body = body;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
