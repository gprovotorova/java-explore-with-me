package com.ewmservice.request.repository;

import com.ewmservice.request.model.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Optional<ParticipationRequest> findByIdAndRequesterId(Long requestId, Long userId);

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    Boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findByEventIdAndRequesterId(Long eventId, Long userId);

    List<ParticipationRequest> findAllByIdIn(List<Long> ids);

    List<ParticipationRequest> findAllByEventId(Long eventId);
}
