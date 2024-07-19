package com.tyut.Mapper;

import com.tyut.domain.Result;
import com.tyut.domain.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface TeacherMapper {
    @Select("SELECT  l.id, l.name, s.state , s.signintime FROM  login l LEFT JOIN `软件2130` s ON l.id = s.id AND l.sclass = #{sclass} AND s.subject = #{subject} AND s.signintime = #{signintime}")
    List<Result> findStudent(@Param("sclass") String sclass, @Param("subject") String subjecte ,@Param("signintime") String signintime);
    @Select("SELECT s.id, s.name, s.state, s.signintime FROM `软件2130` s WHERE s.subject = #{subject} ")
    List<Result> findStudentBySubject(String subject);


}
