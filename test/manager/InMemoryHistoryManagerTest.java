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
    private static HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void getHistoryShouldReturnAllTasks() {
        for (int i = 0; i < 15; i++) {
            Task task = new Task("Задача " + i, "Описание " + i);
            taskManager.addTask(task);
            taskManager.getTaskByID(task.getId());
        }

        List<Task> tasksHistory = taskManager.getHistory();
        assertEquals(15, tasksHistory.size(), "История должна хранить все задачи");
    }

    @Test
    public void getHistoryShouldNotContainDuplicates() {
        Task task = new Task("Задача", "Описание");
        taskManager.addTask(task);

        for (int i = 0; i < 5; i++) {
            taskManager.getTaskByID(task.getId());
        }

        List<Task> tasksHistory = taskManager.getHistory();
        assertEquals(1, tasksHistory.size(), "История не должна содержать дубликатов");
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

    @Test
    public void removeShouldDeleteTaskFromHistory() {
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        // Добавляем задачи в историю
        taskManager.getTaskByID(task1.getId());
        taskManager.getTaskByID(task2.getId());

        // Удаляем задачу из истории через TaskManager
        taskManager.deleteTaskByID(task1.getId());

        // Получаем историю
        List<Task> tasksHistory = taskManager.getHistory();

        // Проверяем, что задача удалена из истории
        assertFalse(tasksHistory.contains(task1), "Задача должна быть удалена из истории");
        assertTrue(tasksHistory.contains(task2), "Задача должна остаться в истории");
    }

    @Test
    public void subtaskShouldBeRemovedFromEpic() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        taskManager.addSubtask(subtask);

        taskManager.deleteSubtaskByID(subtask.getId());

        Epic updatedEpic = taskManager.getEpicByID(epic.getId());
        assertFalse(updatedEpic.getSubtaskList().contains(subtask), "Подзадача должна быть удалена из эпика");
    }

    @Test
    void deletedSubtaskShouldNotExist() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        taskManager.addSubtask(subtask);

        int subtaskId = subtask.getId();

        taskManager.deleteSubtaskByID(subtaskId);

        assertNull(taskManager.getSubtaskByID(subtaskId), "Подзадача должна быть удалена из менеджера.");

        Epic updatedEpic = taskManager.getEpicByID(epic.getId());
        assertFalse(updatedEpic.getSubtaskList().contains(subtask), "Эпик не должен содержать удаленную подзадачу.");
    }

    @Test
    void epicShouldNotContainOutdatedSubtaskIds() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        taskManager.addSubtask(subtask);

        int subtaskId = subtask.getId();

        taskManager.deleteSubtaskByID(subtaskId);

        Epic updatedEpic = taskManager.getEpicByID(epic.getId());

        assertFalse(updatedEpic.getSubtaskList().contains(subtaskId),
                "Эпик не должен содержать id удаленной подзадачи.");
    }
}

