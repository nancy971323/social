package com.nanco.social.repository;

import com.nanco.social.model.User;

public interface UserRepository {

    /**
     * 創建新使用者
     * 
     * @param user 使用者信息
     * @return 是否創建成功
     */
    boolean createUser(User user);

    /**
     * 使用手機號碼和密碼進行登入驗證
     * 
     * @param phoneNumber 手機號碼
     * @param password 密碼
     * @return 登入成功返回使用者信息，失敗返回null
     */
    User login(String phoneNumber, String password);

    /**
     * 更新使用者信息
     * 
     * @param user 使用者信息
     * @return 是否更新成功
     */
    boolean updateUser(User user);
}
