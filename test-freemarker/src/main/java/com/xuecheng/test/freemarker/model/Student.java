package com.xuecheng.test.freemarker.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-13 8:24
 **/
@Data
@ToString
public class Student {
    //姓名
    private String name;
    //年龄
    private int age;
    //生日
    private Date birthday;
    //钱包
    private Float mondy;
    //朋友列表
    private List<Student> friends;
    //最好的朋友
    private Student bestFriend;

}
