package com.daim.blog.repository;

import com.daim.blog.entity.PostEntity;
import com.daim.blog.model.PostResponseModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends CrudRepository<PostEntity, String> {

    @Transactional
    public List<PostEntity> findAllByOrderByPostTimeDesc();
    @Transactional
    public List<PostEntity> findByUserIdEqualsOrderByPostTimeDesc(String userId);
    @Transactional
    public PostEntity findByIdAndUserId(String postId, String userId);

    @Transactional
    @Query("SELECT p FROM PostEntity p JOIN p.user u WHERE u.urlPath = :urlPath")
    public List<PostEntity> findByUrlPath(@Param("urlPath") String urlPath);

    @Transactional
    @Query("SELECT new com.daim.blog.model.PostResponseModel(p.id, p.title, p.content, p.postTime, p.updateTime, p.user.name, sum(c.count)) " +
            "FROM PostEntity p " +
            "LEFT JOIN p.counters c " +
            "GROUP BY p.id, p.user.name " +
            "ORDER BY p.postTime DESC")
    public List<PostResponseModel> findAllCountViewOrderByTime();
}
