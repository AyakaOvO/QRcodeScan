package com.tyut.domain;

public class Student {
    private Integer id;
    private String name;
    private String password;
    private String sclass;
    private Integer ralid;
    private String state;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", sclass='" + sclass + '\'' +
                ", ralid=" + ralid +
                ", state='" + state + '\'' +
                '}';
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public Integer getRalid() {
        return ralid;
    }

    public void setRalid(Integer ralid) {
        this.ralid = ralid;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
