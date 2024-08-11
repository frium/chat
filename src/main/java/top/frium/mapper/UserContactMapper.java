package top.frium.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.frium.pojo.entity.UserContact;
import top.frium.pojo.vo.FriendListVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author frium
 * @since 2024-08-09
 */
@Mapper
public interface UserContactMapper extends BaseMapper<UserContact> {
    List<FriendListVO> getFriend(String userId);
    void   upLoadPersonalId(String userId,String newId);
}
