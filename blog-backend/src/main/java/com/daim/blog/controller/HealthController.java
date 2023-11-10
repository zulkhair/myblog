package com.daim.blog.controller;

import com.daim.blog.model.ResponseModel;
import org.springframework.http.ResponseEntity;

public interface HealthController {

    ResponseEntity<ResponseModel> check();
}
