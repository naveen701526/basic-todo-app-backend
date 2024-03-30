package com.todo.app.todoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



@Data
@NoArgsConstructor
@ToString
public class TodosResponse {


    private String description;

    public TodosResponse(String description){

        this.description = description;
    }

}
