package xyz.uz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知格式
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketMessageVO {

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 通知类型
     */
    private String type;

    /**
     * 默认构造
     */
    public static WebsocketMessageVO standardNotification(String content){
        return WebsocketMessageVO.builder()
                .title("待办提醒")
                .content(content)
                .type("warning")
                .build();
    }
}
