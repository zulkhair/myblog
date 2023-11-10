package com.daim.blog.controller.rest;

import com.daim.blog.controller.SessionController;
import com.daim.blog.infrastructure.jwt.TokenManager;
import com.daim.blog.model.JwtRequestModel;
import com.daim.blog.model.JwtResponseModel;
import com.daim.blog.model.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class SessionControllerRest implements SessionController {

    private UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;

    public SessionControllerRest(UserDetailsService userDetailsService, AuthenticationManager authenticationManager, TokenManager tokenManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
    }

    /**
     * Create JWT
     * @param request
     * @return
     */
    @PostMapping("/service/login")
    public ResponseEntity<ResponseModel> createToken(@RequestBody JwtRequestModel request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword())
            );
        } catch (DisabledException e) {
            ResponseModel.responseStatus(HttpStatus.UNAUTHORIZED);
        } catch (BadCredentialsException e) {
            ResponseModel.responseStatus(HttpStatus.UNAUTHORIZED);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwtToken = tokenManager.generateJwtToken(userDetails);
        return ResponseModel.responseOk( new JwtResponseModel(jwtToken));
    }

    /**
     * Check user session
     * @return
     */
    @GetMapping("/service/session")
    public ResponseEntity<ResponseModel> checkSession() {
        return ResponseModel.responseOk();
    }
}
