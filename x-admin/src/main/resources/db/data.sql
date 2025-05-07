-- 初始化数据字典
INSERT INTO sys_dict (name, code, description, status, create_time, deleted)
VALUES 
    ('用户状态', 'sys_user_status', '用户状态字典', 1, NOW(), false),
    ('菜单类型', 'sys_menu_type', '菜单类型字典', 1, NOW(), false),
    ('客户端状态', 'sys_client_status', '客户端状态字典', 1, NOW(), false);

-- 初始化数据字典项
INSERT INTO sys_dict_item (dict_id, label, value, sort, status, create_time, deleted)
VALUES 
    (1, '正常', '1', 1, 1, NOW(), false),
    (1, '禁用', '0', 2, 1, NOW(), false),
    (2, '目录', '0', 1, 1, NOW(), false),
    (2, '菜单', '1', 2, 1, NOW(), false),
    (2, '按钮', '2', 3, 1, NOW(), false),
    (3, '正常', '1', 1, 1, NOW(), false),
    (3, '禁用', '0', 2, 1, NOW(), false);

-- 初始化客户端
INSERT INTO sys_client (client_id, client_secret, client_name, redirect_uri, grant_types, scopes, status, create_time, deleted)
VALUES 
    ('x-admin', '{noop}secret', '后台管理系统', 'http://localhost:8080/login/oauth2/code/x-admin', 'authorization_code,refresh_token', 'read,write', 1, NOW(), false),
    ('x-web', '{noop}secret', '前台网站', 'http://localhost:3000/login/oauth2/code/x-web', 'authorization_code,refresh_token', 'read', 1, NOW(), false); 