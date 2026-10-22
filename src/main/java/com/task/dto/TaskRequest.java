package com.task.dto;

import com.task.entity.Task;
import com.task.enums.StatusTask;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.RequestBody;

public record TaskRequest(

        @NotBlank(message = "Atenção! É obrigatório informar um título na tarefa!")
        @Size(max = 150, message = "Atenção! O Título deve ter até no máximo 100 caracteres")
        String titulo,

        @Size(max = 150, message = "Atenção! A Descrição deve ter até no máximo 500 caracteres")
        String descricao,

        StatusTask status
) {

    public Task toEntity() {
        Task task = new Task();
        preencher(task);
        return task;
    }

    public void preencher(Task task) {
        task.setTitulo(titulo);
        task.setDescricao(descricao);

        if (status != null) {
            task.setStatus(status);
        }
    }
}
