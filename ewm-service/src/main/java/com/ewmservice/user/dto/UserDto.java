package com.ewmservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя пользователя не может быть пустым.")
    @Size(min = 2, max = 250)
    private String name;

    @Email(message = "Неверный формат e-mail.")
    @NotEmpty(message = "Адрес электронной почты не может быть пустым.")
    @Size(min = 6, max = 254)
    private String email;
}
