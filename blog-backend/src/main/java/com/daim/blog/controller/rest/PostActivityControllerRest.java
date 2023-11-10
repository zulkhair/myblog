package com.daim.blog.controller.rest;

import com.daim.blog.controller.PostActivityController;
import com.daim.blog.infrastructure.jwt.TokenManager;
import com.daim.blog.model.*;
import com.daim.blog.service.PostActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class PostActivityControllerRest implements PostActivityController {

    Logger logger = LoggerFactory.getLogger(PostActivityControllerRest.class);

    private PostActivityService postActivityService;
    private TokenManager tokenManager;

    public PostActivityControllerRest(PostActivityService postActivityService, TokenManager tokenManager) {
        this.postActivityService = postActivityService;
        this.tokenManager = tokenManager;
    }

    @Override
    @GetMapping("/service/post/view")
    public ResponseEntity<ResponseModel> viewPost(@RequestParam("urlPath") @Nullable String urlPath) {
        List<PostResponseModel> postResponseModels;
        if (urlPath != null) {
            // find with unique url path belong to user
            postResponseModels = postActivityService.viewPostByUrlPath(urlPath);
        } else {
            // Find all post
            postResponseModels = postActivityService.viewPost();
        }
        return ResponseModel.responseOk(postResponseModels);
    }

    @Override
    @GetMapping("/service/post/admin")
    public ResponseEntity<ResponseModel> viewPostAdmin(@RequestHeader("Authorization") @Nullable String token) {
        String username = tokenManager.getUsernameFromTokenBearer(token);

        // View all post belong to logged in user
        List<PostResponseModel> postResponseModels = postActivityService.viewPost(username);

        return ResponseModel.responseOk(postResponseModels);
    }

    @Override
    @PostMapping("/service/post/create")
    public ResponseEntity<ResponseModel> createPost(@RequestHeader("Authorization") String token, @RequestBody CreatePostModel createPostModel) {
        String username = tokenManager.getUsernameFromTokenBearer(token);
        postActivityService.createPost(createPostModel, username);

        return ResponseModel.responseOk();
    }

    @Override
    @PostMapping("/service/post/edit")
    public ResponseEntity<ResponseModel> editPost(@RequestHeader("Authorization") String token, @RequestBody EditPostModel editPostModel) {
        try {
            String username = tokenManager.getUsernameFromTokenBearer(token);
            postActivityService.editPost(editPostModel, username);

            return ResponseModel.responseOk();
        } catch (NoSuchElementException e) {
            return ResponseModel.responseStatus(HttpStatus.BAD_REQUEST, "Post Not Found");
        }
    }

    @Override
    @DeleteMapping("/service/post/delete/{postId}")
    public ResponseEntity<ResponseModel> deletePost(@RequestHeader("Authorization") String token, @PathVariable("postId") String postId) {
        String username = tokenManager.getUsernameFromTokenBearer(token);
        try {
            postActivityService.deletePost(postId, username);
            return ResponseModel.responseOk();
        } catch (NoSuchElementException e) {
            return ResponseModel.responseStatus(HttpStatus.BAD_REQUEST, "Post Not Found");
        } catch (SQLException e) {
            logger.error("Error when delete post : ", e);
            return ResponseModel.responseStatus(HttpStatus.INTERNAL_SERVER_ERROR, "Post Not Found");
        }
    }

    @Override
    @GetMapping("/service/post/view/{postId}")
    public ResponseEntity<ResponseModel> viewDetailPost(@RequestHeader("Authorization") @Nullable String token, @PathVariable("postId") String postId, HttpServletRequest request) {
        String username = null;
        // Check jwt token because, if user not logged in the token will be null
        if (token != null && token.length() > 7){
            username = tokenManager.getUsernameFromTokenBearer(token);
        }

        String ipAddress = request.getRemoteAddr();
        try{
            return ResponseModel.responseOk(postActivityService.viewDetailPost(postId, ipAddress, username));
        }catch (NoSuchElementException e){
            return ResponseModel.responseStatus(HttpStatus.BAD_REQUEST, "Post Not Found");
        }
    }

    @Override
    @PostMapping("/service/post/comment")
    public ResponseEntity<ResponseModel> createComment(@RequestBody CreateCommentModel createCommentModel) {
        postActivityService.createComment(createCommentModel);

        return ResponseModel.responseOk();
    }

    @Override
    @DeleteMapping("/service/post/comment/delete/{commentId}")
    public ResponseEntity<ResponseModel> deleteComment(@RequestHeader("Authorization") String token, @PathVariable("commentId") String commentId) {
        String username = tokenManager.getUsernameFromTokenBearer(token);
        postActivityService.deleteComment(commentId, username);

        return ResponseModel.responseOk();
    }
}
