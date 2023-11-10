package com.daim.blog.repository;

import com.daim.blog.entity.CommentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface CommentRepository extends CrudRepository<CommentEntity, String> {
    Set<CommentEntity> findCommentEntitiesByPostId(String postId);
    @Transactional
    List<CommentEntity> findCommentEntitiesByPostIdOrderByPostTimeDesc(String postId);

    @Query(value = "SELECT c FROM CommentEntity c JOIN c.post p WHERE c.id = :commentId AND p.userId = :userId")
    CommentEntity findByCommentIdAndUserId(@Param("commentId") String commentId, @Param("userId") String userId);
}
