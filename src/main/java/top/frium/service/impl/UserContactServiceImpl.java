package top.frium.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.frium.mapper.UserContactMapper;
import top.frium.pojo.entity.UserContact;
import top.frium.service.UserContactService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author frium
 * @since 2024-08-09
 */
@Service
public class UserContactServiceImpl extends ServiceImpl<UserContactMapper, UserContact> implements UserContactService {

}
