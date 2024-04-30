package com.project.Fabo.dao;

import com.project.Fabo.entity.User;

public interface UserDao {

    User findByUserName(String userName);
    
}
