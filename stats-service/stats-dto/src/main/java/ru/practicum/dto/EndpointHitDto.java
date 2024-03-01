package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {

    private Integer id;

    @NotBlank(message = "Идентификатор сервиса (app) не может быть пустым")
    private String app;

    @NotBlank(message = "URI  не может быть пустым")
    private String uri;

    @NotBlank(message = "IP-адрес пользователя не может быть пустым")
    private String ip;

    @NotNull(message = "Дата и время запроса не могут быть пустыми")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
