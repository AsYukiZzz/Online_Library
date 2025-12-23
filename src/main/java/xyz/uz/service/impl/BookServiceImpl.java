package xyz.uz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import xyz.uz.domain.dto.BookDTO;
import xyz.uz.domain.query.BookQuery;
import xyz.uz.domain.entity.Book;
import xyz.uz.domain.vo.BookVO;
import xyz.uz.exception.BusinessException;
import xyz.uz.exception.code.BusinessExceptionCode;
import xyz.uz.exception.code.SystemExceptionCode;
import xyz.uz.service.BookService;
import xyz.uz.mapper.BookMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 针对表【book(图书表)】的数据库操作Service实现
*/
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService{

    @Autowired
    private BookMapper bookMapper;

    /**
     * 书籍分页查询
     */
    @Override
    public IPage<BookVO> pageQuery(BookQuery bookQuery) {
        // 配置分页参数 IPage
        IPage<BookVO> infoIPage = new Page<>(bookQuery.getPage(), bookQuery.getSize());

        // 执行分页查询
        return bookMapper.pageQuery(infoIPage, bookQuery);
    }

    /**
     * 书籍分类查询
     */
    @Override
    public List<String> categoryQuery() {
        return bookMapper.categoryQuery();
    }

    /**
     * 书籍查询（根据书籍 ID）
     */
    @Override
    public BookVO getInfoById(Long id) {
        // 根据 id 查询书籍
        Book book = bookMapper.selectById(id);

        // 确保 ID 存在
        if (book == null){
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        // 封装到 BookVO
        BookVO bookVO = new BookVO();
        BeanUtils.copyProperties(book,bookVO);

        return bookVO;
    }

    /**
     * 添加书籍
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(BookDTO bookDTO) {
        // 检测是否已经存在该图书
        Book book = bookMapper.selectOne(new QueryWrapper<Book>().eq("ISBN", bookDTO.getISBN()));
        if (book != null){
            throw new BusinessException(BusinessExceptionCode.BOOK_ALREADY_EXISTS);
        }

        Book newBook = new Book();
        BeanUtils.copyProperties(bookDTO,newBook);

        // 添加书籍
        bookMapper.insert(newBook);
    }

    /**
     * 修改书籍
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(BookDTO bookDTO) {
        // 检测是否存在图书
        Book book = bookMapper.selectById(bookDTO.getId());
        if (book == null){
            throw new BusinessException(BusinessExceptionCode.BOOK_NOT_FOUND);
        }

        BeanUtils.copyProperties(bookDTO,book);

        // MybatisPlus 乐观锁更新
        bookMapper.updateById(book);
    }

    /**
     * 删除书籍（不使用逻辑删除）
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        bookMapper.deleteBatch(ids);
    }
}




