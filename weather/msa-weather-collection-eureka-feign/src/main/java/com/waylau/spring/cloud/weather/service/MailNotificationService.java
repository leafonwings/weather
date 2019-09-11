package com.waylau.spring.cloud.weather.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("msa-weather-mail-eureka")
public interface MailNotificationService {

    @GetMapping("/mail")
    boolean mailNotice() throws Exception;
}
