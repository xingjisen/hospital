package com.yygh.cmn.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.cmn.client
 * @Description
 * @date 2024-09-25 19:00
 */
@FeignClient(name = "service-cmn", path = "/admin/cmn/dict")
public interface DictFeignClient {
    /**
     * 根据dictcode和value查询
     */
    @GetMapping("/getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode") String dictCode, @PathVariable("value") String value);

    /**
     * 根据value查询
     */
    @GetMapping("/getName/{value}")
    public String getName(@PathVariable("value") String value);
}
