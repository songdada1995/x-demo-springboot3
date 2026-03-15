package com.example.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.core.response.R;
import com.example.upms.api.domain.entity.OAuth2RegisteredClient;
import com.example.upms.mapper.OAuth2RegisteredClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class OAuth2ClientController {

    private final OAuth2RegisteredClientMapper clientMapper;

    @GetMapping("/getClientDetailsById/{clientId}")
    public R<OAuth2RegisteredClient> getClientDetailsById(@PathVariable("clientId") String clientId) {
        OAuth2RegisteredClient client = clientMapper.selectOne(
                new LambdaQueryWrapper<OAuth2RegisteredClient>()
                        .eq(OAuth2RegisteredClient::getClientId, clientId));
        return R.ok(client);
    }

    @PostMapping("/save")
    public R<Void> insert(@RequestBody OAuth2RegisteredClient client) {
        clientMapper.insert(client);
        return R.ok();
    }

    @GetMapping("/selectById/{id}")
    public R<OAuth2RegisteredClient> selectById(@PathVariable("id") String id) {
        OAuth2RegisteredClient client = clientMapper.selectById(id);
        return R.ok(client);
    }
}
