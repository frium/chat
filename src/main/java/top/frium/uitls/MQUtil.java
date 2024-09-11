package top.frium.uitls;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.frium.context.RabbitMQConstant;

import java.io.IOException;
import java.util.*;

/**
 *
 * @date 2024-08-13 17:06:21
 * @description
 */
@Component
public class MQUtil {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("${ecs.exposePath}")
    String exposePath;


    public String sendFileMessage(MultipartFile file) throws IOException {
        String uuid = String.valueOf(UUID.randomUUID());
        String fileSuffix = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = uuid + fileSuffix;
        Map<String, Object> fileData = new HashMap<>();
        fileData.put("fileName", fileName);
        fileData.put("file", Base64.getEncoder().encodeToString(file.getBytes()));
        rabbitTemplate.convertAndSend(RabbitMQConstant.Avatar_QUEUE, fileData);
        return exposePath + fileName;
    }
}
