-- 插入測試使用者
INSERT INTO user (user_name, email, password, phone_number)
VALUES
    ('Alice', 'alice@example.com', SHA2(CONCAT('password123', 'salt123'), 256), '0911111111'),
    ('Bob', 'bob@example.com', SHA2(CONCAT('password456', 'salt123'), 256), '0922222222');

-- 插入測試發文
INSERT INTO post (user_id, content)
VALUES
    (1, '這是 Alice 的第一篇文章'),
    (2, 'Bob 的照片貼文');

-- 插入測試留言
INSERT INTO comment (user_id, post_id, content)
VALUES
    (2, 1, 'Bob 留言給 Alice'),
    (1, 2, 'Alice 回應 Bob 的照片');
