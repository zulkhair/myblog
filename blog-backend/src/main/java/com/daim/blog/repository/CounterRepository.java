package com.daim.blog.repository;

import com.daim.blog.entity.CounterEntity;
import com.daim.blog.entity.id.CounterId;
import com.daim.blog.model.PostResponseModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface CounterRepository extends CrudRepository<CounterEntity, CounterId> {

    @Query(value = "SELECT SUM(c.count) FROM counter c WHERE c.post_id = :postId", nativeQuery = true)
    Long countViewByPost(@Param("postId") String postId);

    Set<CounterEntity> findCounterEntityById_PostId(String postId);
}
