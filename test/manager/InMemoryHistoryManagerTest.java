package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import enums.Status;
import task.Epic;
import task.Subtask;
import task.Task;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void getHistoryShouldReturnListOf10Tasks() {
        for (int i = 0; i < 20; i++) {
            taskManager.addTask(new Task("Задача", "Описание"));
        }

        List<Task> tasks = taskManager.getTasks();
        for (Task task : tasks) {
            taskManager.getTaskByID(task.getId());
        }

        List<Task> tasksHistory = taskManager.getHistory();
        assertEquals(10, tasksHistory.size(), "Неверное количество элементов в истории ");
    }

    @Test
    public void getHistoryShouldReturnOldTaskAfterUpdate() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.addTask(task1);
        taskManager.getTaskByID(task1.getId());
        taskManager.updateTask(new Task("Задача изменилась",
                "Описание задачи изменилось"));
        List<Task> tasks = taskManager.getHistory();
        Task oldTask = tasks.getFirst();

        assertEquals(task1.getName(), oldTask.getName(), "В истории не сохранилась старая версия задачи");
        assertEquals(task1.getDescription(), oldTask.getDescription(),
                "В истории не сохранилась старая версия задачи");
    }
}



