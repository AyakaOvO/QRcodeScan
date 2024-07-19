package com.tyut.Mapper;

import com.tyut.domain.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {
    @Select("select * from login where id = ${id} and password =${password}")
    Student findById(@Param("id") Integer id , @Param("password")String password);
}
