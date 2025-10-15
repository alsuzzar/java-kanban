package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest
{

    @Test
    void shouldBeEqualIfAllFieldsAreEqual()
    {
        Subtask subtask1 = new Subtask();
        subtask1.setName("Test");
        subtask1.setDescription("Test Description");
        subtask1.setStatus(Status.NEW);
        subtask1.setId(1);

        Subtask subtask2 = new Subtask();
        subtask2.setName("Test");
        subtask2.setDescription("Test Description");
        subtask2.setStatus(Status.NEW);
        subtask2.setId(1);
        assertEquals(subtask1, subtask2, "Эпики с одинаковыми полями должны быть равны");

    }

}