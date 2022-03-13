package lcx.service.api;

import lcx.entity.vo.AddressVO;
import lcx.entity.vo.OrderProjectVO;
import lcx.entity.vo.OrderVO;

import java.util.List;

/**
 * Create by LCX on 3/11/2022 10:29 AM
 */
public interface OrderService {
    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVOList(Integer memberId);

    void saveAddress(AddressVO addressVO);

    void saveOrder(OrderVO orderVO);
}
