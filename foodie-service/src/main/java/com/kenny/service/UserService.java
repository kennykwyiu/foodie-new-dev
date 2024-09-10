package com.kenny.service;

import com.kenny.bo.UserBO;
import com.kenny.pojo.Stu;
import com.kenny.pojo.Users;

public interface UserService {
    public boolean queryUsernameIsExist(String username);

    public Users createUser(UserBO userBO);

}
