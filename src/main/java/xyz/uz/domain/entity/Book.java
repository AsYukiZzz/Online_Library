package xyz.uz.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图书表
 */

@Data
@TableName(value ="book")
@EqualsAndHashCode(callSuper = true)
public class Book extends BaseEntity {

    /**
     * 书名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 作者
     */
    @TableField(value = "author")
    private String author;

    /**
     * 简介
     */
    @TableField(value = "intro")
    private String intro;

    /**
     * ISBN
     */
    @TableField(value = "ISBN")
    private String ISBN;

    /**
     * 馆藏数量
     */
    @TableField(value = "amount")
    private Integer amount;

    /**
     * 分类
     */
    @TableField(value = "category")
    private String category;

    /**
     * 是否热门 (0:否 1:是)
     */
    @TableField(value = "is_popular")
    private Integer isPopular;
}