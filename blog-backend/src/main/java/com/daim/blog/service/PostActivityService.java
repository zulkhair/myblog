package com.daim.blog.service;

import com.daim.blog.model.CreateCommentModel;
import com.daim.blog.model.CreatePostModel;
import com.daim.blog.model.EditPostModel;
import com.daim.blog.model.PostResponseModel;

import java.sql.SQLException;
import java.util.List;

public interface PostActivityService {

    List<PostResponseModel> viewPost();
    List<PostResponseModel> viewPost(String username);
    List<PostResponseModel> viewPostByUrlPath(String urlPath);
    void createPost(CreatePostModel createPostModel, String username);
    void editPost(EditPostModel editPostModel, String username);
    void deletePost(String postId, String username) throws SQLException;
    PostResponseModel viewDetailPost(String postId, String ipAddress, String username);
    void createComment(CreateCommentModel createCommentModel);
    void deleteComment(String commentId, String username);
}
