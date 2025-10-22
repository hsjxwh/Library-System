package org.powernode.springboot.bean.mysql;

public class Manager {
    long id;
    String name;
    //电话号码
    String num;
    String password;

    public Manager(long id, String num, String name,String password) {
        this.id = id;
        this.num = num;
        this.name = name;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Manager() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
