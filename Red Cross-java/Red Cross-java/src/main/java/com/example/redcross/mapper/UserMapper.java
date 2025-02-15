package com.example.redcross.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.redcross.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
