package com.tyut.domain;

import java.util.Date;

public class Signinin {
    private Integer id;
    private String name;
    private String sclass;
    private long posttime;
    private long creattime;

    public long getCreattime() {
        return creattime;
    }

    public void setCreattime(long creattime) {
        this.creattime = creattime;
    }

    private String subject;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPosttime() {
        return posttime;
    }

    public void setPosttime(long posttime) {
        this.posttime = posttime;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
