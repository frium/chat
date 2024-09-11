package top.frium.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ChatMessage {
    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;
    private Integer messageType;
    private String messageContent;
    private String sendUserId;
    private String receiveUserId;
    private String sendTime;
    private String fileName;
    private String fileAddress;
    private String fileType;
    private Integer status;
}