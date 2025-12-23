package xyz.uz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import xyz.uz.domain.dto.UserDTO;
import xyz.uz.domain.dto.UserPasswordUpdateDTO;
import xyz.uz.domain.dto.UserProfileUpdateDTO;
import xyz.uz.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.uz.domain.query.UserQuery;
import xyz.uz.domain.vo.UserLoginVO;
import xyz.uz.domain.vo.UserVO;

/**
* 针对表【user(用户表)】的数据库操作Service
*/

public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    void register(UserDTO userDTO);

    /**
     * 用户登录
     */
    UserLoginVO doLogin(String username, String password);

    /**
     * 用户分页条件查询
     */
    IPage<User> pageQuery(UserQuery userQuery);

    /**
     * 获取用户信息（context）
     */
    UserVO getInfoById(Long id);

    /**
     * 修改用户信息
     */
    void profileUpdate(UserProfileUpdateDTO userProfileUpdateDTO);

    /**
     * 修改用户密码（context）
     */
    void passwordUpdate(UserPasswordUpdateDTO userPasswordUpdateDTO);
}
