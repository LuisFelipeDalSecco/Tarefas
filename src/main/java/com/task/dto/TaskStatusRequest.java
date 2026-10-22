package com.task.dto;

import com.task.entity.Task;
import com.task.enums.StatusTask;
import jakarta.validation.constraints.NotNull;

public record TaskStatusRequest(

        @NotNull(message = "O status é obrigatório")
        StatusTask status
) {
}
