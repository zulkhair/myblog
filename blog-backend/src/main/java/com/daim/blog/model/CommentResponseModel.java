package com.daim.blog.model;

import com.daim.blog.entity.CommentEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseModel {
    private String commentId;
    private String content;
    private Date postTime;
    private String name;

    public CommentResponseModel(){

    }

    public CommentResponseModel(CommentEntity commentEntity){
        this.commentId = commentEntity.getId();
        this.content = commentEntity.getContent();
        this.postTime = commentEntity.getPostTime();
        this.name = commentEntity.getName();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommentId() {
        return commentId;
    }
}
