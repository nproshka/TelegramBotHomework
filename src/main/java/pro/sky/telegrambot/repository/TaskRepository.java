package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {



}
