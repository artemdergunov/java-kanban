package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import task.Task;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;


class FileBackedTaskManagerTest {
    private TaskManager taskManager;
    private Path filePath;
    private FileBackedTaskManager manager;


    @BeforeEach
    void setUp() throws IOException {
        filePath = Files.createTempFile("task_manager_test", ".csv");
        taskManager = Managers.getFileBackedTaskManager(filePath.toFile());
    }

    @Test
    void testLoad() throws Exception {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        TaskManager loadedTaskManager = Managers.loadFromFile(filePath.toFile());
        assertEquals(2, loadedTaskManager.getTasks().size());

        String expected = taskManager.getTasks().getFirst().toStringFromFile();
        String actually = loadedTaskManager.getTasks().getFirst().toStringFromFile();
        String expected2 = taskManager.getTasks().getLast().toStringFromFile();
        String actually2 = loadedTaskManager.getTasks().getLast().toStringFromFile();

        assertEquals(expected, actually);
        assertEquals(expected2, actually2);
    }
}




