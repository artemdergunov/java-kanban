import manager.Managers;
import enums.Status;
import manager.TaskManager;
import task.Task;
import task.Epic;
import task.Subtask;

public class Main {

    public static void main(String[] args) {

        final TaskManager taskManager = Managers.getDefault();

        // создаем две задачи
        Task task1 = new Task("Сходить в магазин", "Купить молоко обязательно!");
        Task task2 = new Task("Покормить кота", "Влажным кормом");


        taskManager.addTask(task1);
        taskManager.addTask(task2);

        System.out.println(task1);
        System.out.println(task2);


        // Создаем эпик с двумя подзадачами
        Epic epic1 = new Epic("Переезд", "В ближайшую неделю");
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Собрать вещи", "Сложить все в коробки", epic1.getId());
        Subtask subtask2 = new Subtask("Заказать машину", "Грузовую", epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        System.out.println(epic1);
        System.out.println(subtask1);
        System.out.println(subtask2);

        // Создаем эпик с одной подзадачей
        Epic epic2 = new Epic("Ремонт на даче", "В ближайший месяц");
        taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Купить материалы", "Отвезти на дачу", epic2.getId());

        taskManager.addSubtask(subtask3);

        // меняем статусы
        subtask2.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        task1.setStatus(Status.DONE);


        System.out.println(task1);
        System.out.println(epic1);

    }
}