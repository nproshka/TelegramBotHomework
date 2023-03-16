package pro.sky.telegrambot.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "notification_task_five")
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String message;
    private LocalDateTime dateAndTime;

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", message='" + message + '\'' +
                ", dateAndTime=" + dateAndTime +
                '}';
    }
}
