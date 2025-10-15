package manager;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.util.ArrayList;
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
    void shouldReturnTasksinAddedOrder() {
        ArrayList<Task> expectedList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Task task = new Task();
            task.setName("Test" + i);
            task.setDescription("Test Description" + i);
            task.setStatus(Status.NEW);
            task.setId(i);
            testHistoryManager.addToHistory(task);
            expectedList.add(task);
        }
        final List<Task> historyList = testHistoryManager.getHistory();
        assertEquals(3, historyList.size(), "Должно добавиться 3 задачи.");
        assertEquals(historyList, expectedList, "Списки задач должны совпадать по порядку");
        assertEquals(expectedList.size(), historyList.size(), "Размеры списков должны совпадать");

    }

    @Test
    void shouldRemovePreviouslyOpenedSameTask() {
        ArrayList<Task> expectedList = new ArrayList<>();
        Task task1 = new Task();
        task1.setName("Test" + 1);
        task1.setDescription("Test Description" + 1);
        task1.setStatus(Status.NEW);
        task1.setId(1);
        Task task2 = new Task();
        task2.setName("Test" + 2);
        task2.setDescription("Test Description" + 2);
        task2.setStatus(Status.NEW);
        task2.setId(2);
        Task task3 = new Task();
        task3.setName("Test" + 3);
        task3.setDescription("Test Description" + 3);
        task3.setStatus(Status.NEW);
        task3.setId(3);
        testHistoryManager.addToHistory(task1);
        testHistoryManager.addToHistory(task2);
        testHistoryManager.addToHistory(task3);
        testHistoryManager.addToHistory(task1);
        expectedList.add(task2);
        expectedList.add(task3);
        expectedList.add(task1);

        final List<Task> historyList = testHistoryManager.getHistory();
        assertEquals(historyList, expectedList, "Повторно открытая задача должна перемещаться в конец истории,"
                + "не дублироваться.");

    }

    @Test
    void shouldRemoveTask() {
        ArrayList<Task> expectedList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Task task = new Task();
            task.setName("Test" + i);
            task.setDescription("Test Description" + i);
            task.setStatus(Status.NEW);
            task.setId(i);
            testHistoryManager.addToHistory(task);
            expectedList.add(task);
        }
        testHistoryManager.removeFromHistory(1);
        expectedList.remove(1);
        List<Task> historyList = testHistoryManager.getHistory();
        assertEquals(historyList, expectedList, "Корректное удаление задачи");
        assertEquals(historyList.size(), expectedList.size(), "Корректный размер списка");
        testHistoryManager.removeFromHistory(0);
        testHistoryManager.removeFromHistory(2);
        historyList = testHistoryManager.getHistory();
        assertTrue(historyList.isEmpty(), "После удаления всех задач история должна быть пустой.");
    }

    @Test
    void shouldKeepTasksOrderAfterRemove() {
        ArrayList<Task> expectedList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Task task = new Task();
            task.setName("Test" + i);
            task.setDescription("Test Description" + i);
            task.setStatus(Status.NEW);
            task.setId(i);
            testHistoryManager.addToHistory(task);
            expectedList.add(task);
        }
        testHistoryManager.removeFromHistory(1);
        expectedList.remove(1);
        List<Task> historyList = testHistoryManager.getHistory();
        assertEquals(historyList, expectedList, "Корректный порядок в списке при удалении задачи");
        testHistoryManager.removeFromHistory(0);
        expectedList.remove(0);
        historyList = testHistoryManager.getHistory();
        assertEquals(historyList, expectedList, "Корректный порядок в списке при удалении head");
        Task lastTask = expectedList.get(expectedList.size() - 1);
        testHistoryManager.removeFromHistory(lastTask.getId());
        expectedList.remove(lastTask);
        historyList = testHistoryManager.getHistory();
        assertEquals(historyList, expectedList, "Корректный порядок в списке при удалении tail");
    }
}