package com.p2p.notification.repository;

import com.p2p.notification.entity.NotificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRecordRepository extends JpaRepository<NotificationRecord, UUID> {}
