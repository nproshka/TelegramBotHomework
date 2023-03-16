package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Task;
import pro.sky.telegrambot.repository.TaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TaskRepository taskRepository;

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Long chatId = update.message().chat().id();
            logger.info("id chat: {}", chatId);

            Integer messageId = update.message().messageId();
            logger.info("id message: {}", messageId);

            if (update.message().text().equals("/start")) {
                SendMessage helloMessage = new SendMessage(chatId, "Привет, мир!!!");
                telegramBot.execute(helloMessage);
            } else {


                Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

                Matcher matcher = pattern.matcher(update.message().text());

                String dateAndTime;
                String text;


                if (matcher.matches()) {
                    dateAndTime = matcher.toMatchResult().group(1);
                    text = matcher.toMatchResult().group(3);
                } else {
                    logger.info("Что-то не так");
                    SendMessage nonCorrect = new SendMessage(chatId, "Вы ввели некорректные данные, пожалуйста введите данные в формате: дд.мм.гггг чч:мм 'Текст задания без цифр'");
                    telegramBot.execute(nonCorrect);
                    return;
                }
                logger.info("pattern: {}", dateAndTime);
                logger.info("text: {}", text);

                Task task = new Task();
                task.setChatId(chatId);
                task.setMessage(text);
                task.setDateAndTime(LocalDateTime.parse(dateAndTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

                logger.info("s: {}", task);

                taskRepository.save(task);
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void checkWhatNeedToDo() {

        final List<Task> allTasks = taskRepository.findAll();

        for (int i = 0; i < allTasks.size(); i++) {
            if (allTasks.get(i).getDateAndTime().equals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) {
                SendMessage tempMessage = new SendMessage(allTasks.get(i).getChatId(), allTasks.get(i).getMessage());
                telegramBot.execute(tempMessage);
                taskRepository.deleteById(allTasks.get(i).getId());
            }
        }
    }


}
