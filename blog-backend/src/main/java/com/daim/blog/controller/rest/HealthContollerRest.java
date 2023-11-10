package com.daim.blog.controller.rest;

import com.daim.blog.controller.HealthController;
import com.daim.blog.model.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthContollerRest implements HealthController {

    /**
     * API For Testing
     * @return
     */
    @GetMapping("/service/ping")
    public ResponseEntity<ResponseModel> check() {
        return ResponseModel.responseOk("Service is running !!!");
    }
}
