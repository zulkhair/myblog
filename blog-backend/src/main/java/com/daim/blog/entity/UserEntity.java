package com.daim.blog.entity;

import com.daim.blog.infrastructure.util.CommonUtil;
import com.daim.blog.model.UserRegisterModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "webuser")
public class UserEntity {
    @Id
    @Column(name = "id", length = 32, nullable = false)
    private String id;

    @Column(name = "username", nullable = false, length = 20, unique = true)
    private String username;

    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "email", length = 64, nullable = false)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registration_timestamp")
    private Date registrationTimestamp;

    @Column(name = "url_path", length = 20, unique = true)
    private String urlPath;

    public UserEntity() {
        this.id = CommonUtil.generateId();
    }

    public UserEntity(UserRegisterModel model) {
        this(model.getUsername(), model.getName(), model.getEmail(), model.getUrlPath());
    }

    public UserEntity(String username, String name, String email, String urlPath) {
        this();
        this.username = username;
        this.name = name;
        this.email = email;
        this.registrationTimestamp = new Date();
        this.urlPath = urlPath;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUrlPath() {
        return urlPath;
    }
}