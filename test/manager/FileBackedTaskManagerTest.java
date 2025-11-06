package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static model.Type.*;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    FileBackedTaskManager testFileBackedTaskManager;
    File tempFile;
    Path testFilePath;
    private Task task;
    private Epic epic;
    private Subtask subtask;
    private Subtask subtask1;


    @BeforeEach
    void setUp() {
        try {
            tempFile = File.createTempFile("test", ".csv");
            testFileBackedTaskManager = new FileBackedTaskManager(tempFile);

            task = new Task();
            task.setName("TestTask");
            task.setType(TASK);
            task.setDescription("Test Description");
            task.setStatus(Status.NEW);

            int taskId = testFileBackedTaskManager.createTask(task).getId();

            epic = new Epic();
            epic.setName("TestEpic");
            epic.setType(EPIC);
            epic.setDescription("Test Description");
            epic.setStatus(Status.NEW);
            int epicId = testFileBackedTaskManager.createEpic(epic).getId();

            subtask = new Subtask();
            subtask.setName("TestSubtask");
            subtask.setType(SUBTASK);
            subtask.setDescription("Test Description");
            subtask.setStatus(Status.NEW);
            subtask.setEpicId(epicId);

            subtask1 = new Subtask();
            subtask1.setName("TestSubtask1");
            subtask1.setType(SUBTASK);
            subtask1.setDescription("Test Description");
            subtask1.setStatus(Status.NEW);
            subtask1.setEpicId(epicId);

            int subtaskId = testFileBackedTaskManager.createSubtask(subtask).getId();
            int subtask1Id = testFileBackedTaskManager.createSubtask(subtask1).getId();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void wrapUp() throws IOException {
        Files.deleteIfExists(tempFile.toPath());
    }

    @Test
    void shouldSaveAndLoadTasks() {

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(testFileBackedTaskManager.getAllTasks().size(),
                loadedManager.getAllTasks().size(),
                "Количество задач после загрузки должно совпадать");

        assertEquals(testFileBackedTaskManager.getAllEpics().size(),
                loadedManager.getAllEpics().size(),
                "Количество эпиков после загрузки должно совпадать");

        assertEquals(testFileBackedTaskManager.getAllSubtasks().size(),
                loadedManager.getAllSubtasks().size(),
                "Количество сабтасков после загрузки должно совпадать");

        Task originalTask = testFileBackedTaskManager.getAllTasks().get(0);
        Task loadedTask = loadedManager.getAllTasks().get(0);

        assertEquals(originalTask.getName(), loadedTask.getName(), "Имя задачи должно совпадать");
        assertEquals(originalTask.getDescription(), loadedTask.getDescription(), "Описание должно совпадать");
        assertEquals(originalTask.getStatus(), loadedTask.getStatus(), "Статус должен совпадать");
    }

    @Test
    void shouldHandleEmptyFileCorrectly() {
        try {
            Files.writeString(tempFile.toPath(), "id,type,name,status,description,epic\n");
        } catch (IOException e) {
            System.out.println("Не удалось перезаписать файл: " + e.getMessage());
        }
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());
    }
}
