package com.daim.blog.entity;

import com.daim.blog.infrastructure.util.CommonUtil;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comment")
public class CommentEntity {

    @Id
    @Column(name = "id", length = 32, nullable = false)
    private String id;

    @Column(name = "content", length = 512, nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "post_time", nullable = false)
    private Date postTime;

    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @Column(name = "post_id", insertable = false, updatable = false)
    private String postId;

    @ManyToOne
    @JoinColumn(name="post_id", nullable=false)
    private PostEntity post;

    public CommentEntity(){
        this.id = CommonUtil.generateId();
    }

    public CommentEntity(String content, String name, PostEntity post) {
        this();
        this.content = content;
        this.postTime = new Date();
        this.name = name;
        this.post = post;
        if (post != null){
            this.postId = post.getId();
        }
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Date getPostTime() {
        return postTime;
    }

    public String getName() {
        return name;
    }
}
