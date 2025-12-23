package xyz.uz.domain.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍分页条件查询数据模型
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookQuery {

    /**
     * 页码
     */
    private Integer page;

    /**
     * 单页条目数
     */
    private Integer size;

    /**
     * 书名
     */
    private String name;

    /**
     * 作者
     */
    private String author;

    /**
     * ISBN
     */
    private String ISBN;

    /**
     * 图书分类
     */
    private String category;

    /**
     * 是否为热门书籍 (0:否 1:是)
     */
    private Integer isPopular;
}
