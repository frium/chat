package top.frium.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.frium.pojo.entity.UserContactApply;
import top.frium.pojo.vo.ApplyVO;

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
public interface UserContactApplyMapper extends BaseMapper<UserContactApply> {
    List<ApplyVO> loadMyApply(String userId);

    List<ApplyVO> loadAddMeApply(String userId);

    void upLoadPersonalId(String userId, String newId);

}
