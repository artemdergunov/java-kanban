package manager;

import enums.Status;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int curretndId = 1;

    private int generateID() {
        return curretndId++;
    }

    @Override
    public Task addTask(Task task) {
        task.setId(generateID());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(generateID());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicID());
        if (epic != null) {
            subtask.setId(generateID());
            epic.addSubtask(subtask);
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epic);
        } else {
            System.out.println("Такого эпика нет");
            return null;
        }
        return subtask;
    }

    @Override
    public Task updateTask(Task task) {
        Integer taskID = task.getId();
        if (taskID == 0 || !tasks.containsKey(taskID)) {
            return null;
        }
        tasks.put(taskID, task);
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Integer epicID = epic.getId();
        if (epicID != 0 && epics.containsKey(epicID)) {
            Epic oldEpic = epics.get(epicID);
            oldEpic.setName(epic.getName());
            oldEpic.setDescription(epic.getDescription());
            updateEpicStatus(epic);
        }
        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Integer subtaskID = subtask.getId();
        if (subtaskID == 0 || !subtasks.containsKey(subtaskID)) {
            return null;
        }

        int epicID = subtask.getEpicID();
        Subtask oldSubtask = subtasks.get(subtaskID);
        if (subtask.getEpicID() != oldSubtask.getEpicID()) {
            return null;
        }

        subtasks.put(subtaskID, subtask);
        Epic epic = epics.get(epicID);
        epic.removeSubtask(oldSubtask);
        epic.addSubtask(subtask);
        updateEpicStatus(epic);
        return subtask;
    }

    @Override
    public Task getTaskByID(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
        } else {
            System.out.println("Задача с ID " + id + " не найдена");
            return null;
        }
        return tasks.get(id);

    }

    @Override
    public Epic getEpicByID(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
        } else {
            System.out.println("Эпик с ID " + id + " не найден");
            return null;
        }
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
        } else {
            System.out.println("Подзадача с ID " + id + " не найдена");
            return null;
        }
        return subtasks.get(id);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            return epic.getSubtaskList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteTasks() {
        for (Integer taskId : tasks.keySet()) {

        }
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Integer epicId : epics.keySet()) {

        }
        for (Integer subtaskId : subtasks.keySet()) {

        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Integer subtaskId : subtasks.keySet()) {

        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteTaskByID(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);

        } else {
            System.out.println("Задача с ID " + " не найдена");
        }
    }

    @Override
    public void deleteEpicByID(int id) {
        if (epics.containsKey(id)) {
            ArrayList<Subtask> epicSubtasks = epics.get(id).getSubtaskList();
            for (Subtask subtask : epicSubtasks) {
                subtasks.remove(subtask.getId());

            }
            epics.remove(id);

        } else {
            System.out.println("Эпик с ID " + id + " не найден");
        }
    }

    @Override
    public void deleteSubtaskByID(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            int epicID = subtask.getEpicID();
            subtasks.remove(id);

            // обновляем список подзадач и статус эпика
            Epic epic = epics.get(epicID);
            epic.removeSubtask(subtask);
            updateEpicStatus(epic);
        } else {
            System.out.println("Подзадача с ID " + id + " не найдена");
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    private void updateEpicStatus(Epic epic) {
        int allDone = 0;
        int allNew = 0;
        ArrayList<Subtask> list = epic.getSubtaskList();

        for (Subtask subtask : list) {
            if (subtask.getStatus() == Status.DONE) {
                allDone++;
            }
            if (subtask.getStatus() == Status.NEW) {
                allNew++;
            }
        }
        if (allNew == list.size()) {
            epic.setStatus(Status.NEW);
        } else if (allDone == list.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}