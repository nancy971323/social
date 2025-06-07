package com.nanco.social.repository.impl;

import com.nanco.social.model.Comment;
import com.nanco.social.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// 要求：透過 Stored Procedure 存取資料庫
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 要求：透過 Stored Procedure 存取資料庫 - 創建留言
    @Override
    public boolean createComment(Comment comment) {
        try {
            jdbcTemplate.update(
                    "CALL sp_create_comment(?, ?, ?)",
                    comment.getUserId(),
                    comment.getPostId(),
                    comment.getContent()
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 要求：透過 Stored Procedure 存取資料庫 - 獲取發文留言
    @Override
    public List<Comment> getPostComments(Long postId) {
        try {
            return jdbcTemplate.query(
                    "CALL sp_get_post_comments(?)",
                    new Object[]{postId},
                    new CommentRowMapper()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private static class CommentRowMapper implements RowMapper<Comment> {
        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Comment comment = new Comment();
            comment.setCommentId(rs.getLong("comment_id"));
            comment.setUserId(rs.getLong("user_id"));
            comment.setPostId(rs.getLong("post_id"));
            comment.setContent(rs.getString("content"));
            comment.setCreatedAt(rs.getTimestamp("created_at"));
            comment.setUserName(rs.getString("user_name"));
            return comment;
        }
    }
}
