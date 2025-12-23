package xyz.uz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import xyz.uz.domain.query.BookQuery;
import xyz.uz.domain.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.uz.domain.vo.BookVO;

import java.util.List;

/**
* 针对表【book(图书表)】的数据库操作Mapper
*/
public interface BookMapper extends BaseMapper<Book> {

    /**
     * 书籍分页条件查询
     */
    IPage<BookVO> pageQuery(IPage<BookVO> infoIPage, BookQuery bookQuery);

    /**
     * 书籍分类查询
     */
    @Select("select category from book group by category")
    List<String> categoryQuery();

    /**
     * 删除书籍（不使用逻辑删除）
     */
    void deleteBatch(@Param("ids") List<Long> ids);

    /**
     * 扣减库存
     * @param id 书籍 id
     * @param borrowAmount 请求数量
     */
    @Update("update book set amount = amount - #{borrowAmount} where id = #{id} and amount >= #{borrowAmount}")
    int deductionInventory(@Param("id")Long id, @Param("borrowAmount")Integer borrowAmount);

    /**
     * 增加库存
     * @param id 书籍 id
     * @param amount 请求数量
     */
    @Update("update book set amount = amount + #{amount} where id = #{id}")
    void increaseInventory(@Param("id")Long id, @Param("amount")Integer amount);
}




