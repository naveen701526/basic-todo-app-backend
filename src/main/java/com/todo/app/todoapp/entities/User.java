package com.todo.app.todoapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    private int age;

    public User(String username, String email, String password, int age){
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.todos = new ArrayList<>();
    }

    // One-to-many mapping with Todo
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Todos> todos;


}
