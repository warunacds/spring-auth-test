package com.waruna.authtest.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO {

    //User getUserById(int id);
    User getUserByUserName(String username);

    User getUserByFacebookID(String fbId);

    boolean createNewUser(String fbId, String authToken, String username);


}
