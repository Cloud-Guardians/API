package com.cloudians.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudians.domain.user.entity.Notification;
import com.cloudians.domain.user.entity.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    
    Optional<List<Notification>> findByUser(User user);
    Optional<Notification> findByUserAndNotificationType(User userl, String notificationType);
}
