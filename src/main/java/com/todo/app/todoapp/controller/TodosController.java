package com.todo.app.todoapp.controller;

import com.todo.app.todoapp.dto.DeleteResponse;
import com.todo.app.todoapp.dto.TodosRequest;
import com.todo.app.todoapp.dto.TodosResponse;
import com.todo.app.todoapp.dto.TodosResponseInitial;
import com.todo.app.todoapp.entities.Todos;
import com.todo.app.todoapp.entities.User;
import com.todo.app.todoapp.repository.TodosRepository;
import com.todo.app.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TodosController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodosRepository todosRepository;

    @GetMapping("/todos")
    public ResponseEntity<List<TodosResponseInitial>> getTodos(Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        List<TodosResponseInitial> todosOfUser = user.getTodos().stream().map(todo -> new TodosResponseInitial(todo.getId(), todo.getDescription())).toList();
        return new ResponseEntity<>
                (todosOfUser, HttpStatus.OK);
    }

    @PostMapping("/todos/new")
    public ResponseEntity<TodosResponse> addTodos(@RequestBody TodosRequest todosRequest,Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        Todos todos = new Todos(todosRequest.getDescription(), user);
        List<Todos> updatedTodos = user.getTodos();
        updatedTodos.add(todos);
        user.setTodos(updatedTodos);
        userRepository.save(user);

        return new ResponseEntity<>(new TodosResponse(todos.getDescription()), HttpStatus.CREATED);
    }


    @PutMapping("/todos/update/{id}")
    public ResponseEntity<TodosResponseInitial> updateTodos(@PathVariable long id, @RequestBody TodosRequest todosRequest) throws Exception {
        Optional<Todos> oldTodo = todosRepository.findById(id);
        Todos newTodo = oldTodo.map(todo -> todo).orElseThrow(() -> new Exception("todo does not exist"));
        newTodo.setDescription(todosRequest.getDescription());
        User user = userRepository.findById(newTodo.getUser().getId()).map(user1->user1).orElseThrow(()->new Exception("user not found"));
        List<Todos>  updatedTodos = user.getTodos().stream().map(todo->{
            if(todo.getId()==id)
            {
                return newTodo;
            }
            return todo;
        }).collect(Collectors.toList());

        user.setTodos(updatedTodos);
        userRepository.save(user);
        return new ResponseEntity<>(new TodosResponseInitial(id, newTodo.getDescription()), HttpStatus.OK);

    }

    @DeleteMapping("/todos/delete/{id}")
    public ResponseEntity<DeleteResponse> deleteTodos(@PathVariable long id, Principal principal) throws Exception {

        User user = userRepository.findByUsername(principal.getName());
        user.setTodos(user.getTodos().stream().filter(use-> use.getId() != id).collect(Collectors.toList()));

        userRepository.save(user);
        todosRepository.deleteById(id);
        return new ResponseEntity<>(new DeleteResponse(id, "Todo with id "+String.valueOf(id)+ " has been deleted successfully"), HttpStatus.OK);
    }

}
