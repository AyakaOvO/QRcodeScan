package com.tyut;

import com.tyut.Mapper.AdminMapper;
import com.tyut.Mapper.LoginMapper;
import com.tyut.Mapper.StudentMapper;
import com.tyut.Mapper.TeacherMapper;
import com.tyut.domain.Result;
import com.tyut.domain.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
class Ks202464ApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    private LoginMapper loginMapper;
    @Test
    void login() {
        Student a = loginMapper.findById(2021006022, "2021006022");
        System.out.println(a);
    }
    @Autowired
    private StudentMapper studentMapper;
    @Test
    public void insertStudent() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String signintime = localDate.format(fmt);
        int a = studentMapper.insertIN("软件2130",2021006022,"卫思宇", signintime,"已签到","SpringBoot");
    }
    @Autowired
    private TeacherMapper teachermapper;
    @Test
    public void insertTeacher() {
        String subject = "sbks";
        
        List<Result> a = teachermapper.findStudentBySubject(subject);
        for (Result results : a) {
            System.out.println(results);
        }
    }
    @Autowired
    private AdminMapper adminMapper;
    @Test
    public void insertAdmin() {
        int a = adminMapper.updateRalid(2021006022, 2);
        System.out.println(a);
    }
    @Test
    public void time() throws ParseException {
        String s = "2024-06-18";
        String subject = "SpringBoot";
        String sclass= "软件2130";
        List<Result> a = teachermapper.findStudent(sclass,subject,s);
        for (Result results : a) {
            if(results.getState() == null)
            results.setState("未签到");
        }
        for (Result results : a) {
            System.out.println(results);
        }

    }
}
