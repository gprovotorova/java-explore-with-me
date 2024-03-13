package com.ewmservice.request.dto;

import com.ewmservice.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    @NotNull(message = "Список заявок не может быть пустым")
    private List<Long> requestIds;

    @NotNull(message = "Статус не может быть пустым")
    private RequestStatus status;
}
