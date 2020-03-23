package com.hyl.batch.service;

import com.hyl.batch.dao.UserDao;
import com.hyl.batch.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private static UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        UserService.userDao = userDao;
    }


    public static User getUserById(long idUser) {
        Optional<User> optUser = userDao.findById(idUser);
        if (optUser.isPresent()) return optUser.get();
        else throw new RuntimeException("Utilisateur introuvable");
    }
}
