package om.github.buerxixi.easydcom.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 事件消息
 *
 * @author <a href="mailto:EMAIL * @author <a href="mailto:liujiaqiang@outlook.com">Liujiaqiang</a>
 * @since 2025/04/02 16:58
 */
@Data
@AllArgsConstructor
public class EventMessage {
    private String message;
    private Throwable throwable;
}
