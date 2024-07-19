package com.tyut.domain;

public class Result {
    private Integer id;
    private String name;
    private String state;
    private String signintime;

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", signtime='" + signintime + '\'' +
                '}';
    }

    public String getSignintime() {
        return signintime;
    }

    public void setSigntime(String signtime) {
        this.signintime = signtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
