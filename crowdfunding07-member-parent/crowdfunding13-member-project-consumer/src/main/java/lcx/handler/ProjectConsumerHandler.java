package lcx.handler;

import lcx.api.MySQLRemoteService;
import lcx.config.OSSProperties;
import lcx.constant.CrowdConstant;
import lcx.entity.vo.*;
import lcx.util.CrowdUtil;
import lcx.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 存在的问题：用户上传详情图片时，可能因为网络原因，只要有一个没有上传成功，就不会存入数据库，
 * 但是OSS存入了其它上传成功的图片，这些图片的链接没有被存入数据库，所以无法访问，需要隔一段时间清除，
 * 这使用户的体验也很不好，需要全部重新提交
 * 原因：目前没有分布式事务的支持
 * 解决办法：采用分布式事务的支持,某一个图片上传失败，不影响其它图片的上传，将上传失败的那个图片信息告诉用户，
 * 让用户重新上传这个失败的图片即可
 *
 * Create by LCX on 3/6/2022 11:00 AM
 */
@Controller
public class ProjectConsumerHandler {

    @Autowired
    private OSSProperties ossProperties;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(
            // 接收图片外的普通数据
            ProjectVO projectVO,
            // 接收头图
            MultipartFile headerPicture,
            // 接收详情图片
            List<MultipartFile> detailPictureList,
            // 用来将收集了一部分数据的ProjectVO对象存入Session域中
            HttpSession session,
            Model model
            ) throws IOException {

        // 一.完成头图的上传

        boolean headerPictureIsEmpty = headerPicture.isEmpty();

        if (headerPictureIsEmpty) {
            // 1.如果头图为空，直接返回到表单页面并显示错误信息
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }

        // 2.如果用户确实上传了头图，则执行上传
        ResultEntity<String> uploadHeaderPicResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename());
        String result = uploadHeaderPicResultEntity.getResult();
        // 3.判断头图是否上传成功
        if (ResultEntity.SUCCESS.equals(result)) {
            // 4.从返回的数据中获取图片的访问路径
            String headerPicturePath = uploadHeaderPicResultEntity.getData();

            // 5.存入ProjectVO对象中
            projectVO.setHeaderPicturePath(headerPicturePath);
        }else {
            // 6.如果上传失败则返回到表单页面并显示错误提示
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
            return "project-launch";
        }

        // 二、上传详情图片
        // 1.创建一个用来存放详情图片路径的集合
        List<String> detailPicturePathList = new ArrayList<>();

        // 2.检查detailPictureList是否有效
        if (CollectionUtils.isEmpty(detailPictureList)) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
            return "project-launch";
        }

        // 3.遍历detailPictureList集合
        for (MultipartFile detailPicture : detailPictureList) {

            // 4.判断当前detailPicture是否为内存为0的图片
            if (detailPicture.isEmpty()) {
                model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);
                return "project-launch";
            }
            // 5.执行上传
            ResultEntity<String> detailUploadResultEntity = CrowdUtil.uploadFileToOss(
                    ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    detailPicture.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    detailPicture.getOriginalFilename());

            // 6.检查上传结果
            String detailUploadResult = detailUploadResultEntity.getResult();

            if (ResultEntity.SUCCESS.equals(detailUploadResult)) {

                String detailPicturePath = detailUploadResultEntity.getData();

                // 7.收集刚刚上传的图片的访问路径
                detailPicturePathList.add(detailPicturePath);
            } else {
                // 8.如果上传失败则返回到表单页面并显示错误提示
                model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
                return "project-launch";
            }
        }
        // 9.将存放了详情图片访问路径的集合存入ProjectVO中
        projectVO.setDetailPicturePathList(detailPicturePathList);

        // 三、后续操作
        // 10.将ProjectVO对象存入Session中
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT,projectVO);

        return "redirect:http://localhost/project/return/info/page";
    }

    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(MultipartFile returnPicture) throws IOException {

        // 1.执行上传文件
        ResultEntity<String> uploadReturnPicResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                returnPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                returnPicture.getOriginalFilename());

        // 2.返回上传的结果
        return uploadReturnPicResultEntity;
    }

    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> saveReturn(ReturnVO returnVO,HttpSession session){

        try {
            // 1.从session域中读取之前缓存的ProjectVO对象
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

            // 2.判断projectVO是否为空
            if (projectVO == null) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
            }

            // 3.从ProjectVO对象中获取存储回报信息的集合，
            // 可以避免第二次执行该请求时又创建了一个新的集合，而无法将数据集中保存
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();

            // 4.判断returnVOList是否有效
            if (returnVOList == null || returnVOList.size() == 0) {
                // 5.从ProjectVO中获取的returnVOList无效则对其初始化
                returnVOList = new ArrayList<>();
                // 6.为了让以后能够正常使用这个集合，设置到projectVO对象中
                projectVO.setReturnVOList(returnVOList);
            }

            // 7.将收集了表单数据的ReturnVO存入集合中
            returnVOList.add(returnVO);

            // 8.把数据有变化的ProjectVO对象重新存入Session域，以确保新的数据最终能够存入Redis中

            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT,projectVO);
            
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }

    @RequestMapping("/create/confirm")
    public String saveConfirmInfo(Model model,HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO){

        // 1.从session域中读取之前临时存储的ProjectVO对象
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

        // 2.判断projectVO是否为空
        if (projectVO == null) {
            throw  new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
        }

        // 3.将确认信息数据设置到ProjectVO对象中
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);

        // 4.从session域中读取当前登录的用户
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVO.getId();

        // 5.调用远程方法保存projectVO对象
        ResultEntity<String> saveResultEntity = mySQLRemoteService.saveProjectVORemote(projectVO,memberId);

        // 6.判断远程的保存操作是否成功
        String result = saveResultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveResultEntity.getMessage());
            return "project-confirm";
        }

        // 7.远程保存成功则将session域中的临时projectVO对象移除
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

        return "redirect:http://localhost/project/create/success";
    }

    @RequestMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId") Integer projectId, Model model){
        ResultEntity<DetailProjectVO> resultEntity = mySQLRemoteService.getDetailProjectVORemote(projectId);
        String result = resultEntity.getResult();

        if (ResultEntity.SUCCESS.equals(result)) {
            DetailProjectVO detailProjectVO = resultEntity.getData();
            model.addAttribute(CrowdConstant.DETAIL_PROJECT_VO, detailProjectVO);
        }
        return "project-show-detail";
    }

}
