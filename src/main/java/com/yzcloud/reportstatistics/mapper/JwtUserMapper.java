package com.yzcloud.reportstatistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzcloud.reportstatistics.model.jwt.JwtUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JwtUserMapper extends BaseMapper<JwtUser> {
}
