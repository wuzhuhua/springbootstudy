package org.spring.springboot.utils;

import java.io.Serializable;

/**
 * http 响应数据结构
 *
 * @date 2017/6/6.
 */

public class ResponseBO<T> implements Serializable {

    public static final int ERROR = 0;
    public static final int SUCCESS = 1;

    private Integer completeCode;

    private String reasonCode;

    private String reasonMessage;

    private T data;

    /**
     * 外部不要直接new对象，建议用{@link ResponseBO#responseOK()}
     */
    public ResponseBO() {
        completeOK();
    }

    public Integer getCompleteCode() {
        return completeCode;
    }

    public ResponseBO completeOK(String msg) {
        return this.setCompleteCode(SUCCESS).setReasonMessage(msg);
    }

    public ResponseBO completeOK() {
        return this.setCompleteCode(SUCCESS);
    }

    public ResponseBO completeFail(String msg) {
        return this.setCompleteCode(ERROR).setReasonMessage(msg);
    }

    public ResponseBO setCompleteCode(Integer completeCode) {
        this.completeCode = completeCode;
        return this;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public ResponseBO setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
        return this;
    }

    public String getReasonMessage() {
        return reasonMessage;
    }

    public ResponseBO setReasonMessage(String reasonMessage) {
        this.reasonMessage = reasonMessage;
        return this;
    }


    public T getData() {
        return data;
    }

    public ResponseBO setData(T data) {
        this.data = data;
        return this;
    }

    public static ResponseBO responseOK(){
        return new ResponseBO();
    }

    public static ResponseBO responseFail(String msg){
        return new ResponseBO().completeFail(msg);
    }

    public static ResponseBO build(){
        return new ResponseBO();
    }

    @Override
    public String toString() {
        return "ResponseBO{" +
                "completeCode=" + completeCode +
                ", reasonCode='" + reasonCode + '\'' +
                ", reasonMessage='" + reasonMessage + '\'' +
                ", data=" + (data == null ? "null" : data.toString()) +
                '}';
    }
}
