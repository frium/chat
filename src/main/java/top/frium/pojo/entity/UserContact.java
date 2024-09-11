package top.frium.pojo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class UserContact implements Serializable {
    private String userId;

    private String contactId;

    private String createTime;

    private Integer status;

    private String lastUpdateTime;


}
