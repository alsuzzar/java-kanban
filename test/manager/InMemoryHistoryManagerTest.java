package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager testHistoryManager;

    @BeforeEach
    void setUp() {
        testHistoryManager = new InMemoryHistoryManager();
    }

    @Test
    void shouldAddToHistory() {

        Task task = new Task();
        task.setName("Test");
        task.setDescription("Test Description");
        task.setStatus(Status.NEW);

        testHistoryManager.addToHistory(task);
        final List<Task> historyList = testHistoryManager.getHistory();
        assertNotNull(historyList, "После добавления задачи, история не должна быть пустой.");
        assertEquals(1, historyList.size(), "После добавления задачи, история не должна быть пустой.");
    }

    @Test
    void shouldSaveOnlyTenTasksInHistory() {

        for (int i = 0; i < 12; i++) {
            Task task = new Task();
            task.setName("Test" + i);
            task.setDescription("Test Description" + i);
            task.setStatus(Status.NEW);
            task.setId(i);
            testHistoryManager.addToHistory(task);
        }
        final List<Task> historyList = testHistoryManager.getHistory();
        assertEquals(10, historyList.size(), "Должно добавиться 10 задач.");
        assertEquals("Test2", historyList.get(0).getName(), "Первая задача должна стать 'Test2'");
    }
}