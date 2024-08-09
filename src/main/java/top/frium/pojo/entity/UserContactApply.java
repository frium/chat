package top.frium.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author frium
 * @since 2024-08-09
 */
@Data
@ApiModel(value="UserContactApply对象", description="")
public class UserContactApply implements Serializable {

    private Integer applyId;

    private String applyUserId;

    private String receiveUserId;

    private Integer contactType;

    private String contactId;

    private LocalDateTime lastApplyTime;

    private Integer status;

    private String applyInfo;


}
