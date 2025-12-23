package xyz.uz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍数据模型
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    /**
     * 书籍 ID
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
     * 图书分类
     */
    private String category;

    /**
     * 是否为热门书籍 (0:否 1:是)
     */
    private Integer isPopular;
}
