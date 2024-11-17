package com.yygh.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yygh.model.user.Auth;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.mapper
 * @Description
 * @date 2024-10-16 20:49
 */
public interface AuthMapper extends BaseMapper<Auth> {
    Auth getUser(Auth auth);
}
