package task;

import enums.Status;
import enums.TaskType;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public void removeSubtask(Subtask subtask) {
        subtaskList.remove(subtask);
    }

    public void clearSubtasks() {
        subtaskList.clear();
    }

    public ArrayList<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskList);
    }

    @Override
    public String toString() {
        return "tusk.Epic{" +
                "name= " + getName() + '\'' +
                ", description = " + getDescription() + '\'' +
                ", id=" + getId() +
                ", subtasks.size = " + subtaskList.size() +
                ", status = " + getStatus() +
                '}';
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s\n", getId(), TaskType.EPIC, getName(),getStatus(), getDescription());
    }
}