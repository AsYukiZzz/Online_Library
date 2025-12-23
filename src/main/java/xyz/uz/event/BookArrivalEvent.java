package xyz.uz.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 书籍补货通知
 */

@Getter
public class BookArrivalEvent extends ApplicationEvent {

    // 书籍 Id
    private final Long bookId;

    public BookArrivalEvent(Object source, Long bookId) {
        super(source);
        this.bookId = bookId;
    }
}
