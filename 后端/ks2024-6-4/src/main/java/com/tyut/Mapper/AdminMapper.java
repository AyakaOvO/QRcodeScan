package com.tyut.Mapper;

import com.tyut.domain.Admin;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface AdminMapper {

    // 根据ID查询人员信息
    @Select("SELECT * FROM admin WHERE id = #{id}")
    Admin findById(int id);

    // 根据用户名和密码查询管理员信息（用于登录验证）
    // 注意：这里为了演示，直接使用了SQL拼接，实际开发中应使用预编译SQL防止SQL注入
    @Select("SELECT * FROM admin WHERE name = #{name} AND password = #{password}")
    Admin findByNameAndPassword(String name, String password);

    // 插入新的管理员信息
    @Insert("INSERT INTO admin(name, password, valid) VALUES(#{name}, #{password}, #{valid})")
    int insert(Admin admin);

    // 更新权限
    @Update("UPDATE login SET ralid = #{ralid} WHERE id = #{id}")
    int updateRalid(@Param("id") Integer id, @Param("ralid") Integer ralid);

    // 根据ID删除管理员信息
    @Delete("DELETE FROM admin WHERE id = #{id}")
    int deleteById(int id);
}