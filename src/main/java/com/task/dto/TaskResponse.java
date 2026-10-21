package com.task.dto;

import com.task.entity.Task;

public record TaskResponse(
        Long id,
        String titulo,
        String descricao,
        com.task.enums.StatusTask status
) {

    public static TaskResponse fromEntity(Task task) {
        return new TaskResponse(
                task.getId().longValue(),
                task.getTitulo(),
                task.getDescricao(),
                task.getStatus()
        );
    }


}
