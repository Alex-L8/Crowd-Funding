package lcx.service;

import lcx.entity.po.AddressPO;
import lcx.entity.po.AddressPOExample;
import lcx.entity.po.OrderPO;
import lcx.entity.po.OrderProjectPO;
import lcx.entity.vo.AddressVO;
import lcx.entity.vo.OrderProjectVO;
import lcx.entity.vo.OrderVO;
import lcx.mapper.AddressPOMapper;
import lcx.mapper.OrderPOMapper;
import lcx.mapper.OrderProjectPOMapper;
import lcx.service.api.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LCX on 3/11/2022 10:28 AM
 */
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;

    @Autowired
    private OrderPOMapper orderPOMapper;

    @Autowired
    private AddressPOMapper addressPOMapper;

    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {

        return orderProjectPOMapper.selectOrderProjectVO(returnId);
    }

    @Override
    public List<AddressVO> getAddressVOList(Integer memberId) {
        AddressPOExample example = new AddressPOExample();
        example.createCriteria().andMemberIdEqualTo(memberId);

        List<AddressPO> addressPOList = addressPOMapper.selectByExample(example);

        List<AddressVO> addressVOList = new ArrayList<>();

        for (AddressPO addressPO : addressPOList) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO,addressVO);
            addressVOList.add(addressVO);
        }
        return addressVOList;

    }

    @Transactional(
            rollbackFor = Exception.class,
            propagation = Propagation.REQUIRES_NEW
    )
    @Override
    public void saveAddress(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO,addressPO);
        addressPOMapper.insert(addressPO);
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class)
    @Override
    public void saveOrder(OrderVO orderVO) {
        // 创建OrderPO对象
        OrderPO orderPO = new OrderPO();
        // 从传入的OrderVO给OrderPO赋值
        BeanUtils.copyProperties(orderVO,orderPO);
        // 将OrderPO存入数据库
        orderPOMapper.insert(orderPO);
        // 得到存入后自增产生的order id
        Integer orderId = orderPO.getId();
        // 得到orderProjectVO
        OrderProjectVO orderProjectVO = orderVO.getOrderProjectVO();
        // 创建OrderProjectPO对象
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        // 赋值
        BeanUtils.copyProperties(orderProjectVO,orderProjectPO);
        // 给orderProjectPO设置orderId
        orderProjectPO.setOrderId(orderId);
        // 存入数据库
        orderProjectPOMapper.insert(orderProjectPO);
    }
}
