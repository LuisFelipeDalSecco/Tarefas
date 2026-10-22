package com.task.service;

import com.task.dto.TaskRequest;
import com.task.dto.TaskResponse;
import com.task.entity.Task;
import com.task.enums.StatusTask;
import com.task.exception.RegrasException;
import com.task.repository.TaskRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public TaskResponse cadastrarTask(TaskRequest taskRequest) {
        if (taskRequest.titulo() == null || taskRequest.titulo().isEmpty()) {
            throw new RegrasException("O título da tarefa não pode ser nulo ou vazio.");
        }

        if (taskRequest.titulo().length() > 100) {
            throw new RegrasException("O título da tarefa não pode ter mais de 100 caracteres.");
        }

        Task task = taskRequest.toEntity();
        Task taskSalvo = taskRepository.save(task);
        return TaskResponse.fromEntity(taskSalvo);
    }

    @Transactional
    public TaskResponse atualizarTask(Long id, TaskRequest taskRequest) {

        // 1. Busca a tarefa existente no banco
        Task task = taskRepository.findById(id).
                orElseThrow(() -> new RegrasException("Tarefa não encontrada com o ID: " + id));

        taskRequest.preencher(task);
        Task taskAtualizada = taskRepository.save(task);
        return TaskResponse.fromEntity(taskAtualizada);
    }

    @Transactional
    public TaskResponse atualizarStatusTask(Long id, StatusTask novoStatus) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new RegrasException("Tarefa não encontrada com o ID: " + id)
                );

        task.setStatus(novoStatus);
        Task taskAtualizada = taskRepository.save(task);
        return TaskResponse.fromEntity(taskAtualizada);
    }

    @Transactional(readOnly = true)
    public TaskResponse buscarTaskPorId(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RegrasException("Tarefa não encontrada com o ID: " + id));
        return TaskResponse.fromEntity(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> listarTask() {
        return taskRepository.findAll()
                .stream()
                .map(TaskResponse::fromEntity)
                .toList();
    }


    @Transactional
    public TaskResponse deletarTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RegrasException("Tarefa não encontrada com o ID: " + id));
        taskRepository.delete(task);
        return TaskResponse.fromEntity(task);
    }

}
