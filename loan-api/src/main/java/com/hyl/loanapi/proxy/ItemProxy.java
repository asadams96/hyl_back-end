package com.hyl.loanapi.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Component
@FeignClient(name = "item-api", url = "${hyl.gateway.url}"+"/item")
public interface ItemProxy {

    @PostMapping(value = "/add-tracking-sheet")
    void postComment(@RequestHeader HashMap<String, String> headerMap,
                     @RequestBody HashMap<String, String> hashMap);

    @GetMapping("/check-sub-ref")
    boolean checkReference(@RequestHeader HashMap<String, String> headerMap,
                           @RequestParam String reference);
}
