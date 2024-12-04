import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int currentId = 1;

    // генерация уникального id
    public int generateId() {
        return currentId++;
    }

    // возвращение задач, подзадач и эпиков по идентификатору
    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    // возвращение всех задач, эпиков и подзадач
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // возвращение всех подзадач принадлежащих опеределённому эпику
    public ArrayList<Subtask> getSubtasksByEpic(int epicId) {
        ArrayList<Subtask> result = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                result.add(subtask);
            }
        }
        return result;
    }
    // создание задач, подзадач и эпиков
    public void createTask(Task task) {
        task.id = generateId();
        tasks.put(task.id, task);
    }

    public void createEpic(Epic epic) {
        epic.id = generateId();
        epics.put(epic.id, epic);
    }

    public void createSubtask(Subtask subtask) {
        subtask.id = generateId();
        subtasks.put(subtask.id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
        }
    }

    // обновление задач, подзадач и эпиков
    public void updateTask(Task task) {
        tasks.put(task.id, task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.id, epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
        }
    }

    // удаление задач, эпиков и подазадч по идентификатору
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
            epics.remove(id);
        }
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtasks().remove(subtask);
                epic.updateEpicStatus();
            }
            subtasks.remove(id);
        }
    }

    // удаление всех задач, подзадач и эпиков
    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
        }
        epics.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.updateEpicStatus(); // Обновление статуса эпика после удаления всех подзадач
        }
    }
}