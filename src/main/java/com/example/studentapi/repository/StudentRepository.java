package com.example.studentapi.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import com.example.studentapi.model.Student;

@Repository
public class StudentRepository {
    private ConcurrentHashMap<Integer, Student> studentMap = new ConcurrentHashMap<>(); 
    private AtomicInteger idCounter = new AtomicInteger(1);

    public Student save(Student student) {
        int id = idCounter.getAndIncrement();
        student.setId(id);
        studentMap.put(id, student);
        return student;
    }

    public List<Student> findAll() {
        return new ArrayList<>(studentMap.values());
    }

    public Student findById(int id) {
        return studentMap.get(id);
    }

    public Student update(int id, Student student) {
        student.setId(id);
        return studentMap.replace(id, student);
    }

    public Student delete(int id) {
        return studentMap.remove(id);
    }
}
