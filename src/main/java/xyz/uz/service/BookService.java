package xyz.uz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import xyz.uz.domain.dto.BookDTO;
import xyz.uz.domain.query.BookQuery;
import xyz.uz.domain.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.uz.domain.vo.BookVO;

import java.util.List;

/**
* 针对表【book(图书表)】的数据库操作Service
*/
public interface BookService extends IService<Book> {

    /**
     * 书籍分页查询
     */
    IPage<BookVO> pageQuery(BookQuery bookQuery);

    /**
     * 书籍分类查询
     */
    List<String> categoryQuery();

    /**
     * 书籍查询（根据书籍 ID）
     */
    BookVO getInfoById(Long id);

    /**
     * 添加书籍
     */
    void add(BookDTO bookDTO);

    /**
     * 修改书籍
     */
    void modify(BookDTO bookDTO);

    /**
     * 删除书籍（不使用逻辑删除）
     */
    void deleteBatch(List<Long> ids);
}
