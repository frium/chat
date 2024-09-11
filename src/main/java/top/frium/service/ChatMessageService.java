package top.frium.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.frium.pojo.dto.MessageDTO;
import top.frium.pojo.entity.ChatMessage;

/**
 *
 * @date 2024-09-03 19:51:31
 * @description
 */
public interface ChatMessageService extends IService<ChatMessage> {
    void readMessage(String receiveUserId);

    void sendMessage(MessageDTO messageDTO);
}
