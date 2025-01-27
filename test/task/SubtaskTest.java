package task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import enums.Status;

class SubtaskTest {

    @Test
    public void SubtasksWithEqualIdShouldBeEqual() {
        Subtask subtask1 = new Subtask(10, "Купить билет на новый фильм", "На сайте, там дешевле", Status.NEW, 5);
        Subtask subtask2 = new Subtask(10, "Не забыть попкорн", "Купить в магазине", Status.DONE, 5);
        assertEquals(subtask1, subtask2,
                "Ошибка! Наследники класса Task должны быть равны друг другу, если равен их id;");
    }
}
