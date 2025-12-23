package xyz.uz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import xyz.uz.domain.entity.Book;
import xyz.uz.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.uz.domain.query.UserQuery;

/**
* 针对表【user(用户表)】的数据库操作Mapper
*/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 用户分页条件查询
     */
    IPage<User> pageQuery(IPage<Book> infoIPage, UserQuery userQuery);
}




