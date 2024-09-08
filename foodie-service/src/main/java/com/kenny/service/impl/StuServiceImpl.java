package com.kenny.service.impl;

import com.kenny.mapper.StuMapper;
import com.kenny.pojo.Stu;
import com.kenny.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StuServiceImpl implements StuService {

    @Autowired
    private StuMapper stuMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Stu getStuInfo(int id) {
        return stuMapper.selectByPrimaryKey(id);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveStu() {
        Stu stu = new Stu();
        stu.setName("jack");
        stu.setAge(22);
        stuMapper.insert(stu);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateStu(int id) {
        Stu stu = new Stu();
        stu.setId(id);
        stu.setName("lucy");
        stu.setAge(24);
        stuMapper.updateByPrimaryKey(stu);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteStu(int id) {
        stuMapper.deleteByPrimaryKey(id);
    }

    public void saveParent() {
        Stu stu = new Stu();
        stu.setName("parent");
        stu.setAge(19);
        stuMapper.insert(stu);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void saveChildren() {
        saveChild1();
        int a = 1 / 0;
        saveChild2();
    }
    public void saveChild1() {
        Stu stu = new Stu();
        stu.setName("child-1");
        stu.setAge(22);
        stuMapper.insert(stu);

    }
    public void saveChild2() {
        Stu stu = new Stu();
        stu.setName("child-2");
        stu.setAge(11);
        stuMapper.insert(stu);
    }


}
