package com.ezmeal.product.infrastruture.message.kafka.producer;

import com.ezmeal.common.security.principal.CustomUserPrincipal;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventSender {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Object payload) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, payload);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()
                && auth.getPrincipal() instanceof CustomUserPrincipal principal) {
            record.headers().add("X-User-Id", principal.getUserId().getBytes(StandardCharsets.UTF_8));
            record.headers().add("X-User-Roles", principal.getRole().name().getBytes(StandardCharsets.UTF_8));

            String email = principal.getEmail() != null ? principal.getEmail() : "";
            record.headers().add("X-User-Email", email.getBytes(StandardCharsets.UTF_8));
        }

        kafkaTemplate.send(record)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Kafka 이벤트 발행 실패. topic={}", topic, ex);
                        return;
                    }

                    log.debug("Kafka 이벤트 발행 성공. topic={}, partition={}, offset={}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                });
    }
}
