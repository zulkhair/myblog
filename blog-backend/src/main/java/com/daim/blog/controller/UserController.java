package com.daim.blog.controller;

import com.daim.blog.model.ResponseModel;
import com.daim.blog.model.UserRegisterModel;
import org.springframework.http.ResponseEntity;

public interface UserController {

    ResponseEntity<ResponseModel> register(UserRegisterModel userRegisterModel);
}
