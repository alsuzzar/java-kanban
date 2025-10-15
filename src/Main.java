import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        runTest();
    }

    public static void runTest() {
        TaskManager manager = new InMemoryTaskManager();

        Task task = new Task();
        task.setName("Затопить печь");
        task.setDescription("Подкинуть дров и зажечь");
        task.setStatus(Status.NEW);
        createTask(manager, task);

        Task task2 = new Task();
        task2.setName("Наколоть дров");
        task2.setDescription("В лесу");
        task2.setStatus(Status.NEW);
        createTask(manager, task2);

        Epic epic = new Epic();
        epic.setName("Испечь пирог");
        epic.setDescription("Сметанник");
        epic.setStatus(Status.NEW);
        createEpic(manager, epic);

        Subtask subtask = new Subtask();
        subtask.setName("Поставить тесто");
        subtask.setDescription("См. рецепт");
        subtask.setStatus(Status.NEW);
        subtask.setEpicId(epic.getId());
        createSubtask(manager, subtask);

        Subtask subtask2 = new Subtask();
        subtask2.setName("Приготовить начинку");
        subtask2.setDescription("См. рецепт начинки");
        subtask2.setStatus(Status.NEW);
        subtask2.setEpicId(epic.getId());
        createSubtask(manager, subtask2);

        Epic epic2 = new Epic();
        epic2.setName("Загрузить посудомойку");
        epic2.setDescription("Вечером");
        epic2.setStatus(Status.NEW);
        createEpic(manager, epic2);

        Subtask subtask3 = new Subtask();
        subtask3.setName("Разобрать значала чистую");
        subtask3.setDescription("попросить дочь");
        subtask3.setStatus(Status.NEW);
        subtask3.setEpicId(epic2.getId());
        createSubtask(manager, subtask3);

        printTasks(manager);


        task.setStatus(Status.DONE);
        updateTaskById(manager, task);

        task2.setStatus(Status.DONE);
        updateTaskById(manager, task2);

        subtask.setStatus(Status.DONE);
        updateSubtaskById(manager, subtask);

        subtask2.setStatus(Status.DONE);
        updateSubtaskById(manager, subtask2);

        subtask3.setStatus(Status.IN_PROGRESS);
        updateSubtaskById(manager, subtask3);

        printTasks(manager);
        System.out.println("\nПодзадачи для эпика 3: " + getAllSubtasksByEpic(manager, 3));
        deleteTaskById(manager, 1);
        System.out.println("\nУдалена задача с ID: 1");
        deleteEpicById(manager, 3);
        System.out.println("\nУдален эпик с ID: 3");
        printTasks(manager);
    }

    public static void createTask(TaskManager manager, Task task) {
        Task newTask = manager.createTask(task);
        System.out.println("\nЗадача создана с ID: " + newTask.getId());
    }

    public static Epic createEpic(TaskManager manager, Epic epic) {
        Epic newEpic = manager.createEpic(epic);
        System.out.println("\nЭпик создан с id: " + newEpic.getId());
        return newEpic;
    }

    public static void createSubtask(TaskManager manager, Subtask subtask) {
        Subtask newSubtask = manager.createSubtask(subtask);
        System.out.println("\nПодзадача создана с id: " + newSubtask.getId() + ", относится к эпику с id: " +
                newSubtask.getEpicId());
    }

    public static void updateTaskById(TaskManager manager, Task task) {
        manager.updateTaskById(task);
    }

    public static void updateEpicById(TaskManager manager, Epic epic) {
        manager.updateEpicById(epic);
    }

    public static void updateSubtaskById(TaskManager manager, Subtask subtask) {
        manager.updateSubtaskById(subtask);
    }

    public static ArrayList<Subtask> getAllSubtasksByEpic(TaskManager manager, int epicId) {
        return manager.getAllSubtasksByEpic(epicId);
    }

    public static void deleteTaskById(TaskManager manager, int id) {
        manager.deleteTaskById(id);
    }

    public static void deleteEpicById(TaskManager manager, int id) {
        manager.deleteEpicById(id);
    }

    public static void deleteSubtaskById(TaskManager manager, int id) {
        manager.deleteSubtaskById(id);
    }

    public static void deleteAllTasks(TaskManager manager) {
        manager.deleteAllTasks();
    }

    public static void deleteAllEpics(TaskManager manager) {
        manager.deleteAllEpics();
    }

    public static void deleteAllSubtasks(TaskManager manager) {
        manager.deleteAllSubtasks();
    }


    public static void printTasks(TaskManager manager) {
        System.out.println("Список дел: ");
        System.out.println("Задачи: " + manager.getAllTasks());
        System.out.println("Эпики с подзадачами: ");
        for (Epic epic : manager.getAllEpics())
        {
            System.out.println("Эпик" + epic + "\nПодзадачи: ");
            ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
            for (int subtaskId : subtaskIds)
            {
                Subtask subtask = manager.getSubtaskById(subtaskId);
                if (subtask != null)
                {
                    System.out.println(subtask);
                }
            }
        }

    }

}
