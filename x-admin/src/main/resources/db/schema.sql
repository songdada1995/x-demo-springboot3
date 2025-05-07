-- 数据字典表
CREATE TABLE IF NOT EXISTS sys_dict (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    name VARCHAR(50) NOT NULL COMMENT '字典名称',
    code VARCHAR(50) NOT NULL COMMENT '字典编码',
    description VARCHAR(200) COMMENT '字典描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    create_time DATETIME COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted BOOLEAN DEFAULT FALSE COMMENT '是否删除',
    UNIQUE KEY uk_code (code)
) COMMENT '数据字典表';

-- 数据字典项表
CREATE TABLE IF NOT EXISTS sys_dict_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    dict_id BIGINT NOT NULL COMMENT '字典ID',
    label VARCHAR(50) NOT NULL COMMENT '字典项标签',
    value VARCHAR(50) NOT NULL COMMENT '字典项值',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    create_time DATETIME COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted BOOLEAN DEFAULT FALSE COMMENT '是否删除',
    UNIQUE KEY uk_dict_value (dict_id, value)
) COMMENT '数据字典项表';

-- 客户端表
CREATE TABLE IF NOT EXISTS sys_client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    client_id VARCHAR(50) NOT NULL COMMENT '客户端ID',
    client_secret VARCHAR(100) NOT NULL COMMENT '客户端密钥',
    client_name VARCHAR(50) NOT NULL COMMENT '客户端名称',
    redirect_uri VARCHAR(200) COMMENT '重定向URI',
    grant_types VARCHAR(100) COMMENT '授权类型',
    scopes VARCHAR(100) COMMENT '授权范围',
    access_token_validity INT DEFAULT 3600 COMMENT '访问令牌有效期（秒）',
    refresh_token_validity INT DEFAULT 86400 COMMENT '刷新令牌有效期（秒）',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    create_time DATETIME COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    deleted BOOLEAN DEFAULT FALSE COMMENT '是否删除',
    UNIQUE KEY uk_client_id (client_id)
) COMMENT '客户端表'; 