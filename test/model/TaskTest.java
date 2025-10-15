package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void shouldBeEqualIfAllFieldsAreEqual() {
        Task task1 = new Task();
        task1.setName("Test");
        task1.setDescription("Test Description");
        task1.setStatus(Status.NEW);
        task1.setId(1);

        Task task2 = new Task();
        task2.setName("Test");
        task2.setDescription("Test Description");
        task2.setStatus(Status.NEW);
        task2.setId(1);
        assertEquals(task1, task2, "Задачи с одинаковыми полями должны быть равны");

    }
}