package com.daim.blog.controller;

import com.daim.blog.model.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface PostActivityController {

    ResponseEntity<ResponseModel> viewPost(String urlPath);
    ResponseEntity<ResponseModel> viewPostAdmin(String token);
    ResponseEntity<ResponseModel> createPost(String token, CreatePostModel createPostModel);
    ResponseEntity<ResponseModel> editPost(String token, EditPostModel editPostModel);
    ResponseEntity<ResponseModel> deletePost(String token, String postId);
    ResponseEntity<ResponseModel> viewDetailPost(String token, String postId, HttpServletRequest request);
    ResponseEntity<ResponseModel> createComment(CreateCommentModel createCommentModel);
    ResponseEntity<ResponseModel> deleteComment(String token, String commentId);
}
