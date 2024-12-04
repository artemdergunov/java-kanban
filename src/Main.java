public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создание двух задач
        Task task1 = new Task("Покормить кота", "Описание для Задачи 1", manager.generateId(), TaskStatus.NEW);
        Task task2 = new Task("Сходить в магазин", "Описание для Задачи 2", manager.generateId(), TaskStatus.NEW);
        manager.createTask(task1);
        manager.createTask(task2);

        // Создание эпика с двумя подзадачами
        Epic epic1 = new Epic("Переезд", "Описание для Эпика 1", manager.generateId());
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Собрать вещи", "Описание для Подзадачи 1", manager.generateId(), TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Перевезти", "Описание для Подзадачи 2", manager.generateId(), TaskStatus.NEW, epic1.getId());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        // Создание эпика с одной подзадачей
        Epic epic2 = new Epic("Ремонт на даче", "Описание для Эпика 2", manager.generateId());
        manager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Купить материалы в городе", "Описание для Подзадачи 3", manager.generateId(), TaskStatus.NEW, epic2.getId());
        manager.createSubtask(subtask3);

        // Распечатка списков эпиков, задач и подзадач
        System.out.println("Все эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
            System.out.println();
        }

        System.out.println("Все задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
            System.out.println();
        }

        System.out.println("Все подзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
            System.out.println();
        }

        // Изменение статусов созданных объектов
        task1.setStatus(TaskStatus.DONE);
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        subtask3.setStatus(TaskStatus.DONE);

        manager.updateTask(task1);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        manager.updateSubtask(subtask3);

        // Распечатка обновленных статусов
        System.out.println("Обновленные задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
            System.out.println();
        }

        System.out.println("Обновленные эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
            System.out.println();
        }

        System.out.println("Обновленные подзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
            System.out.println();
        }

        // Удаление одной из задач и одного из эпиков
        manager.deleteTask(task2.getId());
        manager.deleteEpic(epic1.getId());

        // Распечатка списков после удаления
        System.out.println("После удаления - Все эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
            System.out.println();
        }

        System.out.println("После удаления - Все задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
            System.out.println();
        }

        System.out.println("После удаления - Все подзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
            System.out.println();
        }
    }
}
