package com.nanco.social.repository;

import com.nanco.social.model.Comment;

import java.util.List;

public interface CommentRepository {

    /**
     * 創建新留言
     * 
     * @param comment 留言信息
     * @return 是否創建成功
     */
    boolean createComment(Comment comment);

    /**
     * 獲取特定發文的所有留言
     * 
     * @param postId 發文ID
     * @return 留言列表
     */
    List<Comment> getPostComments(Long postId);
}
