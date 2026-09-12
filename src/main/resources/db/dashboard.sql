-- =====================================================
-- 博客管理系统 - 仪表盘相关表结构
-- 数据库：blogdb   字符集：utf8mb4
-- =====================================================

-- 访问记录表
CREATE TABLE IF NOT EXISTS `visit_record` (
  `id`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ip`         VARCHAR(64)  DEFAULT NULL COMMENT '访客IP',
  `province`   VARCHAR(64)  DEFAULT NULL COMMENT '访客省份',
  `article_id` BIGINT       DEFAULT NULL COMMENT '被访问文章ID，可为空',
  `visit_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
  PRIMARY KEY (`id`),
  KEY `idx_visit_time` (`visit_time`),
  KEY `idx_ip` (`ip`),
  KEY `idx_province` (`province`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访问记录表';

-- 文章表（若已存在则不会重建，仅补充说明）
CREATE TABLE IF NOT EXISTS `article` (
  `id`           INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title`        VARCHAR(255) NOT NULL COMMENT '标题',
  `content`      LONGTEXT     COMMENT '正文',
  `summary`      VARCHAR(500) DEFAULT NULL COMMENT '摘要',
  `cover_image`  VARCHAR(255) DEFAULT NULL COMMENT '封面图',
  `category_id`  INT          DEFAULT NULL COMMENT '分类ID',
  `user_id`      INT          DEFAULT NULL COMMENT '作者ID',
  `views`        INT          NOT NULL DEFAULT 0 COMMENT '阅读量',
  `status`       TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：0草稿 1发布',
  `created_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
  `create_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（兼容字段）',
  `update_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_views` (`views`),
  KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `article_id`  BIGINT       DEFAULT NULL COMMENT '文章ID',
  `nickname`    VARCHAR(64)  DEFAULT NULL COMMENT '昵称',
  `content`     TEXT         COMMENT '评论内容',
  `status`      TINYINT      NOT NULL DEFAULT 0 COMMENT '状态：0待审核 1已通过 2已拒绝',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 留言表
CREATE TABLE IF NOT EXISTS `message` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `nickname`    VARCHAR(64)  DEFAULT NULL COMMENT '昵称',
  `content`     TEXT         COMMENT '留言内容',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '留言时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='留言表';

-- 用户表（后台登录使用；与你现有 blogdb 保持一致即可）
CREATE TABLE IF NOT EXISTS `user` (
  `id`          INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username`    VARCHAR(64)  NOT NULL COMMENT '用户名',
  `password`    VARCHAR(128) NOT NULL COMMENT '密码',
  `nickname`    VARCHAR(64)  DEFAULT NULL COMMENT '昵称',
  `avatar`      VARCHAR(255) DEFAULT NULL COMMENT '头像',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 默认管理员账号（用户名 admin / 密码 admin123，请登录后自行修改）
INSERT INTO `user` (`username`, `password`, `nickname`)
SELECT 'admin', 'admin123', '管理员'
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE `username` = 'admin');

-- =====================================================
-- 可选：造一批测试数据，方便仪表盘图表有内容显示
-- =====================================================
DROP PROCEDURE IF EXISTS `mock_visit_data`;
DELIMITER $$
CREATE PROCEDURE `mock_visit_data`()
BEGIN
  DECLARE i INT DEFAULT 0;
  DECLARE dayOffset INT;
  DECLARE provinces VARCHAR(255) DEFAULT '广东,浙江,北京,上海,江苏,四川,湖北,山东,福建,湖南';
  WHILE i < 300 DO
    SET dayOffset = FLOOR(RAND() * 7);
    INSERT INTO `visit_record` (`ip`, `province`, `article_id`, `visit_time`)
    VALUES (
      CONCAT('192.168.1.', FLOOR(RAND() * 255)),
      SUBSTRING_INDEX(SUBSTRING_INDEX(provinces, ',', FLOOR(1 + RAND() * 10)), ',', -1),
      NULL,
      DATE_SUB(NOW(), INTERVAL dayOffset DAY)
    );
    SET i = i + 1;
  END WHILE;
END$$
DELIMITER ;

-- 执行一次生成测试数据后，可按需删除存储过程：
-- CALL mock_visit_data();
-- DROP PROCEDURE IF EXISTS mock_visit_data;
