package xyz.uz.aop;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import xyz.uz.anno.CheckRole;
import xyz.uz.enums.UserPermission;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class CheckRoleAspect {

    /**
     * 权限校验 aop 方法
     */
    @Before("@annotation(checkRole)")
    public void checkPermission(CheckRole checkRole){
        UserPermission[] userPermissions = checkRole.value();

        if (userPermissions.length == 0){
            return;
        }

        String[] roleArray = Arrays.stream(userPermissions)
                .map(UserPermission::getRoleName)
                .toArray(String[]::new);

        switch (checkRole.mode()){
            case OR :
                StpUtil.hasPermissionOr(roleArray);
                break;
            case AND:
                StpUtil.hasPermissionAnd(roleArray);
                break;
        }
    }
}
