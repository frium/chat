package top.frium.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @date 2024-06-19 18:42:49
 * @description
 */
@Data
@ApiModel("分页查询")
public class ManagePageDTO {
    @Min(value = 1,message = "最小从第一页展示")
    @ApiModelProperty(value = "页数",required = true)
    Integer page;

    @Min(value = 1,message = "最少每页展示一条数据")
    @ApiModelProperty(value = "每页展示数量",required = true)
    Integer pageSize;

    @NotNull
    @ApiModelProperty("用户名")
    String userId;

    @NotNull
    @ApiModelProperty("邮箱")
    String email;
}
