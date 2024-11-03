package com.yygh.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yygh.model.user.UserInfo;
import com.yygh.vo.user.UserInfoQueryVo;
import org.apache.ibatis.annotations.Param;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.mapper
 * @Description
 * @date 2024-10-16 20:49
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    IPage<UserInfo> list(Page<UserInfo> page, @Param("userInfoQueryVo") UserInfoQueryVo userInfoQueryVo);
}
