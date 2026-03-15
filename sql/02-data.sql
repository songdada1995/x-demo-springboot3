-- =====================================================
-- x-demo-springboot3 初始化数据脚本
-- 使用方式: 在执行 01-schema.sql 后执行本文件
-- 注意: 使用 INSERT IGNORE 避免重复插入
-- =====================================================

SET NAMES utf8mb4;

-- ===========================
-- 一、系统用户 & 角色 & 关联
-- ===========================

-- 管理员用户（密码：admin123）
INSERT IGNORE INTO `sys_user` (`user_id`, `username`, `password`, `nickname`, `status`, `lock_flag`, `login_limit`, `create_time`, `del_flag`)
VALUES (1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '管理员', 0, 0, 5, NOW(), 0);

-- 角色
INSERT IGNORE INTO `sys_role` (`role_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `status`, `remark`, `create_time`, `del_flag`)
VALUES
  (1, '超级管理员', 'admin',  1, '1', 0, '超级管理员，拥有所有权限', NOW(), 0),
  (2, '普通用户',   'user',   2, '2', 0, '普通用户，拥有基本操作权限', NOW(), 0),
  (3, '访客',       'viewer', 3, '5', 0, '访客，只有查看权限',         NOW(), 0);

-- admin 用户关联 admin 角色
INSERT IGNORE INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- ===========================
-- 二、菜单权限
-- ===========================
-- menu_type: M=目录, C=菜单, F=按钮
-- visible:   0=显示, 1=隐藏
-- status:    0=正常, 1=停用
-- 注意: path 字段对 C 类型存储完整路由路径，便于前端直接跳转

-- 一级菜单（目录 M）—— 顶部导航栏
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `remark`, `create_by`, `create_time`) VALUES
  (1,  '系统管理', 0, 1, '/system',   NULL, 'M', '0', '0', NULL, 'setting',  '系统管理目录', 'admin', NOW()),
  (4,  '业务平台', 0, 2, '/business', NULL, 'M', '0', '0', NULL, 'appstore', '业务平台目录', 'admin', NOW());

-- 二级菜单（菜单 C）—— 系统管理下
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `remark`, `create_by`, `create_time`) VALUES
  (100, '仪表盘', 1, 0, '/dashboard', 'pages/Dashboard', 'C', '0', '0', 'system:dashboard:view', 'dashboard', '仪表盘菜单', 'admin', NOW()),
  (101, '用户管理', 1, 1, '/system/users',       'system/users/index',       'C', '0', '0', 'system:user:list', 'user',   '用户管理菜单', 'admin', NOW()),
  (102, '角色管理', 1, 2, '/system/roles',       'system/roles/index',       'C', '0', '0', 'system:role:list', 'team',   '角色管理菜单', 'admin', NOW()),
  (103, '菜单管理', 1, 3, '/system/permissions', 'system/permissions/index', 'C', '0', '0', 'system:menu:list', 'safety', '菜单管理菜单', 'admin', NOW());

-- 二级目录（目录 M）—— 业务平台下
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `remark`, `create_by`, `create_time`) VALUES
  (3,  '财务管理', 4, 1, '', NULL, 'M', '0', '0', NULL, 'fund',     '财务管理目录', 'admin', NOW()),
  (2,  '业务管理', 4, 2, '', NULL, 'M', '0', '0', NULL, 'appstore', '业务管理目录', 'admin', NOW());

-- 三级菜单（菜单 C）—— 财务管理下
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `remark`, `create_by`, `create_time`) VALUES
  (301, '成本管理', 3, 1, '/business/cost',    'finance/cost/index',   'C', '0', '0', 'biz:cost:list',   'money-collect', '成本管理菜单', 'admin', NOW()),
  (302, '财务报表', 3, 2, '/business/finance',  'finance/report/index', 'C', '0', '0', 'biz:report:list', 'bar-chart',     '财务报表菜单', 'admin', NOW());

-- 三级菜单（菜单 C）—— 业务管理下
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `remark`, `create_by`, `create_time`) VALUES
  (201, '账号管理', 2, 1, '/business/account',   'business/account/index',   'C', '0', '0', 'biz:account:list',   'contacts', '账号管理菜单', 'admin', NOW()),
  (202, '仓库管理', 2, 2, '/business/warehouse', 'business/warehouse/index', 'C', '0', '0', 'biz:warehouse:list', 'shop',     '仓库管理菜单', 'admin', NOW());

-- 按钮权限（F）—— 用户管理
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
  (1011, '用户查询', 101, 1, '', NULL, 'F', '0', '0', 'system:user:query',    '#', 'admin', NOW()),
  (1012, '用户新增', 101, 2, '', NULL, 'F', '0', '0', 'system:user:add',      '#', 'admin', NOW()),
  (1013, '用户修改', 101, 3, '', NULL, 'F', '0', '0', 'system:user:edit',     '#', 'admin', NOW()),
  (1014, '用户删除', 101, 4, '', NULL, 'F', '0', '0', 'system:user:remove',   '#', 'admin', NOW()),
  (1015, '重置密码', 101, 5, '', NULL, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', NOW());

-- 按钮权限（F）—— 角色管理
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
  (1021, '角色查询', 102, 1, '', NULL, 'F', '0', '0', 'system:role:query',  '#', 'admin', NOW()),
  (1022, '角色新增', 102, 2, '', NULL, 'F', '0', '0', 'system:role:add',    '#', 'admin', NOW()),
  (1023, '角色修改', 102, 3, '', NULL, 'F', '0', '0', 'system:role:edit',   '#', 'admin', NOW()),
  (1024, '角色删除', 102, 4, '', NULL, 'F', '0', '0', 'system:role:remove', '#', 'admin', NOW());

-- 按钮权限（F）—— 菜单管理
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
  (1031, '菜单查询', 103, 1, '', NULL, 'F', '0', '0', 'system:menu:query',  '#', 'admin', NOW()),
  (1032, '菜单新增', 103, 2, '', NULL, 'F', '0', '0', 'system:menu:add',    '#', 'admin', NOW()),
  (1033, '菜单修改', 103, 3, '', NULL, 'F', '0', '0', 'system:menu:edit',   '#', 'admin', NOW()),
  (1034, '菜单删除', 103, 4, '', NULL, 'F', '0', '0', 'system:menu:remove', '#', 'admin', NOW());

-- 按钮权限（F）—— 账号管理
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
  (2011, '账号查询', 201, 1, '', NULL, 'F', '0', '0', 'biz:account:query',  '#', 'admin', NOW()),
  (2012, '账号新增', 201, 2, '', NULL, 'F', '0', '0', 'biz:account:add',    '#', 'admin', NOW()),
  (2013, '账号修改', 201, 3, '', NULL, 'F', '0', '0', 'biz:account:edit',   '#', 'admin', NOW()),
  (2014, '账号删除', 201, 4, '', NULL, 'F', '0', '0', 'biz:account:remove', '#', 'admin', NOW());

-- 按钮权限（F）—— 仓库管理
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
  (2021, '仓库查询', 202, 1, '', NULL, 'F', '0', '0', 'biz:warehouse:query',  '#', 'admin', NOW()),
  (2022, '仓库新增', 202, 2, '', NULL, 'F', '0', '0', 'biz:warehouse:add',    '#', 'admin', NOW()),
  (2023, '仓库修改', 202, 3, '', NULL, 'F', '0', '0', 'biz:warehouse:edit',   '#', 'admin', NOW()),
  (2024, '仓库删除', 202, 4, '', NULL, 'F', '0', '0', 'biz:warehouse:remove', '#', 'admin', NOW());

-- 按钮权限（F）—— 成本管理
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
  (3011, '成本查询', 301, 1, '', NULL, 'F', '0', '0', 'biz:cost:query',  '#', 'admin', NOW()),
  (3012, '成本新增', 301, 2, '', NULL, 'F', '0', '0', 'biz:cost:add',    '#', 'admin', NOW()),
  (3013, '成本修改', 301, 3, '', NULL, 'F', '0', '0', 'biz:cost:edit',   '#', 'admin', NOW()),
  (3014, '成本删除', 301, 4, '', NULL, 'F', '0', '0', 'biz:cost:remove', '#', 'admin', NOW());

-- 按钮权限（F）—— 财务报表
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
  (3021, '报表查询', 302, 1, '', NULL, 'F', '0', '0', 'biz:report:query',  '#', 'admin', NOW()),
  (3022, '报表新增', 302, 2, '', NULL, 'F', '0', '0', 'biz:report:add',    '#', 'admin', NOW()),
  (3023, '报表修改', 302, 3, '', NULL, 'F', '0', '0', 'biz:report:edit',   '#', 'admin', NOW()),
  (3024, '报表删除', 302, 4, '', NULL, 'F', '0', '0', 'biz:report:remove', '#', 'admin', NOW());

-- ===========================
-- 三、角色-菜单关联
-- ===========================

-- 超级管理员（role_id=1）拥有所有菜单
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
  (1,1),(1,4),(1,2),(1,3),
  (1,100),(1,101),(1,102),(1,103),(1,201),(1,202),(1,301),(1,302),
  (1,1011),(1,1012),(1,1013),(1,1014),(1,1015),
  (1,1021),(1,1022),(1,1023),(1,1024),
  (1,1031),(1,1032),(1,1033),(1,1034),
  (1,2011),(1,2012),(1,2013),(1,2014),
  (1,2021),(1,2022),(1,2023),(1,2024),
  (1,3011),(1,3012),(1,3013),(1,3014),
  (1,3021),(1,3022),(1,3023),(1,3024);

-- 普通用户（role_id=2）拥有业务平台全部权限 + 仪表盘
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
  (2,1),(2,100),(2,4),(2,2),(2,3),
  (2,201),(2,202),(2,301),(2,302),
  (2,2011),(2,2012),(2,2013),(2,2014),
  (2,2021),(2,2022),(2,2023),(2,2024),
  (2,3011),(2,3012),(2,3013),(2,3014),
  (2,3021),(2,3022),(2,3023),(2,3024);

-- 访客（role_id=3）只有查看权限 + 仪表盘
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
  (3,1),(3,100),(3,4),(3,2),(3,3),
  (3,101),(3,102),(3,103),(3,201),(3,202),(3,301),(3,302),
  (3,1011),(3,1021),(3,1031),
  (3,2011),(3,2021),(3,3011),(3,3021);

-- ===========================
-- 四、OAuth2 客户端注册
-- ===========================
INSERT IGNORE INTO `oauth2_registered_client`
  (`id`, `client_id`, `client_id_issued_at`, `client_secret`, `client_name`,
   `client_authentication_methods`, `authorization_grant_types`, `redirect_uris`,
   `scopes`, `client_settings`, `token_settings`)
VALUES
  ('x-web', 'x-web', CURRENT_TIMESTAMP, '{noop}secret', '前台网站',
   'client_secret_basic', 'authorization_code,refresh_token',
   'http://localhost:3000/login/oauth2/code/x-web',
   'openid,read,write',
   '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}',
   '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",86400.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');

-- ===========================
-- 五、通知公告
-- ===========================
INSERT IGNORE INTO `sys_notice` (`notice_id`, `notice_title`, `notice_content`, `notice_type`, `status`, `create_by`, `create_time`) VALUES
  (1, '系统上线通知', '系统已正式上线运行，请各部门做好相关准备工作。',                    '1', 0, 'admin', NOW()),
  (2, '系统维护公告', '系统将于本周日凌晨2:00-6:00进行维护升级，届时系统将暂停服务。',     '2', 0, 'admin', NOW()),
  (3, '新功能发布',   '新增仓库管理和财务报表模块，欢迎使用并反馈问题。',                  '1', 0, 'admin', NOW());

-- ===========================
-- 六、业务数据（示例）
-- ===========================

-- 业务账号
INSERT IGNORE INTO `biz_account` (`id`, `account_name`, `account_type`, `contact_person`, `contact_phone`, `email`, `status`, `description`, `create_time`) VALUES
  (1, '华东分公司', '2', '张三', '13800138001', 'zhangsan@example.com', 1, '华东区域总部',   NOW()),
  (2, '个人测试号', '1', '李四', '13800138002', 'lisi@example.com',     1, '开发测试用账号', NOW()),
  (3, '系统服务号', '3', '系统', '10000',       'system@example.com',   1, '系统内置服务号', NOW());

-- 仓库
INSERT IGNORE INTO `biz_warehouse` (`id`, `warehouse_name`, `warehouse_type`, `region`, `address`, `manager`, `contact_phone`, `status`, `description`, `create_time`) VALUES
  (1, '华东原材料仓', '1', '1', '上海市浦东新区张江路100号', '王五',  '13900139001', 1, '华东区原材料存储',   NOW()),
  (2, '华南成品仓',   '2', '2', '广州市天河区科韵路50号',   '赵六',  '13900139002', 1, '华南区成品存储',     NOW()),
  (3, '华北周转仓',   '4', '3', '北京市朝阳区望京街10号',   '孙七',  '13900139003', 1, '华北区物流周转仓库', NOW());

-- 成本记录
INSERT IGNORE INTO `biz_cost_record` (`id`, `cost_type`, `amount`, `record_date`, `remark`, `create_time`) VALUES
  (1,  'material', 15000.00, '2026-01-15', '原材料采购-钢材',   NOW()),
  (2,  'labor',     8000.00, '2026-01-20', '一月份工人工资',     NOW()),
  (3,  'overhead',  3000.00, '2026-01-25', '水电费',             NOW()),
  (4,  'material', 22000.00, '2026-02-10', '原材料采购-铝材',   NOW()),
  (5,  'labor',     8500.00, '2026-02-20', '二月份工人工资',     NOW()),
  (6,  'overhead',  3200.00, '2026-02-25', '水电费+网络费',     NOW()),
  (7,  'material', 18000.00, '2026-03-05', '原材料采购-塑料',   NOW()),
  (8,  'labor',     9000.00, '2026-03-10', '三月份工人工资',     NOW());

-- 财务报表
INSERT IGNORE INTO `biz_finance_report` (`id`, `report_type`, `amount`, `report_date`, `remark`, `create_time`) VALUES
  (1,  'income',  50000.00, '2026-01-31', '一月份销售收入',  NOW()),
  (2,  'expense', 26000.00, '2026-01-31', '一月份总支出',    NOW()),
  (3,  'profit',  24000.00, '2026-01-31', '一月份净利润',    NOW()),
  (4,  'income',  65000.00, '2026-02-28', '二月份销售收入',  NOW()),
  (5,  'expense', 33700.00, '2026-02-28', '二月份总支出',    NOW()),
  (6,  'profit',  31300.00, '2026-02-28', '二月份净利润',    NOW()),
  (7,  'income',  45000.00, '2026-03-15', '三月份销售收入',  NOW()),
  (8,  'expense', 27000.00, '2026-03-15', '三月份总支出',    NOW());

-- ===========================
-- 七、数据字典
-- ===========================
INSERT IGNORE INTO `sys_dict` (`id`, `name`, `code`, `description`, `status`, `create_time`, `deleted`) VALUES
  (1, '用户状态',   'sys_user_status',   '用户状态字典',   1, NOW(), 0),
  (2, '菜单类型',   'sys_menu_type',     '菜单类型字典',   1, NOW(), 0),
  (3, '客户端状态', 'sys_client_status', '客户端状态字典', 1, NOW(), 0);

INSERT IGNORE INTO `sys_dict_item` (`id`, `dict_id`, `label`, `value`, `sort`, `status`, `create_time`, `deleted`) VALUES
  (1, 1, '正常', '0', 1, 1, NOW(), 0),
  (2, 1, '停用', '1', 2, 1, NOW(), 0),
  (3, 2, '目录', 'M', 1, 1, NOW(), 0),
  (4, 2, '菜单', 'C', 2, 1, NOW(), 0),
  (5, 2, '按钮', 'F', 3, 1, NOW(), 0),
  (6, 3, '正常', '1', 1, 1, NOW(), 0),
  (7, 3, '禁用', '0', 2, 1, NOW(), 0);

-- 客户端
INSERT IGNORE INTO `sys_client` (`id`, `client_id`, `client_secret`, `client_name`, `redirect_uri`, `grant_types`, `scopes`, `status`, `create_time`, `deleted`) VALUES
  (1, 'x-upms-biz', '{noop}secret', '后台管理系统', 'http://localhost:9003/login/oauth2/code/x-upms-biz', 'authorization_code,refresh_token', 'read,write', 1, NOW(), 0),
  (2, 'x-web',      '{noop}secret', '前台网站',     'http://localhost:3000/login/oauth2/code/x-web',      'authorization_code,refresh_token', 'read',       1, NOW(), 0);
