package com.hyl.userapi.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient(name = "mail-server", url = "${hyl.gateway.url}"+"/mail")
public interface MailProxy {
}
