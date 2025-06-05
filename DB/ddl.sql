-- 建立使用者資料表
create table user
(
    user_id      bigint auto_increment
        primary key,
    user_name    varchar(15)  not null,
    email        varchar(100) null,
    password     varchar(100) not null,
    biography    text         null,
    phone_number varchar(10)  not null,
    constraint user_pk_2
        unique (user_name),
    constraint user_pk_3
        unique (password),
    constraint user_pk_4
        unique (phone_number)
);

-- 建立貼文資料表
create table post
(
    post_id    bigint auto_increment
        primary key,
    user_id    bigint                              not null,
    content    varchar(5000)                       not null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    constraint post_user_user_id_fk
        foreign key (user_id) references user (user_id)
            on delete cascade
);


-- 建立留言資料表
create table comment
(
    comment_id bigint auto_increment
        primary key,
    user_id    bigint                              not null,
    post_id    bigint                              not null,
    content    varchar(255)                        not null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    constraint comment_post_post_id_fk
        foreign key (post_id) references post (post_id)
            on delete cascade,
    constraint comment_user_user_id_fk
        foreign key (user_id) references user (user_id)
            on delete cascade
);
