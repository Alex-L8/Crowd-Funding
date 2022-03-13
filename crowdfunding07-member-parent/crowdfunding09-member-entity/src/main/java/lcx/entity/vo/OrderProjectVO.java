package lcx.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Create by LCX on 3/11/2022 9:20 AM
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderProjectVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    // 项目名称
    private String projectName;
    // 发起人
    private String launchName;
    // 回报内容
    private String returnContent;
    // 回报数量
    private Integer returnCount;
    // 支持单价
    private Integer supportPrice;
    // 运费，“0”为包邮
    private Integer freight;
    // 订单表的主键
    private Integer orderId;
    // 是否设置单笔限购
    private Integer signalPurchase;
    // 具体限购数量
    private Integer purchase;

}
