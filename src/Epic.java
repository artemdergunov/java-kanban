import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description, int id) {
        super(name, description, id, TaskStatus.NEW);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        updateEpicStatus();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    // обновление статусов
    public void updateEpicStatus() {
        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != TaskStatus.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != TaskStatus.DONE) {
                allDone = false;
            }
        }

        if (allDone) {
            this.status = TaskStatus.DONE;
        } else if (allNew || subtasks.isEmpty()) {
            this.status = TaskStatus.NEW;
        } else {
            this.status = TaskStatus.IN_PROGRESS;
        }
    }
}