package com.daim.blog.entity.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CounterId implements Serializable {

    @Column(name = "post_id", length = 32, insertable = false, updatable = false)
    private String postId;

    @Column(name = "ip_address", length = 16)
    private String ipAddress;

    public CounterId(String postId, String ipAddress) {
        this.postId = postId;
        this.ipAddress = ipAddress;
    }

    public CounterId() {

    }
}
