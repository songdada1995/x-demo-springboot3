-- =====================================================
-- x-demo-springboot3 全量建表脚本
-- 使用方式: 在 MySQL 中直接执行本文件即可创建所有表
-- 注意: 会先 DROP 再 CREATE，请勿在生产环境直接执行
-- =====================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 系统用户表
-- 对应实体: SysUser
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id`     BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username`    VARCHAR(50)  NOT NULL                COMMENT '用户名',
  `password`    VARCHAR(100) NOT NULL                COMMENT '密码',
  `nickname`    VARCHAR(50)  DEFAULT NULL            COMMENT '昵称',
  `email`       VARCHAR(100) DEFAULT NULL            COMMENT '邮箱',
  `phone`       VARCHAR(20)  DEFAULT NULL            COMMENT '手机号',
  `status`      TINYINT      DEFAULT 0               COMMENT '状态（0正常 1停用）',
  `lock_flag`   TINYINT      DEFAULT 0               COMMENT '是否锁定（0否 1是）',
  `login_limit` TINYINT      DEFAULT 5               COMMENT '剩余登录重试次数',
  `remark`      VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  `create_by`   VARCHAR(50)  DEFAULT NULL            COMMENT '创建者',
  `create_time` DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`   VARCHAR(50)  DEFAULT NULL            COMMENT '更新者',
  `update_time` DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `del_flag`    TINYINT      DEFAULT 0               COMMENT '删除标志（0存在 1删除）',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_username` (`username`, `del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ----------------------------
-- 2. 系统角色表
-- 对应实体: SysRole (@TableLogic)
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name`            VARCHAR(50)  NOT NULL                COMMENT '角色名称',
  `role_key`             VARCHAR(100) DEFAULT NULL            COMMENT '角色编码',
  `role_sort`            INT          DEFAULT 0               COMMENT '显示顺序',
  `data_scope`           CHAR(1)      DEFAULT '1'             COMMENT '数据范围（1全部 2自定义 3本部门 4本部门及以下 5仅本人）',
  `menu_check_strictly`  TINYINT      DEFAULT 1               COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly`  TINYINT      DEFAULT 1               COMMENT '部门树选择项是否关联显示',
  `status`               TINYINT      DEFAULT 0               COMMENT '状态（0正常 1停用）',
  `remark`               VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  `create_by`            VARCHAR(50)  DEFAULT NULL            COMMENT '创建者',
  `create_time`          DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`            VARCHAR(50)  DEFAULT NULL            COMMENT '更新者',
  `update_time`          DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `del_flag`             TINYINT      DEFAULT 0               COMMENT '删除标志（0存在 1删除）',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- 3. 菜单权限表
-- 对应实体: SysMenu (@TableLogic)
-- menu_type/is_frame/is_cache/visible/status 均为 char(1)
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id`     BIGINT       NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name`   VARCHAR(50)  NOT NULL                COMMENT '菜单名称',
  `parent_id`   BIGINT       DEFAULT 0               COMMENT '父菜单ID',
  `order_num`   INT          DEFAULT 0               COMMENT '显示顺序',
  `path`        VARCHAR(200) DEFAULT ''              COMMENT '路由地址',
  `component`   VARCHAR(255) DEFAULT NULL            COMMENT '组件路径',
  `query`       VARCHAR(255) DEFAULT NULL            COMMENT '路由参数',
  `is_frame`    CHAR(1)      DEFAULT '1'             COMMENT '是否外链（0是 1否）',
  `is_cache`    CHAR(1)      DEFAULT '0'             COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type`   CHAR(1)      DEFAULT 'C'             COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible`     CHAR(1)      DEFAULT '0'             COMMENT '显示状态（0显示 1隐藏）',
  `status`      CHAR(1)      DEFAULT '0'             COMMENT '菜单状态（0正常 1停用）',
  `perms`       VARCHAR(100) DEFAULT NULL            COMMENT '权限标识',
  `icon`        VARCHAR(100) DEFAULT '#'             COMMENT '菜单图标',
  `remark`      VARCHAR(500) DEFAULT ''              COMMENT '备注',
  `create_by`   VARCHAR(50)  DEFAULT NULL            COMMENT '创建者',
  `create_time` DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`   VARCHAR(50)  DEFAULT NULL            COMMENT '更新者',
  `update_time` DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `del_flag`    TINYINT      DEFAULT 0               COMMENT '删除标志（0存在 1删除）',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- ----------------------------
-- 4. 用户和角色关联表（中间表，无逻辑删除）
-- 对应实体: SysUserRole
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

-- ----------------------------
-- 5. 角色和菜单关联表（中间表，无逻辑删除）
-- 对应实体: SysRoleMenu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

-- ----------------------------
-- 6. 通知公告表
-- 对应实体: SysNotice (@TableLogic)
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `notice_id`      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title`   VARCHAR(100) NOT NULL DEFAULT ''     COMMENT '公告标题',
  `notice_content` TEXT                                 COMMENT '公告内容',
  `notice_type`    CHAR(1)      NOT NULL DEFAULT '1'    COMMENT '公告类型（1通知 2公告）',
  `status`         INT          DEFAULT 0               COMMENT '状态（0正常 1关闭）',
  `create_by`      VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  `create_time`    DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`      VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  `update_time`    DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `del_flag`       INT          DEFAULT 0               COMMENT '删除标志（0存在 1删除）',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知公告表';

-- ----------------------------
-- 7. 登录日志表（无逻辑删除）
-- 对应实体: SysLoginLog
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `info_id`        BIGINT       NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `username`       VARCHAR(50)  DEFAULT ''              COMMENT '用户账号',
  `ipaddr`         VARCHAR(128) DEFAULT ''              COMMENT '登录IP地址',
  `login_location` VARCHAR(255) DEFAULT ''              COMMENT '登录地点',
  `browser`        VARCHAR(50)  DEFAULT ''              COMMENT '浏览器类型',
  `os`             VARCHAR(50)  DEFAULT ''              COMMENT '操作系统',
  `status`         CHAR(1)      DEFAULT '0'             COMMENT '登录状态（0成功 1失败）',
  `msg`            VARCHAR(255) DEFAULT ''              COMMENT '提示消息',
  `login_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
  PRIMARY KEY (`info_id`),
  KEY `idx_login_username` (`username`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统登录日志';

-- ----------------------------
-- 8. 操作日志表（无逻辑删除）
-- 对应实体: SysOperLog
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `oper_id`        BIGINT        NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title`          VARCHAR(50)   DEFAULT ''              COMMENT '模块标题',
  `business_type`  INT           DEFAULT 0               COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method`         VARCHAR(255)  DEFAULT ''              COMMENT '方法名称',
  `request_method` VARCHAR(10)   DEFAULT ''              COMMENT '请求方式',
  `operator_type`  INT           DEFAULT 0               COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name`      VARCHAR(50)   DEFAULT ''              COMMENT '操作人员',
  `oper_url`       VARCHAR(255)  DEFAULT ''              COMMENT '请求URL',
  `oper_ip`        VARCHAR(128)  DEFAULT ''              COMMENT '主机地址',
  `oper_location`  VARCHAR(255)  DEFAULT ''              COMMENT '操作地点',
  `oper_param`     VARCHAR(2000) DEFAULT ''              COMMENT '请求参数',
  `json_result`    VARCHAR(2000) DEFAULT ''              COMMENT '返回参数',
  `status`         INT           DEFAULT 0               COMMENT '操作状态（0正常 1异常）',
  `error_msg`      VARCHAR(2000) DEFAULT ''              COMMENT '错误消息',
  `oper_time`      DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`oper_id`),
  KEY `idx_oper_time` (`oper_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志记录';

-- ----------------------------
-- 9. 数据字典表
-- 对应实体: SysDict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name`        VARCHAR(50)  NOT NULL                COMMENT '字典名称',
  `code`        VARCHAR(50)  NOT NULL                COMMENT '字典编码',
  `description` VARCHAR(200) DEFAULT NULL            COMMENT '字典描述',
  `status`      TINYINT      DEFAULT 1               COMMENT '状态（0禁用 1正常）',
  `create_by`   VARCHAR(50)  DEFAULT NULL            COMMENT '创建人',
  `create_time` DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`   VARCHAR(50)  DEFAULT NULL            COMMENT '更新人',
  `update_time` DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `deleted`     TINYINT(1)   DEFAULT 0               COMMENT '是否删除（0否 1是）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典表';

-- ----------------------------
-- 10. 数据字典项表
-- 对应实体: SysDictItem
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_id`     BIGINT      NOT NULL                COMMENT '字典ID',
  `label`       VARCHAR(50) NOT NULL                COMMENT '字典项标签',
  `value`       VARCHAR(50) NOT NULL                COMMENT '字典项值',
  `sort`        INT         DEFAULT 0               COMMENT '排序',
  `status`      TINYINT     DEFAULT 1               COMMENT '状态（0禁用 1正常）',
  `create_by`   VARCHAR(50) DEFAULT NULL            COMMENT '创建人',
  `create_time` DATETIME    DEFAULT NULL            COMMENT '创建时间',
  `update_by`   VARCHAR(50) DEFAULT NULL            COMMENT '更新人',
  `update_time` DATETIME    DEFAULT NULL            COMMENT '更新时间',
  `deleted`     TINYINT(1)  DEFAULT 0               COMMENT '是否删除（0否 1是）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_value` (`dict_id`, `value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典项表';

-- ----------------------------
-- 11. 客户端表
-- 对应实体: SysClient
-- ----------------------------
DROP TABLE IF EXISTS `sys_client`;
CREATE TABLE `sys_client` (
  `id`                     BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `client_id`              VARCHAR(50)  NOT NULL                COMMENT '客户端ID',
  `client_secret`          VARCHAR(100) NOT NULL                COMMENT '客户端密钥',
  `client_name`            VARCHAR(50)  NOT NULL                COMMENT '客户端名称',
  `redirect_uri`           VARCHAR(200) DEFAULT NULL            COMMENT '重定向URI',
  `grant_types`            VARCHAR(100) DEFAULT NULL            COMMENT '授权类型',
  `scopes`                 VARCHAR(100) DEFAULT NULL            COMMENT '授权范围',
  `access_token_validity`  INT          DEFAULT 3600            COMMENT '访问令牌有效期（秒）',
  `refresh_token_validity` INT          DEFAULT 86400           COMMENT '刷新令牌有效期（秒）',
  `status`                 TINYINT      DEFAULT 1               COMMENT '状态（0禁用 1正常）',
  `create_by`              VARCHAR(50)  DEFAULT NULL            COMMENT '创建人',
  `create_time`            DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`              VARCHAR(50)  DEFAULT NULL            COMMENT '更新人',
  `update_time`            DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `deleted`                TINYINT(1)   DEFAULT 0               COMMENT '是否删除（0否 1是）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_client_id` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户端表';

-- ----------------------------
-- 12. 业务账号表
-- 对应实体: BizAccount (@TableLogic)
-- ----------------------------
DROP TABLE IF EXISTS `biz_account`;
CREATE TABLE `biz_account` (
  `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '账号ID',
  `account_name`    VARCHAR(100) NOT NULL                COMMENT '账号名称',
  `account_type`    CHAR(1)      NOT NULL DEFAULT '1'    COMMENT '账号类型（1个人 2企业 3系统）',
  `contact_person`  VARCHAR(50)  DEFAULT ''              COMMENT '联系人',
  `contact_phone`   VARCHAR(20)  DEFAULT ''              COMMENT '联系电话',
  `email`           VARCHAR(100) DEFAULT ''              COMMENT '电子邮箱',
  `status`          INT          DEFAULT 1               COMMENT '状态（0禁用 1启用）',
  `description`     VARCHAR(500) DEFAULT ''              COMMENT '备注',
  `create_by`       VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  `create_time`     DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`       VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  `update_time`     DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `del_flag`        INT          DEFAULT 0               COMMENT '删除标志（0存在 1删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务账号表';

-- ----------------------------
-- 13. 仓库管理表
-- 对应实体: BizWarehouse (@TableLogic)
-- ----------------------------
DROP TABLE IF EXISTS `biz_warehouse`;
CREATE TABLE `biz_warehouse` (
  `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '仓库ID',
  `warehouse_name`  VARCHAR(100) NOT NULL                COMMENT '仓库名称',
  `warehouse_type`  CHAR(1)      NOT NULL DEFAULT '1'    COMMENT '仓库类型（1原材料 2成品 3半成品 4周转）',
  `region`          CHAR(1)      DEFAULT '1'             COMMENT '区域（1华东 2华南 3华北 4西南 5东北）',
  `address`         VARCHAR(255) DEFAULT ''              COMMENT '仓库地址',
  `manager`         VARCHAR(50)  DEFAULT ''              COMMENT '负责人',
  `contact_phone`   VARCHAR(20)  DEFAULT ''              COMMENT '联系电话',
  `status`          INT          DEFAULT 1               COMMENT '状态（0停用 1正常）',
  `description`     VARCHAR(500) DEFAULT ''              COMMENT '备注',
  `create_by`       VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  `create_time`     DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`       VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  `update_time`     DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `del_flag`        INT          DEFAULT 0               COMMENT '删除标志（0存在 1删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库管理表';

-- ----------------------------
-- 14. 成本记录表
-- 对应实体: BizCostRecord (@TableLogic)
-- ----------------------------
DROP TABLE IF EXISTS `biz_cost_record`;
CREATE TABLE `biz_cost_record` (
  `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `cost_type`   VARCHAR(20)    NOT NULL                COMMENT '成本类型（material/labor/overhead）',
  `amount`      DECIMAL(12, 2) NOT NULL DEFAULT 0      COMMENT '金额',
  `record_date` DATE           NOT NULL                COMMENT '日期',
  `remark`      VARCHAR(500)   DEFAULT ''              COMMENT '备注',
  `create_by`   VARCHAR(64)    DEFAULT ''              COMMENT '创建者',
  `create_time` DATETIME       DEFAULT NULL            COMMENT '创建时间',
  `update_by`   VARCHAR(64)    DEFAULT ''              COMMENT '更新者',
  `update_time` DATETIME       DEFAULT NULL            COMMENT '更新时间',
  `del_flag`    INT            DEFAULT 0               COMMENT '删除标志（0存在 1删除）',
  PRIMARY KEY (`id`),
  KEY `idx_cost_type` (`cost_type`),
  KEY `idx_record_date` (`record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成本记录表';

-- ----------------------------
-- 15. 财务报表记录表
-- 对应实体: BizFinanceReport (@TableLogic)
-- ----------------------------
DROP TABLE IF EXISTS `biz_finance_report`;
CREATE TABLE `biz_finance_report` (
  `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `report_type` VARCHAR(20)    NOT NULL                COMMENT '报表类型（income/expense/profit）',
  `amount`      DECIMAL(12, 2) NOT NULL DEFAULT 0      COMMENT '金额',
  `report_date` DATE           NOT NULL                COMMENT '日期',
  `remark`      VARCHAR(500)   DEFAULT ''              COMMENT '备注',
  `create_by`   VARCHAR(64)    DEFAULT ''              COMMENT '创建者',
  `create_time` DATETIME       DEFAULT NULL            COMMENT '创建时间',
  `update_by`   VARCHAR(64)    DEFAULT ''              COMMENT '更新者',
  `update_time` DATETIME       DEFAULT NULL            COMMENT '更新时间',
  `del_flag`    INT            DEFAULT 0               COMMENT '删除标志（0存在 1删除）',
  PRIMARY KEY (`id`),
  KEY `idx_report_type` (`report_type`),
  KEY `idx_report_date` (`report_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务报表记录表';

-- ----------------------------
-- 16. OAuth2 授权表（Spring Authorization Server）
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization` (
  `id`                              VARCHAR(100)  NOT NULL,
  `registered_client_id`            VARCHAR(100)  NOT NULL,
  `principal_name`                  VARCHAR(200)  NOT NULL,
  `authorization_grant_type`        VARCHAR(100)  NOT NULL,
  `attributes`                      VARCHAR(4000) DEFAULT NULL,
  `state`                           VARCHAR(500)  DEFAULT NULL,
  `authorization_code_value`        TEXT,
  `authorization_code_issued_at`    DATETIME      DEFAULT NULL,
  `authorization_code_expires_at`   DATETIME      DEFAULT NULL,
  `authorization_code_metadata`     VARCHAR(2000) DEFAULT NULL,
  `access_token_value`              TEXT,
  `access_token_issued_at`          DATETIME      DEFAULT NULL,
  `access_token_expires_at`         DATETIME      DEFAULT NULL,
  `access_token_metadata`           VARCHAR(2000) DEFAULT NULL,
  `access_token_type`               VARCHAR(100)  DEFAULT NULL,
  `access_token_scopes`             VARCHAR(1000) DEFAULT NULL,
  `refresh_token_value`             TEXT,
  `refresh_token_issued_at`         DATETIME      DEFAULT NULL,
  `refresh_token_expires_at`        DATETIME      DEFAULT NULL,
  `refresh_token_metadata`          VARCHAR(2000) DEFAULT NULL,
  `oidc_id_token_value`             TEXT,
  `oidc_id_token_issued_at`         DATETIME      DEFAULT NULL,
  `oidc_id_token_expires_at`        DATETIME      DEFAULT NULL,
  `oidc_id_token_metadata`          VARCHAR(2000) DEFAULT NULL,
  `oidc_id_token_claims`            VARCHAR(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2 授权表';

-- ----------------------------
-- 17. OAuth2 授权同意表
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization_consent`;
CREATE TABLE `oauth2_authorization_consent` (
  `registered_client_id` VARCHAR(100)  NOT NULL,
  `principal_name`       VARCHAR(200)  NOT NULL,
  `authorities`          VARCHAR(1000) NOT NULL,
  PRIMARY KEY (`registered_client_id`, `principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2 授权同意表';

-- ----------------------------
-- 18. OAuth2 客户端注册表（Spring Authorization Server）
-- 对应实体: OAuth2RegisteredClient
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_registered_client`;
CREATE TABLE `oauth2_registered_client` (
  `id`                            VARCHAR(100)  NOT NULL,
  `client_id`                     VARCHAR(100)  NOT NULL,
  `client_id_issued_at`           DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `client_secret`                 VARCHAR(200)  DEFAULT NULL,
  `client_secret_expires_at`      DATETIME      DEFAULT NULL,
  `client_name`                   VARCHAR(200)  NOT NULL,
  `client_authentication_methods` VARCHAR(1000) NOT NULL,
  `authorization_grant_types`     VARCHAR(1000) NOT NULL,
  `redirect_uris`                 VARCHAR(1000) DEFAULT NULL,
  `scopes`                        VARCHAR(1000) NOT NULL,
  `client_settings`               VARCHAR(2000) NOT NULL,
  `token_settings`                VARCHAR(2000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2 客户端注册表';

SET FOREIGN_KEY_CHECKS = 1;
