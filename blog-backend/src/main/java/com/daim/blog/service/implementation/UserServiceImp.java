package com.daim.blog.service.implementation;

import com.daim.blog.entity.UserEntity;
import com.daim.blog.model.UserRegisterModel;
import com.daim.blog.repository.UserRepository;
import com.daim.blog.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register new user
     * @param userRegisterModel
     * @throws EntityExistsException
     */
    public void register(UserRegisterModel userRegisterModel) throws EntityExistsException {
        UserEntity entity = userRepo.findByUsername(userRegisterModel.getUsername());
        if (entity != null){
            throw new EntityExistsException();
        }
        entity = new UserEntity(userRegisterModel);
        entity.setPassword(passwordEncoder.encode(userRegisterModel.getPassword()));
        userRepo.save(entity);
    }
}
