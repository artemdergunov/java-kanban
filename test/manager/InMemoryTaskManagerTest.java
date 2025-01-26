package manager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import enums.Status;
import task.Task;
import task.Subtask;
import task.Epic;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void aNewTaskShouldBeCreated() {
        Task task1 = taskManager.addTask(new Task("Задача 1", "Описание задачи 1"));
        final Task savedTask = taskManager.getTaskByID(task1.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void aNewEpicAndSubtaskShouldBeCreated() {
        Epic epic1 = taskManager.addEpic(new Epic("Эпик 1",
                "Описание эпика 1"));
        Subtask epic1Subtask1 = taskManager.addSubtask(new Subtask("Подзадача 1",
                "Описание подзадачи 1", epic1.getId()));
        Subtask epic1Subtask2 = taskManager.addSubtask(new Subtask("Подзадача 2",
                "Описание подзадачи 2", epic1.getId()));

        final Epic savedEpic = taskManager.getEpicByID(epic1.getId());
        final Subtask savedSubtask1 = taskManager.getSubtaskByID(epic1Subtask1.getId());
        final Subtask savedSubtask2 = taskManager.getSubtaskByID(epic1Subtask2.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertNotNull(savedSubtask1, "Подзадача не найдена.");
        assertEquals(epic1, savedEpic, "Эпики не совпадают.");
        assertEquals(epic1Subtask1, savedSubtask1, "Подзадачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(savedSubtask1, subtasks.getFirst(), "Подзадачи не совпадают.");
    }

    @Test
    void tasksWithTheSpecifiedIdAndTheGeneratedShouldNotConflict() {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        taskManager.addTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2");
        taskManager.addTask(task2);

        Task savedTask1 = taskManager.getTaskByID(1);
        Task savedTask2 = taskManager.getTaskByID(task2.getId());

        assertNotNull(savedTask1, "Задача с заданным ID не найдена.");
        assertNotNull(savedTask2, "Задача с сгенерированным ID не найдена.");
        assertNotEquals(savedTask1.getId(), savedTask2.getId(), "ID задач конфликтуют.");
    }

    @Test
    void createdTaskShouldNotChangeWhenAddedToManager() {
        Task expected = new Task(1, "Задача1", "Описание задачи 1", Status.DONE);
        taskManager.addTask(expected);
        List<Task> tasks = taskManager.getTasks();
        Task actual = tasks.getFirst();
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Test
    public void testSubtaskCannotBeItsOwnEpicReturnsNull() {
        Subtask subtask = new Subtask("Подзадача 1", "Описание", 1);
        taskManager.updateSubtask(new Subtask("Подзадача 1","Описание", subtask.getId()));
        Subtask result = taskManager.addSubtask(subtask);

        assertNull(result, "Подзадача не должна быть добавлена как свой эпик, метод должен вернуть null.");
    }
}

