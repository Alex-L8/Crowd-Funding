package lcx.entity.po;

public class MemberConfirmInfoPO {
    private Integer id;

    private Integer memberid;

    private String paynum;

    private String cardnum;

    public MemberConfirmInfoPO() {
    }

    public MemberConfirmInfoPO(Integer id, Integer memberid, String paynum, String cardnum) {
        this.id = id;
        this.memberid = memberid;
        this.paynum = paynum;
        this.cardnum = cardnum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public String getPaynum() {
        return paynum;
    }

    public void setPaynum(String paynum) {
        this.paynum = paynum == null ? null : paynum.trim();
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum == null ? null : cardnum.trim();
    }

    @Override
    public String toString() {
        return "MemberConfirmInfoPO{" +
                "id=" + id +
                ", memberid=" + memberid +
                ", paynum='" + paynum + '\'' +
                ", cardnum='" + cardnum + '\'' +
                '}';
    }
}