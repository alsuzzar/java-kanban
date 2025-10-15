package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void shouldBeEqualIfAllFieldsAreEqual()
    {
        Epic epic1 = new Epic();
        epic1.setName("Test");
        epic1.setDescription("Test Description");
        epic1.setStatus(Status.NEW);
        epic1.setId(1);

        Epic epic2 = new Epic();
        epic2.setName("Test");
        epic2.setDescription("Test Description");
        epic2.setStatus(Status.NEW);
        epic2.setId(1);
        assertEquals(epic1, epic2, "Эпики с одинаковыми полями должны быть равны");

    }

}