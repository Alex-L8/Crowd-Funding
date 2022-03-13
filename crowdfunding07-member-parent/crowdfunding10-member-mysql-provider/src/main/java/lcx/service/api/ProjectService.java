package lcx.service.api;

import lcx.entity.vo.DetailProjectVO;
import lcx.entity.vo.PortalTypeVO;
import lcx.entity.vo.ProjectVO;

import java.util.List;

/**
 * Create by LCX on 3/5/2022 9:11 PM
 */
public interface ProjectService {
    void saveProject(ProjectVO projectVO, Integer memberId);

    List<PortalTypeVO> getPortalTypeVO();

    DetailProjectVO getDetailProjectVO(Integer projectId);
}
