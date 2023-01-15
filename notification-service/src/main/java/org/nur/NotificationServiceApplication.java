package org.nur;

import lombok.extern.slf4j.Slf4j;
import org.nur.event.OrderPlacedEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificatioTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        //now orderPlacedEvent can be saved to db or send notification to user-service
        log.info("Received notification for order - {} ", orderPlacedEvent.getOrderNumber());
    }

}
