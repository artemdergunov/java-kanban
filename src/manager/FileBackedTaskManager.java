package manager;

import enums.Status;
import task.Epic;
import task.Subtask;
import task.Task;
import enums.TaskType;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.List;

import exceptions.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() throws ManagerSaveException {
        final String title = "id,type,name,status,description,id_links\n";
        List<String> lines = new ArrayList<>();
        lines.add(title);

        for (Task task : getTasks()) {
            lines.add(task.toStringFromFile());
        }

        for (Epic epic : getEpics()) {
            lines.add(epic.toStringFromFile());
        }

        for (Subtask subTask : getSubtasks()) {
            lines.add(subTask.toStringFromFile());
        }

        if (file == null) {
            throw new ManagerSaveException("Невозможно сохранить данные в файл.");
        }

        try {
            FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String line : lines) {
                bufferedWriter.write(line);
            }

            bufferedWriter.close();

        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл: " + exception.getMessage());
        }
    }

    public void load() {
        List<String> lines;
        try {
            lines = loadFromCsv();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        lines.removeFirst();

        for (String line : lines) {
            deSerialize(line);
        }
    }

    private List<String> loadFromCsv() throws ManagerSaveException {
        if (file == null) {
            throw new ManagerSaveException("Невозможно загрузить данные из файла.");
        }

        List<String> lines = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while (bufferedReader.ready()) {
                lines.add(bufferedReader.readLine());
            }

            bufferedReader.close();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при загрузке данных из файла: " + exception.getMessage());
        }
        return lines;
    }

    private void deSerialize(String line) {
        String[] lines = line.trim().split(",");
        TaskType taskType = TaskType.valueOf(lines[1]);
        switch (taskType) {
            case TASK -> super.addTask(
                    new Task(
                            Integer.parseInt(lines[0]),
                            lines[2],
                            lines[4],
                            getTaskStatusFromString(lines[3])
                    ));
            case EPIC -> super.addEpic(
                    new Epic(
                            Integer.parseInt(lines[0]),
                            lines[2],
                            lines[4],
                            getTaskStatusFromString(lines[3])
                    ));

            case SUBTASK -> {
                int subTaskId = Integer.parseInt(lines[0]);
                int epicId = Integer.parseInt(lines[lines.length - 1]);
                super.addSubtask(
                        new Subtask(
                                subTaskId,
                                lines[2],
                                lines[4],
                                getTaskStatusFromString(lines[3]),
                                epicId
                        ));
            }
        }
    }

    private Status getTaskStatusFromString(String line) {
        return switch (line) {
            case "NEW" -> Status.NEW;
            case "IN_PROGRESS" -> Status.IN_PROGRESS;
            case "DONE" -> Status.DONE;
            default -> Status.NEW;
        };
    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save();
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteTaskByID(int id) {
        super.deleteTaskByID(id);
        save();
    }

    @Override
    public void deleteEpicByID(int id) {
        super.deleteEpicByID(id);
        save();
    }

    @Override
    public void deleteSubtaskByID(int id) {
        super.deleteSubtaskByID(id);
        save();
    }
}






















