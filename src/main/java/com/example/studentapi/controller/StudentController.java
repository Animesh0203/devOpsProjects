package com.example.studentapi.controller;

import java.util.List;
// added no code lmao
// animesh has a huge cock
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.example.studentapi.model.Student;
import com.example.studentapi.repository.StudentRepository;
import com.example.studentapi.service.OllamaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private OllamaService ollamaService;

    //  new student creation
    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        // Input 
        if (student.getName() == null || student.getName().isEmpty() ||
            student.getAge() <= 0 || student.getEmail() == null || student.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Student savedStudent = studentRepository.save(student);
        return ResponseEntity.status(201).body(savedStudent);
    }

    //  get all students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(students);
    }

    // get by id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        Student student = studentRepository.findById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // update by id
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @Valid @RequestBody Student student) {
        if (studentRepository.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        // Input Validation
        if (student.getName() == null || student.getName().isEmpty() ||
            student.getAge() <= 0 || student.getEmail() == null || student.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Student updatedStudent = studentRepository.update(id, student);
        return ResponseEntity.ok(updatedStudent);
    }

    // **Delete a Student by ID** (`DELETE /students/{id}`)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        if (studentRepository.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.delete(id);
        return ResponseEntity.noContent().build();
    }

    // **Generate a Student Summary Using Ollama** (`GET /students/{id}/summary`)
    @GetMapping("/{id}/summary")
    public ResponseEntity<?> getStudentSummary(@PathVariable int id) {
        Student student = studentRepository.findById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        String prompt = String.format(
                "Provide a brief, engaging summary for a student named %s, aged %d, with email %s.",
                student.getName(), student.getAge(), student.getEmail());

        try {
            String summary = ollamaService.generateSummary(prompt);
            return ResponseEntity.ok().body(new SummaryResponse(summary));
        } catch (RestClientException e) {
            return ResponseEntity.status(500).body("Failed to generate summary: " + e.getMessage());
        }
    }

    // Inner class to represent the summary response
    public static class SummaryResponse {
        private String summary;

        public SummaryResponse(String summary) {
            this.summary = summary;
        }

        public String getSummary() {
            return summary;
        }
    }
}
