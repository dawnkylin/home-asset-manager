package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.model.User;
import cn.shadowkylin.ham.service.AccountService;
import cn.shadowkylin.ham.service.HomeRequestService;
import cn.shadowkylin.ham.service.HomeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */

@RestController
@RequestMapping("/account")
public class AccountController {
    @Resource
    private AccountService accountService;
    @Resource
    private HomeService homeService;
    @Resource
    private HomeRequestService homeRequestService;
    /**
     * 获取账户列表
     */
    @GetMapping("/getAccountList")
    public ResultUtil<Object> getAccountList() {
        //调用service层的方法，获取账户列表
        List<User> accountList = accountService.getAccountList();
        return ResultUtil.success("获取账户列表成功", accountList);
    }

    /**
     * 通过ID获取账户详情
     * @param id
     */
    @GetMapping("/getAccountDetail/{id}")
    public ResultUtil<Object> getAccountDetail(@PathVariable("id") int id) {
        //调用service层的方法，获取账户详情
        User accountDetail = accountService.getAccountDetail(id);
        return ResultUtil.success("获取账户详情成功", accountDetail);
    }

    /**
     * 根据家庭序列号获取账户列表
     * @param homeSerialNumber
     */
    @GetMapping("/getAccountsByHSN/{homeSerialNumber}")
    public ResultUtil<Object> getAccountsByHSN(@PathVariable("homeSerialNumber") String homeSerialNumber) {
        //查看home表中是否存在该家庭序列号
        if (!homeService.isHomeExist(homeSerialNumber)) {
            return ResultUtil.error("该家庭序列号不存在！");
        }
        //调用service层的方法，获取账户列表
        List<User> accountList = accountService.getAccountsByHSN(homeSerialNumber);
        return ResultUtil.success("获取账户列表成功", accountList);
    }

    /**
     * 添加账户
     * @param user
     */
    @PostMapping("/addAccount")
    public ResultUtil<Object> addAccount(User user) {
        accountService.addAccount(user);
        return ResultUtil.success("添加成功！");
    }

    /**
     * 修改账户
     * @param user
     */
    @PostMapping("/updateAccount")
    public ResultUtil<Object> updateAccount(User user) {
        accountService.updateAccount(user);
        return ResultUtil.success("修改成功！");
    }

    /**
     * 删除账户 慎重使用
     * @param id
     */
    @PostMapping("/deleteAccount")
    public ResultUtil<Object> deleteAccount(int id) {
        accountService.deleteAccount(id);
        return ResultUtil.success("删除成功！");
    }

    /**
     * 批量删除账户 慎重使用
     * @param ids
     */
    @PostMapping("/deleteAccountList")
    public ResultUtil<Object> deleteAccountList(@RequestBody int[] ids) {
        accountService.deleteAccountList(ids);
        return ResultUtil.success("删除成功！");
    }

    /**
     * 将用户从家庭中移除
     */
    @PostMapping("/removeUserFromHome/{requestId}")
    public ResultUtil<Object> removeUserFromHome(@PathVariable("requestId") int requestId,int removeId) {
        //获取请求者的家庭序列号，根据该序列号获取其创建者的id，判断请求者是否为家庭创建者，若不是，则无权移除
        String homeSerialNumber = homeService.getHSNByUserId(requestId);
        int creatorId = homeService.getCreatorIdByHSN(homeSerialNumber);
        if (creatorId != requestId) {
            return ResultUtil.error("您不是家庭创建者，无权移除！");
        }
        //获取被移除者的家庭序列号，判断该序列号是否与请求者的家庭序列号相同，若不同，则无权移除
        String removeHomeSerialNumber = homeService.getHSNByUserId(removeId);
        if (!homeSerialNumber.equals(removeHomeSerialNumber)) {
            return ResultUtil.error("该用户不在您的家庭中，无法移除！");
        }
        //调用service层的方法，将用户从家庭中移除，即将其家庭序列号置空
        accountService.removeUserFromHome(removeId);
        return ResultUtil.success("移除成功！");
    }

    /**
     * 更新用户的家庭序列号，即将用户加入家庭
     */
    @PostMapping("/updateUserHSN/{userId}")
    public ResultUtil<Object> updateUserHSN(@PathVariable("userId") int userId,String homeSerialNumber) {
        //查看home表中是否存在该家庭序列号，以及用户是否已经加入家庭
        if (!homeService.isHomeExist(homeSerialNumber)) {
            return ResultUtil.error("该家庭序列号不存在！");
        }
        if (accountService.userHasJoinedHome(userId)) {
            return ResultUtil.error("该用户已加入家庭！");
        }
        //调用service层的方法，更新用户的家庭序列号
        accountService.updateUserHSN(userId,homeSerialNumber);
        return ResultUtil.success("更新成功！");
    }

    /**
     * 解散家庭
     */
    @PostMapping("/disbandHome/{userId}")
    public ResultUtil<Object> disbandHome(@PathVariable("userId") int userId) {
        //获取用户的家庭序列号，根据该序列号获取其创建者的id，判断请求者是否为家庭创建者，若不是，则无权解散
        String homeSerialNumber = homeService.getHSNByUserId(userId);
        int creatorId = homeService.getCreatorIdByHSN(homeSerialNumber);
        if (creatorId != userId) {
            return ResultUtil.error("您不是家庭创建者，无权解散！");
        }
        //调用service层的方法，解散家庭
        accountService.disbandHome(homeSerialNumber);
        return ResultUtil.success("解散成功！");
    }
}
