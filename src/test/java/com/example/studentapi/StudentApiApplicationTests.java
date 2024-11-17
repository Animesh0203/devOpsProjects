package com.example.studentapi;

import com.example.studentapi.controller.StudentController;
import com.example.studentapi.model.Student;
import com.example.studentapi.repository.StudentRepository;
import com.example.studentapi.service.OllamaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private OllamaService ollamaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        student1 = new Student(1, "John Doe", 20, "john@example.com");
        student2 = new Student(2, "Jane Doe", 22, "jane@example.com");
    }

    @Test
    void testCreateStudent() throws Exception {
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        ResultActions response = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student1)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(20))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testGetAllStudents() throws Exception {
        List<Student> students = Arrays.asList(student1, student2);
        when(studentRepository.findAll()).thenReturn(students);

        ResultActions response = mockMvc.perform(get("/students")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById_ValidId() throws Exception {
        when(studentRepository.findById(1)).thenReturn(student1);

        ResultActions response = mockMvc.perform(get("/students/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(20));

        verify(studentRepository, times(1)).findById(1);
    }

    @Test
    void testGetStudentById_InvalidId() throws Exception {
        when(studentRepository.findById(3)).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/students/3")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound());

        verify(studentRepository, times(1)).findById(3);
    }

    @Test
    void testUpdateStudent() throws Exception {
        Student updatedStudent = new Student(1, "John Smith", 21, "john.smith@example.com");
        when(studentRepository.findById(1)).thenReturn(student1);
        when(studentRepository.update(1, updatedStudent)).thenReturn(updatedStudent);

        ResultActions response = mockMvc.perform(put("/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(21));

        verify(studentRepository, times(1)).update(1, updatedStudent);
    }

    @Test
    void testDeleteStudent_ValidId() throws Exception {
        when(studentRepository.findById(1)).thenReturn(student1);
        doNothing().when(studentRepository).delete(1);

        ResultActions response = mockMvc.perform(delete("/students/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNoContent());

        verify(studentRepository, times(1)).delete(1);
    }

    @Test
    void testDeleteStudent_InvalidId() throws Exception {
        when(studentRepository.findById(3)).thenReturn(null);

        ResultActions response = mockMvc.perform(delete("/students/3")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound());

        verify(studentRepository, times(1)).findById(3);
    }
}
