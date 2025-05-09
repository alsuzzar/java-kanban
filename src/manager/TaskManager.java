import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private HashMap<Integer, Task> allTasks = new HashMap<>();

    int taskCount = 0;

    public void updateTaskById(String newName, String newDescription, int id, Status newStatus) {
        Task task = allTasks.get(id);
        if (task == null) {
            System.out.println("Задача с ID " + id + " не найдена.");
            return;
        } else {
            Task updatedTask = new Task(newName, newDescription, id, newStatus);
            allTasks.put(id, updatedTask);
        }

        System.out.println("Задача с ID " + id + " успешно обновлена.");
    }

    public void updateSubtaskById(String newName, String newDescription, int id, Status newStatus) {
        Task task = allTasks.get(id);
        if (task == null) {
            System.out.println("\nЗадача с ID " + id + " не найдена.");
            return;
        } else {
            Subtask subtask = (Subtask) task;
            int epicId = subtask.getEpicId();
            Subtask updatedSubtask = new Subtask(newName, newDescription, id, newStatus, epicId);
            allTasks.put(id, updatedSubtask);
            updateEpicStatus(epicId);
        }
        System.out.println("\nПодзадача с ID " + id + " успешно обновлена.");
    }

    public void updateEpicById(String newName, String newDescription, int id, Status newStatus) {
        Task task = allTasks.get(id);
        if (task == null) {
            System.out.println("\nЗадача с ID " + id + " не найдена.");
            return;
        } else {
            Epic epic = (Epic) task;
            ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
            Epic updatedEpic = new Epic(newName, newDescription, id, newStatus, subtaskIds);
            allTasks.put(id, updatedEpic);
        }
        System.out.println("\nПодзадача с ID " + id + " успешно обновлена.");
    }


    public Task createTask(String name, String description, Status status) {
        Task task = new Task(name, description, ++taskCount, status);
        allTasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(String name, String description, Status status, ArrayList<Integer> subtaskIds) {
        Epic epic = new Epic(name, description, ++taskCount, status, subtaskIds);
        allTasks.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(String name, String description, Status status, int epicId) {
        Subtask subtask = new Subtask(name, description, ++taskCount, status, epicId);
        allTasks.put(subtask.getId(), subtask);
        Task task = allTasks.get(epicId);
        if (task instanceof Epic) {
            Epic epic = (Epic) task;
            ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
            int subtaskId = subtask.getId();
            subtaskIds.add(subtaskId);
        }
        return subtask;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.addAll(allTasks.values());
        return taskList;
    }

    public void deleteAllTasks() {
        allTasks.clear();
    }

    public void deleteTaskById(int id) {
            allTasks.remove(id);
    }
    public Task getTaskById(int id) {
        return allTasks.get(id);
    }

    public ArrayList<Subtask> getAllSubtasksByEpic(int epicId) {
        ArrayList<Subtask> allSubtasksByEpic = new ArrayList<>();

        for (Task task : allTasks.values()) {
            if (task instanceof Subtask) {
                Subtask subtask = (Subtask) task;
                if (subtask.getEpicId() == epicId) {
                    allSubtasksByEpic.add(subtask);
                }
            }
        }
        return allSubtasksByEpic;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();

        for (Task task : allTasks.values()) {
            if (task instanceof Subtask) {
                Subtask subtask = (Subtask) task;
                    allSubtasks.add(subtask);
                }
            }
        return allSubtasks;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();

        for (Task task : allTasks.values()) {
            if (task instanceof Epic) {
                Epic epic = (Epic) task;
                allEpics.add(epic);
            }
        }
        return allEpics;
    }

    public void updateEpicStatus(int epicId) {
        ArrayList<Subtask> allSubtasksByEpic = getAllSubtasksByEpic(epicId);
        Status statusEpic;
        boolean allNew = true;
        boolean allDone = true;

        if (allSubtasksByEpic == null || allSubtasksByEpic.isEmpty()) {
            Epic epic = (Epic) allTasks.get(epicId);
            epic.setStatus(Status.NEW);
            return;
        }

            for (Subtask sub : allSubtasksByEpic) {
                if (sub.getStatus() != Status.NEW) {
                    allNew = false;
                }
                if (sub.getStatus() != Status.DONE) {
                    allDone = false;
                }
            }

        if (allNew) {
            statusEpic = Status.NEW;
        } else if (allDone) {
            statusEpic = Status.DONE;
        } else {
            statusEpic = Status.IN_PROGRESS;
        }

        Task task = allTasks.get(epicId);
        if (task instanceof Epic) {
            Epic epic = (Epic) task;
            epic.setStatus(statusEpic);
        }

    }
}

