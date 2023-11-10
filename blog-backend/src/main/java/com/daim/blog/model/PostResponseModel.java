package com.daim.blog.model;

import com.daim.blog.entity.PostEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseModel {
    private String postId;
    private String title;
    private String content;
    private Date postTime;
    private Date updateTime;
    private String author;
    private List<CommentResponseModel> comments;
    private Long viewCount;
    private Boolean deleteComment;

    public PostResponseModel(){

    }

    public PostResponseModel(String postId, String title, byte[] content, Date postTime, Date updateTime, String author, Long viewCount) {
        this.postId = postId;
        this.title = title;
        this.content = new String(content);
        this.postTime = postTime;
        this.updateTime = updateTime;
        this.author = author;
        if (viewCount != null){
            this.viewCount = viewCount;
        }else{
            this.viewCount = 0L;
        }
    }

    public PostResponseModel(PostEntity postEntity) {
        this.postId = postEntity.getId();
        this.title = postEntity.getTitle();
        this.content = new String(postEntity.getContent());
        this.postTime = postEntity.getPostTime();
        this.updateTime = postEntity.getUpdateTime();
        this.author = postEntity.getUser().getName();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<CommentResponseModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponseModel> comments) {
        this.comments = comments;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public String getPostId() {
        return postId;
    }

    public Boolean getDeleteComment() {
        return deleteComment;
    }

    public void setDeleteComment(Boolean deleteComment) {
        this.deleteComment = deleteComment;
    }
}
