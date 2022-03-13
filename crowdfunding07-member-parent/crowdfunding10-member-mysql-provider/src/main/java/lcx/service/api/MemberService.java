package lcx.service.api;

import lcx.entity.po.MemberPO;

/**
 * Create by LCX on 2/25/2022 12:44 AM
 */
public interface MemberService {
    MemberPO getMemberPOByLoginAcct(String loginacct);

    void saveMember(MemberPO memberPO);
}
