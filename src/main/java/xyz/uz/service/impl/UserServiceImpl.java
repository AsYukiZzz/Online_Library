package xyz.uz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.uz.domain.dto.UserDTO;
import xyz.uz.domain.dto.UserPasswordUpdateDTO;
import xyz.uz.domain.dto.UserProfileUpdateDTO;
import xyz.uz.domain.entity.Book;
import xyz.uz.domain.entity.User;
import xyz.uz.domain.query.UserQuery;
import xyz.uz.domain.vo.UserLoginVO;
import xyz.uz.domain.vo.UserVO;
import xyz.uz.enums.UserPermission;
import xyz.uz.exception.BusinessException;
import xyz.uz.exception.code.AuthExceptionCode;
import xyz.uz.exception.code.BusinessExceptionCode;
import xyz.uz.exception.code.SystemExceptionCode;
import xyz.uz.mapper.UserMapper;
import xyz.uz.service.UserService;

/**
 * 针对表【user(用户表)】的数据库操作Service实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserDTO userDTO) {
        // 查询是否已存在该用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", userDTO.getUsername()));
        if (user != null) {
            throw new BusinessException(BusinessExceptionCode.USER_ALREADY_EXISTS);
        }

        // 属性复制
        User newUser = new User();
        BeanUtils.copyProperties(userDTO, newUser);

        // 对密码进行 BCrypt 加密
        newUser.setPassword(BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt()));
        newUser.setPermission(UserPermission.USER);

        // 关键属性赋值
        newUser.setIsOverdue(0);
        newUser.setPermission(UserPermission.USER);

        // 存储到数据库中
        userMapper.insert(newUser);
    }

    /**
     * 用户登录
     */
    @Override
    public UserLoginVO doLogin(String username, String password) {
        // 查询用户（名）是否存在
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new BusinessException(AuthExceptionCode.USER_NOT_FOUND);
        }

        // 验证密码
        if (BCrypt.checkpw(password,user.getPassword())) {
            // 密码匹配则登录成功
            StpUtil.login(
                    user.getId(),
                    new SaLoginParameter()
                            .setExtra("username",user.getUsername())
                            .setExtra("role",user.getPermission())
            );
        } else {
            throw new BusinessException(AuthExceptionCode.INVALID_PASSWORD);
        }

        // 构造登录后返回的用户信息
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user,userLoginVO);

        return userLoginVO;
    }

    /**
     * 用户分页条件查询
     */
    @Override
    public IPage<User> pageQuery(UserQuery userQuery) {
        // 配置分页参数 IPage
        IPage<Book> infoIPage = new Page<>(userQuery.getPage(), userQuery.getSize());

        // 执行分页查询
        return userMapper.pageQuery(infoIPage, userQuery);
    }

    /**
     * 获取用户信息（context）
     */
    @Override
    public UserVO getInfoById(Long id) {
        User user = userMapper.selectById(id);

        if (user == null){
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);

        return userVO;
    }

    /**
     * 修改用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void profileUpdate(UserProfileUpdateDTO userProfileUpdateDTO) {
        // 1. 获取当前用户
        User user = userMapper.selectById(StpUtil.getLoginIdAsLong());

        if (user == null){
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        BeanUtils.copyProperties(userProfileUpdateDTO,user);

        // 执行修改
        int updateRows = userMapper.updateById(user);
        if (updateRows == 0){
            throw new BusinessException(SystemExceptionCode.DATA_EXPIRED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void passwordUpdate(UserPasswordUpdateDTO userPasswordUpdateDTO) {
        // 1. 获取当前用户
        User user = userMapper.selectById(StpUtil.getLoginIdAsLong());

        if (user == null){
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        // 2. 校验旧密码是否有效
        if (!BCrypt.checkpw(userPasswordUpdateDTO.getOldPassword(),user.getPassword())){
            throw new BusinessException(AuthExceptionCode.INVALID_PASSWORD);
        }

        // 3. 加密新密码并设置
        user.setPassword(BCrypt.hashpw(userPasswordUpdateDTO.getNewPassword(), BCrypt.gensalt()));

        // 4. 持久化新密码
        int updateRows = userMapper.updateById(user);
        if (updateRows == 0){
            throw new BusinessException(SystemExceptionCode.DATA_EXPIRED);
        }

        // 5. 强制所有设备下线
        StpUtil.logout();
    }
}




