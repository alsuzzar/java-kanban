package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void shouldReturnNewTaskManager()
    {
        TaskManager testTaskManager = Managers.getDefault();
        assertNotNull(testTaskManager, "объект в утилитарном классе не должен возвращать null");
        assertTrue(testTaskManager instanceof InMemoryTaskManager,
                "Ожидался InMemoryTaskManager");
    }

    @Test
    void shouldReturnNewHistoryManager()
    {
        HistoryManager testHistoryManager = Managers.getDefaultHistory();
        assertNotNull(testHistoryManager, "объект в утилитарном классе не должен возвращать null");
        assertTrue(testHistoryManager instanceof InMemoryHistoryManager,
                "Ожидался InMemoryHistoryManager");
    }

}