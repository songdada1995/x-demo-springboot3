CREATE
DATABASE IF NOT EXISTS `x_demo` DEFAULT CHARACTER SET utf8mb4;

USE
`x_demo`;

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `menu_id`     BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `menu_name`   VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `parent_id`   BIGINT(20) DEFAULT '0' COMMENT '父菜单ID',
    `order_num`   INT(11) DEFAULT '0' COMMENT '显示顺序',
    `path`        VARCHAR(200) DEFAULT '' COMMENT '路由地址',
    `component`   VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
    `query`       VARCHAR(255) DEFAULT NULL COMMENT '路由参数',
    `is_frame`    TINYINT(4) DEFAULT '1' COMMENT '是否为外链（0是 1否）',
    `is_cache`    TINYINT(4) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
    `menu_type`   TINYINT(4) DEFAULT NULL COMMENT '菜单类型（0-目录，1-菜单，2-按钮）',
    `visible`     CHAR(1)      DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
    `status`      TINYINT(4) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
    `perms`       VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    `icon`        VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
    `remark`      VARCHAR(500) DEFAULT '' COMMENT '备注',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    `create_by`   VARCHAR(50)  DEFAULT NULL COMMENT '创建者',
    `update_time` DATETIME     DEFAULT NULL COMMENT '更新时间',
    `update_by`   VARCHAR(50)  DEFAULT NULL COMMENT '更新者',
    `del_flag`    TINYINT(4) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    PRIMARY KEY (`menu_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `role_id`     BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name`   VARCHAR(50)  NOT NULL COMMENT '角色名称',
    `role_key`    VARCHAR(100) NOT NULL COMMENT '角色权限字符串',
    `role_sort`   INT(11) NOT NULL COMMENT '显示顺序',
    `status`      TINYINT(4) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    `create_by`   VARCHAR(50)  DEFAULT NULL COMMENT '创建者',
    `update_time` DATETIME     DEFAULT NULL COMMENT '更新时间',
    `update_by`   VARCHAR(50)  DEFAULT NULL COMMENT '更新者',
    `del_flag`    TINYINT(4) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    PRIMARY KEY (`role_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT(20) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `user_id`     BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`    VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100) NOT NULL COMMENT '密码',
    `nickname`    VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `status`      TINYINT(4) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
    `lock_flag`   TINYINT(4) DEFAULT '0' COMMENT '账号是否被锁（0否 1是）',
    `login_limit` TINYINT(4) DEFAULT '5' COMMENT '账号剩余登录重试次数',
    `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    `create_by`   VARCHAR(50)  DEFAULT NULL COMMENT '创建者',
    `update_time` DATETIME     DEFAULT NULL COMMENT '更新时间',
    `update_by`   VARCHAR(50)  DEFAULT NULL COMMENT '更新者',
    `del_flag`    TINYINT(4) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `idx_username` (`username`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization`
(
    `id`                            VARCHAR(100) NOT NULL,
    `registered_client_id`          VARCHAR(100) NOT NULL,
    `principal_name`                VARCHAR(200) NOT NULL,
    `authorization_grant_type`      VARCHAR(100) NOT NULL,
    `attributes`                    VARCHAR(4000) DEFAULT NULL,
    `state`                         VARCHAR(500)  DEFAULT NULL,
    `authorization_code_value`      TEXT,
    `authorization_code_issued_at`  DATETIME      DEFAULT NULL,
    `authorization_code_expires_at` DATETIME      DEFAULT NULL,
    `authorization_code_metadata`   VARCHAR(2000) DEFAULT NULL,
    `access_token_value`            TEXT,
    `access_token_issued_at`        DATETIME      DEFAULT NULL,
    `access_token_expires_at`       DATETIME      DEFAULT NULL,
    `access_token_metadata`         VARCHAR(2000) DEFAULT NULL,
    `access_token_type`             VARCHAR(100)  DEFAULT NULL,
    `access_token_scopes`           VARCHAR(1000) DEFAULT NULL,
    `refresh_token_value`           TEXT,
    `refresh_token_issued_at`       DATETIME      DEFAULT NULL,
    `refresh_token_expires_at`      DATETIME      DEFAULT NULL,
    `refresh_token_metadata`        VARCHAR(2000) DEFAULT NULL,
    `oidc_id_token_value`           TEXT,
    `oidc_id_token_issued_at`       DATETIME      DEFAULT NULL,
    `oidc_id_token_expires_at`      DATETIME      DEFAULT NULL,
    `oidc_id_token_metadata`        VARCHAR(2000) DEFAULT NULL,
    `oidc_id_token_claims`          VARCHAR(2000) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4   COMMENT='OAuth2 授权表';

DROP TABLE IF EXISTS `oauth2_authorization_consent`;
CREATE TABLE `oauth2_authorization_consent`
(
    `registered_client_id` VARCHAR(100)  NOT NULL,
    `principal_name`       VARCHAR(200)  NOT NULL,
    `authorities`          VARCHAR(1000) NOT NULL,
    PRIMARY KEY (`registered_client_id`, `principal_name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4   COMMENT='OAuth2 授权同意表';

DROP TABLE IF EXISTS `oauth2_registered_client`;
CREATE TABLE `oauth2_registered_client`
(
    `id`                            VARCHAR(100)  NOT NULL,
    `client_id`                     VARCHAR(100)  NOT NULL,
    `client_id_issued_at`           DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `client_secret`                 VARCHAR(200)           DEFAULT NULL,
    `client_secret_expires_at`      DATETIME               DEFAULT NULL,
    `client_name`                   VARCHAR(200)  NOT NULL,
    `client_authentication_methods` VARCHAR(1000) NOT NULL,
    `authorization_grant_types`     VARCHAR(1000) NOT NULL,
    `redirect_uris`                 VARCHAR(1000)          DEFAULT NULL,
    `scopes`                        VARCHAR(1000) NOT NULL,
    `client_settings`               VARCHAR(2000) NOT NULL,
    `token_settings`                VARCHAR(2000) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4  COMMENT='OAuth2 客户端表';
