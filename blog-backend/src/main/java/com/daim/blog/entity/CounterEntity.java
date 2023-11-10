package com.daim.blog.entity;

import com.daim.blog.entity.id.CounterId;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "counter")
public class CounterEntity {
    @EmbeddedId
    private CounterId id;

    @Column(name = "count")
    private int count;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_time")
    private Date lastUpdatedTime;

    @MapsId("postId")
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    @ManyToOne
    public PostEntity post;

    public CounterEntity(CounterId id, int count) {
        this.id = id;
        this.count = count;
        this.lastUpdatedTime = new Date();
    }

    public CounterEntity() {

    }

    public CounterId getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }
}
