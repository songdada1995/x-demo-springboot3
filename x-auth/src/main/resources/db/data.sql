-- 初始化管理员用户（密码：123456）
INSERT INTO sys_user (username, password, nickname, status, create_time, deleted)
VALUES ('admin', '$2a$10$X/uMNuiw1Uw1RaFqM1UqUO3Yz1JZqK1qK1qK1qK1qK1qK1qK1qK1q', '管理员', 1, NOW(), false);

-- 初始化角色
INSERT INTO sys_role (name, code, description, status, create_time, deleted)
VALUES ('管理员', 'ROLE_ADMIN', '系统管理员', 1, NOW(), false);

-- 初始化菜单
INSERT INTO sys_menu (parent_id, name, path, component, perms, type, icon, order_num, status, create_time, deleted)
VALUES
    (null, '系统管理', '/system', null, null, 0, 'system', 1, 1, NOW(), false),
    (1, '用户管理', '/system/user', 'system/user/index', 'sys:user:list', 1, 'user', 1, 1, NOW(), false),
    (2, '用户新增', null, null, 'sys:user:add', 2, null, 1, 1, NOW(), false),
    (2, '用户修改', null, null, 'sys:user:update', 2, null, 2, 1, NOW(), false),
    (2, '用户删除', null, null, 'sys:user:delete', 2, null, 3, 1, NOW(), false),
    (2, '用户分配角色', null, null, 'sys:user:assign', 2, null, 4, 1, NOW(), false),
    (1, '角色管理', '/system/role', 'system/role/index', 'sys:role:list', 1, 'role', 2, 1, NOW(), false),
    (7, '角色新增', null, null, 'sys:role:add', 2, null, 1, 1, NOW(), false),
    (7, '角色修改', null, null, 'sys:role:update', 2, null, 2, 1, NOW(), false),
    (7, '角色删除', null, null, 'sys:role:delete', 2, null, 3, 1, NOW(), false),
    (7, '角色分配菜单', null, null, 'sys:role:assign', 2, null, 4, 1, NOW(), false),
    (1, '菜单管理', '/system/menu', 'system/menu/index', 'sys:menu:list', 1, 'menu', 3, 1, NOW(), false),
    (12, '菜单新增', null, null, 'sys:menu:add', 2, null, 1, 1, NOW(), false),
    (12, '菜单修改', null, null, 'sys:menu:update', 2, null, 2, 1, NOW(), false),
    (12, '菜单删除', null, null, 'sys:menu:delete', 2, null, 3, 1, NOW(), false);

-- 初始化用户角色关联
INSERT INTO sys_user_role (user_id, role_id)
VALUES (1, 1);

-- 初始化角色菜单关联
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
    (1, 7), (1, 8), (1, 9), (1, 10), (1, 11),
    (1, 12), (1, 13), (1, 14), (1, 15);

-- 初始化 OAuth2 客户端
INSERT INTO oauth2_registered_client (
    id, client_id, client_id_issued_at, client_secret, client_name,
    client_authentication_methods, authorization_grant_types, redirect_uris, scopes,
    client_settings, token_settings
) VALUES (
    '1',
    'x-admin',
    CURRENT_TIMESTAMP,
    '{noop}secret',
    '后台管理系统',
    'client_secret_basic',
    'authorization_code,refresh_token',
    'http://localhost:8080/login/oauth2/code/x-admin',
    'openid,read,write',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":"RS256","settings.token.access-token-time-to-live":300,"settings.token.access-token-format":"self-contained","settings.token.refresh-token-time-to-live":3600,"settings.token.authorization-code-time-to-live":300}'
);

INSERT INTO oauth2_registered_client (
    id, client_id, client_id_issued_at, client_secret, client_name,
    client_authentication_methods, authorization_grant_types, redirect_uris, scopes,
    client_settings, token_settings
) VALUES (
    '2',
    'x-web',
    CURRENT_TIMESTAMP,
    '{noop}secret',
    '前台网站',
    'client_secret_basic',
    'authorization_code,refresh_token',
    'http://localhost:3000/login/oauth2/code/x-web',
    'openid,read',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":"RS256","settings.token.access-token-time-to-live":300,"settings.token.access-token-format":"self-contained","settings.token.refresh-token-time-to-live":3600,"settings.token.authorization-code-time-to-live":300}'
); 