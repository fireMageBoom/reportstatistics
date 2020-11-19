package com.yzcloud.reportstatistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzcloud.reportstatistics.mapper.JwtUserMapper;
import com.yzcloud.reportstatistics.model.jwt.JwtUser;
import com.yzcloud.reportstatistics.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<JwtUserMapper, JwtUser> implements UserService {
}
