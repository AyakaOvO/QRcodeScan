package com.tyut.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.Date;

@Mapper
public interface StudentMapper {

    // 根据ID查询学生信息

    // 查询所有学生信息

    // 插入学生信息
    @Insert("INSERT INTO ${tablename}(id, name, signintime, state, subject) VALUES(#{id}, #{name}, #{signintime}, #{state}, #{subject})")
    int insertIN(@Param("tablename") String tablename, @Param("id") Integer id, @Param("name") String name, @Param("signintime") String signintime, @Param("state") String state, @Param("subject") String subject);

}