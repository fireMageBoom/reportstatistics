package com.yzcloud.reportstatistics.controller;

import com.yzcloud.reportstatistics.model.AbstractResponseEntity;
import com.yzcloud.reportstatistics.model.SuccessResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationRestController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationRestController.class);

    @PostMapping("/reportstatistic")
    public AbstractResponseEntity authentication() {
        logger.info("test");
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return new SuccessResponseEntity();
    }
}
