package com.daim.blog.controller.rest;

import com.daim.blog.controller.UserController;
import com.daim.blog.model.ResponseModel;
import com.daim.blog.model.UserRegisterModel;
import com.daim.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityExistsException;

@RestController
public class UserControllerRest implements UserController {

    private final UserService userService;

    public UserControllerRest(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register User
     * @param userRegisterModel
     * @return
     */
    @PostMapping(value = "/service/user/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> register(@RequestBody UserRegisterModel userRegisterModel) {
        try {
            userService.register(userRegisterModel);
            return ResponseModel.responseOk();
        }catch (EntityExistsException e){
            return ResponseModel.responseStatus(HttpStatus.BAD_REQUEST, "Username is already taken");
        }
    }
}
