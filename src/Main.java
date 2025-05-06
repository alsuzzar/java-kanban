import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
    runTest();
}

public static void runTest() {
    TaskManager manager = new TaskManager();

    createTask(manager, "Затопить печь", "Подкинуть дров и зажечь", Status.NEW);
    createTask(manager, "Наколоть дров", "В лесу", Status.NEW);

    Epic epic = createEpic(manager, "Испечь пирог", "Сметанник", Status.NEW);
    createSubtask(manager, "Поставить тесто", "См. рецепт", Status.NEW, epic.getId());
    createSubtask(manager, "Приготовить начинку", "См. рецепт начинки", Status.NEW, epic.getId());

    Epic epic2 = createEpic(manager, "Загрузить посудомойку", "Вечером", Status.NEW);
    createSubtask(manager, "Разобрать значала чистую", "попросить дочь", Status.NEW, epic2.getId());

    System.out.println(
manager.getAllTasks());

    updateTaskById(manager, "Затопить печь", "Подкинуть дров и зажечь", 1, Status.DONE);
    updateTaskById(manager, "Наколоть дров", "В лесу", 2, Status.DONE);

    updateSubtaskById(manager, "Поставить тесто", "См. рецепт", 4, Status.DONE);
    updateSubtaskById(manager, "Приготовить начинку", "См. рецепт начинки", 5, Status.DONE);

    updateSubtaskById(manager, "Разобрать значала чистую", "попросить дочь", 7,
            Status.IN_PROGRESS);

    System.out.println(
manager.getAllTasks());
    System.out.println("\nПодзадачи для эпика 3: " + getAllSubtasksByEpic(manager,3));
    deleteTaskById(manager,1);
    System.out.println("\nУдалена задача с ID: 1");
    deleteTaskById(manager,3);
    System.out.println("\nУдален эпик с ID: 3");
    System.out.println(
manager.getAllTasks());
}

    public static void createTask(TaskManager manager, String name, String description, Status status) {
        Task task = manager.createTask(name, description, status);
        System.out.println("\nЗадача создана с ID: " + task.getId());
    }

    public static Epic createEpic(TaskManager manager, String name, String description, Status status) {
        Epic epic = manager.createEpic(name, description, status, new ArrayList<>());
        System.out.println("\nЭпик создан с id: " + epic.getId());
        return epic;
    }

    public static void createSubtask(TaskManager manager, String name, String description, Status status, int epicId) {
        Subtask subtask = manager.createSubtask(name, description, status, epicId);
        System.out.println("\nПодзадача создана с id: " + subtask.getId() + ", относится к эпику с id: " + epicId);
    }

    public static void updateTaskById(TaskManager manager, String name, String description, int id, Status status) {
       manager.updateTaskById(name, description, id, status);
    }

    public static void updateEpicById(TaskManager manager, String newName, String newDescription, int id, Status newStatus) {
        manager.updateEpicById(newName, newDescription, id, newStatus);
    }

    public static void updateSubtaskById(TaskManager manager, String newName, String newDescription, int id,
                                         Status newStatus) {
        manager.updateSubtaskById(newName, newDescription, id, newStatus);
    }

    public static ArrayList<Subtask> getAllSubtasksByEpic(TaskManager manager, int epicId) {
        return manager.getAllSubtasksByEpic(epicId);
    }

    public static void deleteTaskById(TaskManager manager,int id) {
        manager.deleteTaskById(id);
    }

    public static void deleteAllTasks(TaskManager manager) {
        manager.deleteAllTasks();
    }

}
