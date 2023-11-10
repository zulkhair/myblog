package com.daim.blog.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel {
    private String message;
    private Object data;

    public ResponseModel(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public static ResponseEntity<ResponseModel> responseOk(String message, Object data){
        return ResponseEntity.ok(new ResponseModel(message, data));
    }

    public static ResponseEntity<ResponseModel> responseOk(){
        return responseOk(null, null);
    }

    public static ResponseEntity<ResponseModel> responseOk(String message){
        return responseOk(message, null);
    }

    public static ResponseEntity<ResponseModel> responseOk(Object data){
        return responseOk(null, data);
    }

    public static ResponseEntity<ResponseModel> responseStatus(HttpStatus httpStatus, String message, Object data){
        return ResponseEntity.status(httpStatus).body(new ResponseModel(message, data));
    }

    public static ResponseEntity<ResponseModel> responseStatus(HttpStatus httpStatus, String message){
        return responseStatus(httpStatus, message, null);
    }

//    public static ResponseEntity<ResponseModel> responseStatus(HttpStatus httpStatus, Object data){
//        return responseStatus(httpStatus, null, data);
//    }

    public static ResponseEntity<ResponseModel> responseStatus(HttpStatus httpStatus){
        return responseStatus(httpStatus, null, null);
    }

    public String getMessage() {
        return message;
    }

//    public void setMessage(String message) {
//        this.message = message;
//    }

    public Object getData() {
        return data;
    }

//    public void setData(Object data) {
//        this.data = data;
//    }
}
