package com.task.controller;

import com.task.dto.TaskRequest;
import com.task.dto.TaskResponse;
import com.task.service.TaskService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tarefas")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponse createTask(@RequestBody TaskRequest taskRequest) {
        return taskService.cadastrarTask(taskRequest);
    }









}
