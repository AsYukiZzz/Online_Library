package xyz.uz.config.satoken;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.uz.domain.entity.User;
import xyz.uz.exception.BusinessException;
import xyz.uz.exception.code.AuthExceptionCode;
import xyz.uz.mapper.UserMapper;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 自定义权限加载接口实现类
 */

@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取账号拥有的权限码
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return Collections.emptyList();
    }

    /**
     * 获取账号拥有的角色标识
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        User user = getUserById(loginId);
        return List.of(user.getPermission().getRoleName());
    }

    /**
     * 根据 Id 获取 User
     */
    private User getUserById(Object loginId){
        User user = userMapper.selectById((Serializable) loginId);
        if (user == null){
            throw new BusinessException(AuthExceptionCode.USER_NOT_FOUND);
        }
        return user;
    }
}

