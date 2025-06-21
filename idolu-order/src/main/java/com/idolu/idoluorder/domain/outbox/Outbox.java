package com.idolu.idoluorder.domain.outbox;

import com.idolu.idoluorder.domain.outbox.type.MessageStatus;
import com.idolu.idoluorder.domain.outbox.type.MessageType;
import com.idolu.idoluorder.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
public class Outbox extends BaseEntity {

    @Id
    private Long outboxId;

    private MessageType type;

    private String topic;

    private String key;

    private String message;

    private MessageStatus statue;

    @Builder
    @PersistenceCreator
    public Outbox(LocalDateTime createdAt, LocalDateTime updatedAt, Long outboxId, MessageType type, String topic, String key, String message, MessageStatus statue) {
        super(createdAt, updatedAt);
        this.outboxId = outboxId;
        this.type = type;
        this.topic = topic;
        this.key = key;
        this.message = message;
        this.statue = statue;
    }
}
