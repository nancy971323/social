package com.nanco.social.service;

import com.nanco.social.model.Comment;

import java.util.List;

public interface CommentService {

    /**
     * 創建留言
     * 
     * @param comment 留言信息
     * @param userId 使用者ID
     * @return 是否創建成功
     */
    boolean createComment(Comment comment, Long userId);

    /**
     * 獲取特定發文的所有留言
     * 
     * @param postId 發文ID
     * @return 留言列表
     */
    List<Comment> getPostComments(Long postId);
}
