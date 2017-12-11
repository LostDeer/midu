package aduio.lib.entitys;

/**
 * Created by ${LiChaoBo} on 2017/3/3.
 * 消息传递
 */
public class MessageEvent {
    private String message;
    private Object data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public MessageEvent(String message, Object data) {
        this.message = message;
        this.data = data;

    }
}
