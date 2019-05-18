package com.leyou.page.clients;


import com.leyou.item.api.SpecificationApi;
import com.leyou.item.pojo.SpecParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {


}
