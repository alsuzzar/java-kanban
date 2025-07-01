package manager;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager testHistoryManager;
    Task task;

    @BeforeEach
    void setUp() {
        testHistoryManager = new InMemoryHistoryManager();

        task = new Task();
        task.setName("Test");
        task.setDescription("Test Description");
        task.setStatus(Status.NEW);
    }

    @Test
    void shouldAddToHistory() {
        testHistoryManager.addToHistory(task);
        final List<Task> historyList = testHistoryManager.getHistory();
        assertNotNull(historyList, "После добавления задачи, история не должна быть пустой.");
        assertEquals(1, historyList.size(), "После добавления задачи, история не должна быть пустой.");
    }


}