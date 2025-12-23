package xyz.uz.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 书籍下架通知
 */

@Getter
public class BookRemovedEvent extends ApplicationEvent {

    // 书籍 Id
    private final Long bookId;

    public BookRemovedEvent(Object source, Long bookId) {
        super(source);
        this.bookId = bookId;
    }
}
