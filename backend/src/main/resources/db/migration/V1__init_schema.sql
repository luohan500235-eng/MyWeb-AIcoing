CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    email VARCHAR(100),
    status TINYINT NOT NULL DEFAULT 1,
    last_login_time DATETIME NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS blog_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    slug VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    sort INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS blog_tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    slug VARCHAR(100) NOT NULL UNIQUE,
    color VARCHAR(20),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS blog_post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(200) NOT NULL UNIQUE,
    summary VARCHAR(500),
    cover_image VARCHAR(255),
    content_md LONGTEXT NOT NULL,
    content_html LONGTEXT NULL,
    category_id BIGINT NULL,
    status TINYINT NOT NULL DEFAULT 0,
    is_top TINYINT NOT NULL DEFAULT 0,
    allow_comment TINYINT NOT NULL DEFAULT 1,
    view_count INT NOT NULL DEFAULT 0,
    like_count INT NOT NULL DEFAULT 0,
    seo_title VARCHAR(255),
    seo_description VARCHAR(500),
    published_at DATETIME NULL,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    KEY idx_blog_post_category_id (category_id),
    KEY idx_blog_post_status_published_at (status, published_at),
    KEY idx_blog_post_is_top_published_at (is_top, published_at)
);

CREATE TABLE IF NOT EXISTS blog_post_tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    UNIQUE KEY uk_blog_post_tag_post_tag (post_id, tag_id),
    KEY idx_blog_post_tag_tag_id (tag_id)
);

CREATE TABLE IF NOT EXISTS blog_comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    parent_id BIGINT NOT NULL DEFAULT 0,
    reply_to_id BIGINT NULL,
    nickname VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    website VARCHAR(255),
    content VARCHAR(1000) NOT NULL,
    ip VARCHAR(64),
    user_agent VARCHAR(500),
    status TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    KEY idx_blog_comment_post_created_at (post_id, created_at),
    KEY idx_blog_comment_parent_id (parent_id),
    KEY idx_blog_comment_status (status)
);

CREATE TABLE IF NOT EXISTS sys_file (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    origin_name VARCHAR(255) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_url VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    content_type VARCHAR(100),
    storage_type VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    KEY idx_sys_file_created_at (created_at)
);

CREATE TABLE IF NOT EXISTS blog_site_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    site_name VARCHAR(100),
    site_subtitle VARCHAR(255),
    site_description VARCHAR(500),
    site_keywords VARCHAR(255),
    logo VARCHAR(255),
    favicon VARCHAR(255),
    author_name VARCHAR(50),
    author_intro VARCHAR(500),
    github_url VARCHAR(255),
    email VARCHAR(100),
    about_md LONGTEXT,
    icp VARCHAR(100),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);