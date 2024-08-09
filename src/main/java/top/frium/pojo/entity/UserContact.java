package top.frium.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="UserContact对象", description="")
public class UserContact implements Serializable {

    private String userId;

    private String contactId;

    @ApiModelProperty("0好友类型 1群类型")
    private Integer contactType;

    private String createTime;

    private Integer status;

    private String lastUpdateTime;


}
