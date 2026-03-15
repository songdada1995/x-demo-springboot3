package com.example.upms.api.feign;

import com.example.common.core.response.R;
import com.example.upms.api.domain.entity.OAuth2RegisteredClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 远程客户端服务
 */
@FeignClient(contextId = "remoteClientDetailsService", value = "x-upms-biz")
public interface RemoteClientDetailsService {

    /**
     * 通过clientId 查询客户端信息 (未登录，需要无token 内部调用)
     *
     * @param clientId 用户名
     * @return R
     */
    @GetMapping("/client/getClientDetailsById/{clientId}")
    R<OAuth2RegisteredClient> getClientDetailsById(@PathVariable("clientId") String clientId);

    @PostMapping("/client/save")
    R<Void> insert(@RequestBody OAuth2RegisteredClient client);

    @GetMapping("/client/selectById/{id}")
    R<OAuth2RegisteredClient> selectById(@PathVariable("id") String id);
}
