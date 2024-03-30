package com.todo.app.todoapp.repository;

import com.todo.app.todoapp.entities.Todos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodosRepository extends JpaRepository<Todos, Long> {
}
