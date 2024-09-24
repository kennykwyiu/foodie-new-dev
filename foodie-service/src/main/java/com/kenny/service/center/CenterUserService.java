package com.kenny.service.center;

import com.kenny.bo.CenterUserBO;
import com.kenny.pojo.Users;

public interface CenterUserService {
    public Users queryUserInfo(String userId);

    public Users updateUserInfo(String userId, CenterUserBO centerUserBO);
    public Users updateUserFace(String userId, String faceUrl);
}
