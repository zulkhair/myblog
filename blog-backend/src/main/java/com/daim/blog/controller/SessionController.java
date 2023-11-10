package com.daim.blog.controller;

import com.daim.blog.model.JwtRequestModel;
import com.daim.blog.model.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface SessionController {

    ResponseEntity<ResponseModel> createToken(JwtRequestModel request) throws Exception;
    ResponseEntity<ResponseModel> checkSession();
}
