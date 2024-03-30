package com.todo.app.todoapp.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Todos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    public Todos(String description){
        this.description = description;
    }

    // Many-to-one mapping with User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Todos(String description, User user) {
        this.description = description;
        this.user = user;
    }


}
