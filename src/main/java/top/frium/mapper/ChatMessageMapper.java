package top.frium.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.frium.pojo.entity.ChatMessage;
import top.frium.pojo.vo.ChatListVO;

import java.util.List;

/**
 *
 * @date 2024-09-03 19:48:36
 * @description
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage>{
    List<ChatListVO>getChatList(String userId);
}
