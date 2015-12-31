package com.wa.edu.service;

import com.wa.edu.domain.User;

import java.util.List;

/**
 * Created by dell on 15-11-6.
 **/
public interface UserService {

    public int insertUser(User user);

    public  List<User> selectAllUsers();



}
