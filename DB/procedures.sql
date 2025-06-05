DELIMITER $$

-- 建立使用者
CREATE PROCEDURE sp_create_user (
    IN p_user_name VARCHAR(15),
    IN p_email VARCHAR(100),
    IN p_password VARCHAR(100),
    IN p_phone_number VARCHAR(10)
)
BEGIN
INSERT INTO user (user_name, email, password, phone_number)
VALUES (p_user_name, p_email, p_password, p_phone_number);
END$$

-- 建立貼文
CREATE PROCEDURE sp_create_post (
    IN p_user_id BIGINT,
    IN p_content VARCHAR(5000)
)
BEGIN
INSERT INTO post (user_id, content)
VALUES (p_user_id, p_content);
END$$

-- 建立留言
CREATE PROCEDURE sp_create_comment (
    IN p_user_id BIGINT,
    IN p_post_id BIGINT,
    IN p_content VARCHAR(255)
)
BEGIN
START TRANSACTION;

INSERT INTO comment (user_id, post_id, content)
VALUES (p_user_id, p_post_id, p_content);

COMMIT;
END$$

-- 用戶登入驗證
CREATE PROCEDURE sp_user_login (
    IN p_phone_number VARCHAR(20),
    IN p_password VARCHAR(255)
)
BEGIN
    SELECT user_id, user_name, email 
    FROM user
    WHERE phone_number = p_phone_number AND password = p_password;
END$$

-- 獲取所有發文
CREATE PROCEDURE sp_get_all_posts ()
BEGIN
    SELECT p.*, u.user_name
    FROM post p
    JOIN user u ON p.user_id = u.user_id
    ORDER BY p.created_at DESC;
END$$

-- 獲取特定發文的留言
CREATE PROCEDURE sp_get_post_comments (
    IN p_post_id BIGINT
)
BEGIN
    SELECT c.*, u.user_name
    FROM comment c
    JOIN user u ON c.user_id = u.user_id
    WHERE c.post_id = p_post_id
    ORDER BY c.created_at ASC;
END$$

-- 編輯發文
CREATE PROCEDURE sp_edit_post (
    IN p_post_id BIGINT,
    IN p_content VARCHAR(5000)
)
BEGIN
    UPDATE post
    SET content = p_content
    WHERE post_id = p_post_id;
END$$

-- 刪除發文
CREATE PROCEDURE sp_delete_post (
    IN p_post_id BIGINT
)
BEGIN
    DELETE FROM `post`
    WHERE post_id = p_post_id;
END$$

-- 更新使用者資料
CREATE PROCEDURE sp_update_user (
    IN p_user_id BIGINT,
    IN p_user_name VARCHAR(15),
    IN p_email VARCHAR(100),
    IN p_biography TEXT
)
BEGIN
    UPDATE user
    SET user_name = p_user_name,
        email = p_email,
        biography = p_biography
    WHERE user_id = p_user_id;
END$$

DELIMITER ;