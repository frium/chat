package top.frium.service;


import com.baomidou.mybatisplus.extension.service.IService;
import top.frium.pojo.dto.ApplyAddDTO;
import top.frium.pojo.entity.UserContact;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author frium
 * @since 2024-08-09
 */
public interface UserContactService extends IService<UserContact> {

    void applyAdd(ApplyAddDTO applyAddDTO);
}
