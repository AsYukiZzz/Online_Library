package xyz.uz.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.uz.anno.CheckRole;
import xyz.uz.domain.dto.UserDTO;
import xyz.uz.domain.dto.UserPasswordUpdateDTO;
import xyz.uz.domain.dto.UserProfileUpdateDTO;
import xyz.uz.domain.entity.User;
import xyz.uz.domain.query.UserQuery;
import xyz.uz.domain.vo.UserVO;
import xyz.uz.enums.UserPermission;
import xyz.uz.result.Result;
import xyz.uz.service.UserService;

/**
 * 用户 API
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Object> register(@RequestBody UserDTO userDTO){
        userService.register(userDTO);
        return Result.ok();
    }

    /**
     * 用户登录
     */
    @RequestMapping("/doLogin")
    public Result<Object> doLogin(String username,String password){
        log.info("用户登录，username={}",username);
        return Result.ok(userService.doLogin(username,password));
    }

    /**
     * 用户注销
     */
    @RequestMapping("/logout")
    public Result<Object> logout(){
        log.info("用户id={}注销",StpUtil.getLoginIdAsLong());
        StpUtil.logout();
        return Result.ok();
    }

    /**
     * 用户分页条件查询
     */
    @GetMapping
    @CheckRole(UserPermission.ADMIN)
    public Result<IPage<User>> pageQuery(@RequestBody UserQuery userQuery){
        log.info("用户分页条件查询，操作员id={}", StpUtil.getLoginIdAsLong());
        IPage<User> userIPage = userService.pageQuery(userQuery);
        return Result.ok(userIPage);
    }

    /**
     * 获取用户信息（context）
     */
    @GetMapping("/profile")
    public Result<UserVO> getInfo(){
        Long id = StpUtil.getLoginIdAsLong();
        log.info("用户id={}获取个人信息",id);
        return Result.ok(userService.getInfoById(id));
    }

    /**
     * 修改用户信息（context）
     */
    @PutMapping
    public Result<Object> profileUpdate(@RequestBody UserProfileUpdateDTO userProfileUpdateDTO){
        log.info("用户id={}修改自身用户信息",StpUtil.getLoginIdAsLong());
        userService.profileUpdate(userProfileUpdateDTO);
        return Result.ok();
    }

    /**
     * 修改用户密码（context）
     */
    @PutMapping("/password")
    public Result<Object> passwordUpdate(@RequestBody UserPasswordUpdateDTO userPasswordUpdateDTO){
        log.info("用户id={}修改密码",StpUtil.getLoginIdAsLong());
        userService.passwordUpdate(userPasswordUpdateDTO);
        return Result.ok();
    }
}
