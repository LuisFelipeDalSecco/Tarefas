package com.task.service;

import com.task.dto.TaskRequest;
import com.task.dto.TaskResponse;
import com.task.entity.Task;
import com.task.repository.TaskRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
public class TaskService {

    public final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse cadastrarTask(TaskRequest taskRequest) {
        if (taskRequest.titulo() == null || taskRequest.titulo().isEmpty()) {
            throw new RuntimeException("O título da tarefa não pode ser nulo ou vazio.");
        }

        if (taskRequest.titulo().length() > 100) {
            throw new RuntimeException("O título da tarefa não pode ter mais de 100 caracteres.");
        }

        Task task = taskRequest.toEntity();
        Task taskSalvo = taskRepository.save(task);
        return TaskResponse.fromEntity(taskSalvo);
    }

    public TaskResponse atualizarTask(Integer id, TaskRequest taskRequest) {
        TaskResponse task = buscarTaskPorId(id);
        taskRequest.preencher(taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com o ID: " + id)));
        Task taskAtualizado = taskRepository.save(taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com o ID: " + id)));
        return TaskResponse.fromEntity(taskAtualizado);
    }

    public TaskResponse buscarTaskPorId(Integer id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com o ID: " + id));
        return TaskResponse.fromEntity(task);
    }

    public TaskResponse listarTask(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .stream()
                .map(TaskResponse::fromEntity)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhuma tarefa encontrada."));
    }

    public TaskResponse deletarTask(Integer id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com o ID: " + id));
        taskRepository.delete(task);
        return TaskResponse.fromEntity(task);
    }
}
