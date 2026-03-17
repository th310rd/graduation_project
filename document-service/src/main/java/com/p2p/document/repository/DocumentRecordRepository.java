package com.p2p.document.repository;

import com.p2p.document.entity.DocumentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRecordRepository extends JpaRepository<DocumentRecord, UUID> {}
