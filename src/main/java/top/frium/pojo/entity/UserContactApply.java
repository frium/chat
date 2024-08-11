package top.frium.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author frium
 * @since 2024-08-09
 */
@Data
@ApiModel(value = "UserContactApply对象", description = "")
public class UserContactApply implements Serializable {
    @TableId(value = "apply_id", type = IdType.AUTO)
    private Long applyId;

    private String applyUserId;

    private String receiveUserId;

    private Integer contactType;

    private String applyTime;

    private Integer status;

    private String applyInfo;


}
