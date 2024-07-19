package com.tyut.controller;

import com.tyut.Mapper.AdminMapper;
import com.tyut.Mapper.LoginMapper;
import com.tyut.Mapper.StudentMapper;
import com.tyut.Mapper.TeacherMapper;
import com.tyut.domain.Result;
import com.tyut.domain.Signinin;
import com.tyut.domain.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class SignInController {
    private static final Logger log = LoggerFactory.getLogger(SignInController.class);
    @Autowired
    private LoginMapper loginMapper;
    @PostMapping("/toLogin")
    public Student toLogin(@RequestParam Integer id,@RequestParam String password) {
      Student result = loginMapper.findById(id,password);
     return result;
    }
    //session保存了签到信息，包括id,class,teachername,posttime
    @Autowired
    StudentMapper studentMapper;
    @PostMapping("/signin")
    public String signIn(@RequestBody Signinin signinin) throws ParseException {
        // 获取session中的信息
        Integer id =  signinin.getId();
        String name =  signinin.getName();
        String subject = signinin.getSubject();
        String sclass = signinin.getSclass();
        long posttime = signinin.getPosttime()/1000;
        long creattime = signinin.getCreattime()/1000;
        // 计算时间差
        long seconds = posttime-creattime;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String signintime = localDate.format(fmt);
        if(seconds>5){
            return "签到失败，签到时间已超过5秒";
        }
        else {
            String state = "已签到";
            studentMapper.insertIN(sclass, id, name, signintime, state, subject);
            return "签到成功";
            }
        // 这里进行签名验证和时效性验证等操作
    }
    @Autowired
    TeacherMapper teacherMapper;
    @PostMapping("/teacherselect")
    public List<Result> teacherSelect(String subject) throws ParseException {
        System.out.println(subject);
        List<Result> results = teacherMapper.findStudentBySubject(subject);
        System.out.println(results);
        if(results.isEmpty()){
            System.out.println("YES");
        }
        for (Result result : results) {
            System.out.println(result);
        }
        return results;
    }
    @PostMapping("/teacherselect2")
    public List<Result> teacherSelect2(String subject,String sclass,String signintime) throws ParseException {
        System.out.println(sclass);
        System.out.println(subject);
        System.out.println(signintime);
        List<Result> results = teacherMapper.findStudent(sclass, subject ,signintime);
        for (Result result : results) {
            if(result.getState() == null)
                result.setState("未签到");
        }
        for (Result result : results) {
            System.out.println(result);
        }
        return results;
    }

    @Autowired
    private AdminMapper  adminMapper;
    @PostMapping("/adminupdate")
    public String adminupdate(@RequestParam String id,String raild){
        Integer id1 = Integer.parseInt(id);
        Integer raild1 = Integer.parseInt(raild);
        adminMapper.updateRalid(id1,raild1);
        return "修改成功";
    }
}