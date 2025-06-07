package com.nanco.social.repository.impl;

import com.nanco.social.model.Post;
import com.nanco.social.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// 要求：透過 Stored Procedure 存取資料庫
@Repository
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 要求：透過 Stored Procedure 存取資料庫 - 創建發文
    @Override
    public boolean createPost(Post post) {
        try {
            jdbcTemplate.update(
                    "CALL sp_create_post(?, ?)",
                    post.getUserId(),
                    post.getContent()
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 要求：透過 Stored Procedure 存取資料庫 - 獲取所有發文
    @Override
    public List<Post> getAllPosts() {
        try {
            return jdbcTemplate.query(
                    "CALL sp_get_all_posts()",
                    new PostRowMapper()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // 要求：透過 Stored Procedure 存取資料庫 - 編輯發文
    @Override
    public boolean editPost(Post post) {
        try {
            jdbcTemplate.update(
                    "CALL sp_edit_post(?, ?)",
                    post.getPostId(),
                    post.getContent()
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 要求：透過 Stored Procedure 存取資料庫 - 刪除發文
    @Override
    public boolean deletePost(Long postId) {
        try {
            jdbcTemplate.update(
                    "CALL sp_delete_post(?)",
                    postId
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class PostRowMapper implements RowMapper<Post> {
        @Override
        public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            Post post = new Post();
            post.setPostId(rs.getLong("post_id"));
            post.setUserId(rs.getLong("user_id"));
            post.setContent(rs.getString("content"));
            post.setCreatedAt(rs.getTimestamp("created_at"));
            post.setUserName(rs.getString("user_name"));
            return post;
        }
    }
}
