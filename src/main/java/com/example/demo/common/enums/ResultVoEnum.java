package com.example.demo.common.enums;

public enum ResultVoEnum {
    SUCCESS("000", "SUCCESS"),
    FAILED("999", "FAILED"),
    ILLEGAL_PARAMETER("602", "ILLEGAL_PARAMETER");

    private String code;

    private String msg;

    ResultVoEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg(String code) {
        for (ResultVoEnum voEnum : ResultVoEnum.values()) {
            if (code.equals(voEnum.getCode())) {
                return voEnum.getMsg();
            }
        }
        return null;
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
