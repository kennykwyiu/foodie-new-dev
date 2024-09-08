package com.kenny.service;

import com.kenny.pojo.Stu;

public interface StuService {
    public Stu getStuInfo(int id);
    public void saveStu();
    public void updateStu(int id);
    public void deleteStu(int id);

    void saveParent();
    void saveChildren();
    void saveChild1();
    void saveChild2();
}
