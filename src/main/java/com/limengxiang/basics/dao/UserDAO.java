package com.limengxiang.basics.dao;

import com.limengxiang.basics.model.UserModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDAO {

    public Integer insert(@Param("username") String username, @Param("mobile") String mobile);

    public UserModel selectOne(@Param("tableName") String tableName, @Param("id") Integer id);

    public List<UserModel> fuzzySearch(@Param("username") String username, @Param("mobile") String mobile);

    public List<UserModel> searchByUsernameOrMobile(@Param("username") String username, @Param("mobile") String mobile);
}
