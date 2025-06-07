package com.nanco.social.repository;

import com.nanco.social.model.Post;

import java.util.List;

public interface PostRepository {

    /**
     * 創建新發文
     * 
     * @param post 發文信息
     * @return 是否創建成功
     */
    boolean createPost(Post post);

    /**
     * 獲取所有發文列表
     * 
     * @return 發文列表
     */
    List<Post> getAllPosts();

    /**
     * 編輯發文
     * 
     * @param post 發文信息
     * @return 是否編輯成功
     */
    boolean editPost(Post post);

    /**
     * 刪除發文
     * 
     * @param postId 發文ID
     * @return 是否刪除成功
     */
    boolean deletePost(Long postId);
}
