package cn.niit.shop_online.vo;

import cn.niit.shop_online.common.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 86187
 * @Date 2023/11/25
 */
@Data
public class LogisticItemVO {
    @Schema(description = "id")
    private String id;
    @Schema(description = "信息文字")
    private String text;
    @Schema(description = "时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime time;
}
