# Nacos 配置导入说明

将本目录下的 YAML 文件内容导入到 Nacos 配置中心。

## 前置：配置 hosts 文件

项目使用 hosts 域名方式，需先配置 hosts 文件：

| 域名 | 默认指向 | 说明 |
|------|----------|------|
| x-nacos.com | 127.0.0.1 | Nacos |
| x-sentinel.com | 127.0.0.1 | Sentinel |
| x-mysql.com | 127.0.0.1 | MySQL |
| x-redis.com | 127.0.0.1 | Redis |
| x-gateway.com | 127.0.0.1 | 网关（OAuth2 issuer） |

**Windows**：编辑 `C:\Windows\System32\drivers\etc\hosts`（需管理员权限）  
**Linux/Mac**：编辑 `/etc/hosts`

可参考 `hosts.example` 内容，根据实际 IP 修改后追加到 hosts 文件。

## 导入步骤

1. 登录 Nacos 控制台
2. 进入「配置管理」→「配置列表」
3. 点击「+」创建配置
4. Data ID、Group 见下表，配置格式选择 **YAML**
5. 将对应文件内容粘贴到配置内容中保存

## 配置清单

| Data ID | Group | 说明 |
|---------|-------|------|
| common | DEFAULT_GROUP | 公共配置（DB、Redis、JWT、Sentinel） |
| x-auth | DEFAULT_GROUP | 认证服务 |
| x-upms-biz | DEFAULT_GROUP | 用户权限服务 |
| x-gateway | DEFAULT_GROUP | 网关 |

## 多环境

- **开发**：使用 DEFAULT_GROUP 或 namespace=dev
- **生产**：建议新建 namespace=prod，在对应 namespace 下创建上述配置

## 注意事项

- 导入前请根据实际环境修改 `common.yaml` 中的数据库、Redis、JWT 等敏感信息
- 首次导入后，需重启各服务或等待配置刷新生效
- **未导入 Nacos 时**：应用仍可启动，将使用各模块 `application.yml` 中的本地配置兜底
