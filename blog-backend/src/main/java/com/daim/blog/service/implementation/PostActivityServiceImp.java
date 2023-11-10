package com.daim.blog.service.implementation;

import com.daim.blog.entity.CommentEntity;
import com.daim.blog.entity.CounterEntity;
import com.daim.blog.entity.PostEntity;
import com.daim.blog.entity.UserEntity;
import com.daim.blog.entity.id.CounterId;
import com.daim.blog.model.*;
import com.daim.blog.repository.CommentRepository;
import com.daim.blog.repository.CounterRepository;
import com.daim.blog.repository.PostRepository;
import com.daim.blog.repository.UserRepository;
import com.daim.blog.service.PostActivityService;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PostActivityServiceImp implements PostActivityService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CounterRepository counterRepository;
    private final CommentRepository commentRepository;

    public PostActivityServiceImp(PostRepository postRepository, UserRepository userRepository, CounterRepository counterRepository,
                                  CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.counterRepository = counterRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * Get all post with view counter and ordered by post time
     * @return
     */
    @Override
    public List<PostResponseModel> viewPost() {
        return postRepository.findAllCountViewOrderByTime();
    }

    /**
     * This method is to search post when user has been login (admin)
     * @param username
     * @return
     */
    @Override
    public List<PostResponseModel> viewPost(String username) {
        String userId = userRepository.findUserIdByUsername(username);
        List<PostEntity> postEntities = postRepository.findByUserIdEqualsOrderByPostTimeDesc(userId);

        // Todo : qreate query with PostReponse model, so in the future we dont have to iterate it again to create PostResponse model
        List<PostResponseModel> postResponseModels = new ArrayList<>();
        postEntities.forEach(postEntity -> {
            postResponseModels.add(new PostResponseModel(postEntity));
        });
        return postResponseModels;
    }

    /**
     * This method is to search post by urlPath user. User can have unique url path and it can be used to filter only our post
     * @param urlPath
     * @return
     */
    @Override
    public List<PostResponseModel> viewPostByUrlPath(String urlPath){
        List<PostEntity> postEntities = postRepository.findByUrlPath(urlPath);
        // Todo : qreate query with PostReponse model, so in the future we dont have to iterate it again to create PostResponse model
        List<PostResponseModel> postResponseModels = new ArrayList<>();
        postEntities.forEach(postEntity -> {
            postResponseModels.add(new PostResponseModel(postEntity));
        });
        return postResponseModels;
    }

    /**
     * Method for add new Post
     * @param createPostModel
     * @param username
     * @throws NoSuchElementException
     */
    @Override
    @Transactional
    public void createPost(CreatePostModel createPostModel, String username) throws NoSuchElementException {
        // Todo : put userId in jwt, so we didn't have to search to database
        String userId = userRepository.findUserIdByUsername(username);
        UserEntity user = userRepository.findById(userId).orElseThrow();
        PostEntity entity = new PostEntity(createPostModel, user);
        postRepository.save(entity);
    }

    /**
     * Method for edit a post
     * @param editPostModel
     * @param username
     * @throws NoSuchElementException
     */
    @Override
    @Transactional
    public void editPost(EditPostModel editPostModel, String username) throws NoSuchElementException {
        // Todo : put userId in jwt, so we didn't have to search to database
        String userId = userRepository.findUserIdByUsername(username);
        PostEntity entity = postRepository.findByIdAndUserId(editPostModel.getPostId(), userId);
        if (entity != null) {
            entity.setContent(editPostModel.getContent().getBytes());
            entity.setTitle(editPostModel.getTitle());
            entity.setUpdateTime(new Date());
            postRepository.save(entity);
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Method for delete post
     * @param postId
     * @param username
     * @throws NoSuchElementException
     */
    @Override
    @Transactional
    public void deletePost(String postId, String username) throws NoSuchElementException {
        // Todo : put userId in jwt, so we didn't have to search to database
        String userId = userRepository.findUserIdByUsername(username);

        // find by post id and user id to prevent other user delete the comment
        PostEntity entity = postRepository.findByIdAndUserId(postId, userId);
        Set<CommentEntity> commentEntities = commentRepository.findCommentEntitiesByPostId(postId);
        Set<CounterEntity> counterEntities = counterRepository.findCounterEntityById_PostId(postId);
        if (entity != null) {
            // Delete all dependency first
            counterRepository.deleteAll(counterEntities);
            commentRepository.deleteAll(commentEntities);
            postRepository.delete(entity);

            // Todo : create query for deleting all data with 1 query
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Method for viewing detail of post
     * @param postId
     * @param ipAddress
     * @param username
     * @return
     * @throws NoSuchElementException
     */
    @Override
    public PostResponseModel viewDetailPost(String postId, String ipAddress, String username) throws NoSuchElementException {
        PostEntity postEntity = postRepository.findById(postId).get();

        // Prepare response model
        PostResponseModel postResponseModel = new PostResponseModel(postEntity);
        postResponseModel.setComments(new ArrayList<>());

        // Check login user to give the owner of post ability to delete comment
        if (username != null && !username.isEmpty()){
            UserEntity userEntity = userRepository.findByUsername(username);
            if (userEntity != null && userEntity.getId().equals(postEntity.getUser().getId())){
                postResponseModel.setDeleteComment(true);
            }else{
                postResponseModel.setDeleteComment(false);
            }
        }

        // Put the comment to the model
        List<CommentEntity> commentEntities = commentRepository.findCommentEntitiesByPostIdOrderByPostTimeDesc(postId);
        commentEntities.forEach(commentEntity -> {
            postResponseModel.getComments().add(new CommentResponseModel(commentEntity));
        });

        // Sum view counter in database
        Long count = counterRepository.countViewByPost(postId);
        if (count == null){
            count = 0L;
        }

        // Count the view
        // Todo : create queue or lock mechanism to prevent race condition
        CounterId counterId = new CounterId(postId, ipAddress);
        CounterEntity counterEntity = counterRepository.findById(counterId).orElse(null);
        if (counterEntity == null) {
            // Viewer is from new ip address
            counterEntity = new CounterEntity(counterId, 1);
            counterEntity.setPost(postEntity);
            count++;
        } else {
            // Viewer with same ip address
            DateTime now = DateTime.now();
            DateTime lastUpdatedTime = new DateTime(counterEntity.getLastUpdatedTime());
            long minuteDifferences = new Duration(lastUpdatedTime, now).getStandardMinutes();

            // If the viewer visit the same page before 5 minute the counter will not increase
            if (minuteDifferences > 5) {
                counterEntity.setCount(counterEntity.getCount() + 1);
                counterEntity.setLastUpdatedTime(now.toDate());
                count++;
            }
        }

        // save new count
        counterRepository.save(counterEntity);

        postResponseModel.setViewCount(count);
        return postResponseModel;
    }

    /**
     * Method for creating a comment
     * @param createCommentModel
     * @throws NoSuchElementException
     */
    @Override
    @Transactional
    public void createComment(CreateCommentModel createCommentModel) throws NoSuchElementException {
        PostEntity postEntity = postRepository.findById(createCommentModel.getPostId()).get();
        CommentEntity commentEntity = new CommentEntity(createCommentModel.getComment(), createCommentModel.getName(), postEntity);
        commentRepository.save(commentEntity);
    }

    /**
     * Method for delete comment
     * @param commentId
     * @param username
     * @throws NoSuchElementException
     */
    @Override
    @Transactional
    public void deleteComment(String commentId, String username) throws NoSuchElementException{
        String userId = userRepository.findUserIdByUsername(username);

        // find comment by comment id and user id to prevent other user delete the comment
        CommentEntity commentEntity = commentRepository.findByCommentIdAndUserId(commentId, userId);
        if (commentEntity != null){
            commentRepository.delete(commentEntity);
        }else{
            throw new NoSuchElementException();
        }
    }
}
