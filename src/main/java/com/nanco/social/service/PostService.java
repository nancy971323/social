package com.nanco.social.service;

import com.nanco.social.model.Post;

import java.util.List;

public interface PostService {

    /**
     * 創建發文
     * 
     * @param post 發文信息
     * @param userId 使用者ID
     * @return 是否創建成功
     */
    boolean createPost(Post post, Long userId);

    /**
     * 獲取所有發文
     * 
     * @return 發文列表
     */
    List<Post> getAllPosts();

    /**
     * 編輯發文
     * 
     * @param post 發文信息
     * @param userId 使用者ID
     * @return 是否編輯成功
     */
    boolean editPost(Post post, Long userId);

    /**
     * 刪除發文
     * 
     * @param postId 發文ID
     * @param userId 使用者ID
     * @return 是否刪除成功
     */
    boolean deletePost(Long postId, Long userId);
}
