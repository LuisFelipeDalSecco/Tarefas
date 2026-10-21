package com.task.dto;

import com.task.entity.Task;

public record TaskRequest(
        String titulo,
        String descricao,
        String status
) {

    public Task toEntity() {
        Task task = new Task();
        preencher(task);
        return task;
    }

    public void preencher(Task task) {
        task.setTitulo(titulo);
        task.setDescricao(descricao);
        task.setStatus(status);
    }
}
