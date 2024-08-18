package com.cloudians.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudians.domain.user.entity.Notification;

@Repository
public interface FcmNotificationRepository extends JpaRepository<Notification,Long> {

}
