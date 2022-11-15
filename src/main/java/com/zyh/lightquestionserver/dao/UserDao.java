package com.zyh.lightquestionserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.lightquestionserver.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
