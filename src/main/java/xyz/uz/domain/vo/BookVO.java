package xyz.uz.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍分页查询返回信息
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookVO {

    /**
     * ID
     */
    private Long id;

    /**
     * 书名
     */
    private String name;

    /**
     * 作者
     */
    private String author;

    /**
     * 简介
     */
    private String intro;

    /**
     * ISBN
     */
    private String ISBN;

    /**
     * 馆藏数量
     */
    private Integer amount;

    /**
     * 分类
     */
    private String category;

    /**
     * 是否热门 (0:否 1:是)
     */
    private Integer isPopular;
}
