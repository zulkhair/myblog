package com.daim.blog.entity;

import com.daim.blog.infrastructure.util.CommonUtil;
import com.daim.blog.model.CreatePostModel;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "post")
public class PostEntity {
    @Id
    @Column(name = "id", length = 32, nullable = false)
    private String id;

    @Column(name = "title", length = 64, nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private byte[] content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "post_time")
    private Date postTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "webuser_id", updatable = false, insertable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name="webuser_id", nullable=false)
    private UserEntity user;

    @OneToMany(mappedBy="post", fetch = FetchType.LAZY)
    public Collection<CounterEntity> counters;

    public PostEntity(String title, byte[] content, UserEntity user) {
        this();
        this.title = title;
        this.content = content;
        this.postTime = new Date();
        this.user = user;
        if (user != null){
            this.userId = user.getId();
        }
    }

    public PostEntity() {
        this.id = CommonUtil.generateId();
    }

    public PostEntity(CreatePostModel createPostModel, UserEntity user){
        this(createPostModel.getTitle(), createPostModel.getContent().getBytes(), user);
    }

    public String getId() {
        return id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Date getPostTime() {
        return postTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
