package xyz.uz.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.uz.anno.CheckRole;
import xyz.uz.domain.dto.BookDTO;
import xyz.uz.domain.query.BookQuery;
import xyz.uz.domain.vo.BookVO;
import xyz.uz.enums.UserPermission;
import xyz.uz.result.Result;
import xyz.uz.service.BookService;

import java.util.List;

/**
 * 书籍 API
 */

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 书籍分页查询
     */
    @GetMapping("/list")
    public Result<IPage<BookVO>> pageQuery(BookQuery bookQuery){
        log.info("书籍分页查询，查询者id={}", StpUtil.getLoginIdAsLong());
        IPage<BookVO> bookIPage = bookService.pageQuery(bookQuery);
        return Result.ok(bookIPage);
    }

    /**
     * 书籍分类查询
     */
    @GetMapping("/category/list")
    public Result<List<String>> categoryQuery(){
        log.info("书籍分类查询，查询者id={}",StpUtil.getLoginIdAsLong());
        return Result.ok(bookService.categoryQuery());
    }

    /**
     * 书籍查询（根据书籍 ID）
     */
    @GetMapping("/{id}")
    public Result<BookVO> getInfoById(@PathVariable Long id){
        log.info("获取书籍详情信息，查询者id={}",StpUtil.getLoginIdAsLong());
        return Result.ok(bookService.getInfoById(id));
    }

    /**
     * 添加书籍
     */
    @PostMapping
    @CheckRole(UserPermission.ADMIN)
    public Result<Object> add(@RequestBody BookDTO bookDTO){
        log.info("添加书籍，操作员id={}", StpUtil.getLoginIdAsLong());
        bookService.add(bookDTO);
        return Result.ok();
    }

    /**
     * 修改书籍
     */
    @PutMapping
    @CheckRole(UserPermission.ADMIN)
    public Result<Object> modify(@RequestBody BookDTO bookDTO){
        log.info("修改书籍信息，操作员id={}", StpUtil.getLoginIdAsLong());
        bookService.modify(bookDTO);
        return Result.ok();
    }

    /**
     * 删除书籍（不使用逻辑删除）
     */
    @DeleteMapping("/{ids}")
    @CheckRole(UserPermission.ADMIN)
    public Result<Object> deleteBatch(@PathVariable List<Long> ids){
        log.info("删除书籍，操作员id={}", StpUtil.getLoginIdAsLong());
        bookService.deleteBatch(ids);
        return Result.ok();
    }
}
