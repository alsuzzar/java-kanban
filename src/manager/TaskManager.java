package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private HashMap<Integer, Task> allTasks = new HashMap<>();

    private int taskCount = 0;

    private int generateId() {
        return ++taskCount;
    }
    public void updateTaskById(int id, Task task) {
        Task existingTask = allTasks.get(id);
        if (existingTask == null) {
            System.out.println("Задача с ID " + id + " не найдена.");
            return;
        } else {
            Task updatedTask = new Task(task);
            allTasks.put(id, updatedTask);
        }

        System.out.println("Задача с ID " + id + " успешно обновлена.");
    }

    public void updateSubtaskById(int id, Subtask subtask) {
        Task existingTask = allTasks.get(id);
        if (existingTask == null) {
            System.out.println("\nЗадача с ID " + id + " не найдена.");
            return;
        } else {
            Subtask existingSubtask = (Subtask) existingTask;
            int epicId = existingSubtask.getEpicId();
            Subtask updatedSubtask = new Subtask(subtask);
            allTasks.put(id, updatedSubtask);
            updateEpicStatus(epicId);
        }
        System.out.println("\nПодзадача с ID " + id + " успешно обновлена.");
    }

    public void updateEpicById(int id, Epic epic) {
        Task existingTask = allTasks.get(id);
        if (existingTask == null) {
            System.out.println("\nЗадача с ID " + id + " не найдена.");
            return;
        } else {
            Epic existingEpic = (Epic) existingTask;
            Status currentStatus = existingEpic.getStatus();
            ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
            Epic updatedEpic = new Epic(epic);
            allTasks.put(id, updatedEpic);
        }
        System.out.println("\nЭпик с ID " + id + " успешно обновлен.");
    }


    public Task createTask(Task task) {
        task.setId(generateId());
        allTasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epic.setSubtaskIds(new ArrayList<>());
        allTasks.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Task task = allTasks.get(epicId);
        if (task == null || !(task instanceof Epic)) {
            System.out.println("Эпик с ID " + epicId + " не найден.");
            return null;
        }
        subtask.setId(generateId());
        allTasks.put(subtask.getId(), subtask);
        Epic epic = (Epic) task;

        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        int subtaskId = subtask.getId();
        subtaskIds.add(subtaskId);

        updateEpicStatus(epicId);
        return subtask;
    }

    public void deleteAllTasks() {
        allTasks.clear();
    }

    public void deleteAllTaskList() {
        for (Task task : getAllTasks()) {
            allTasks.remove(task.getId());
        }
    }

    public void deleteAllEpicsList() {
        for (Epic epic : getAllEpics()) {
            allTasks.remove(epic.getId());
        }
    }

    public void deleteAllSubtaskList() {
        for (Subtask subtask : getAllSubtasks()) {
            allTasks.remove(subtask.getId());
        }
    }

    public void deleteTaskById(int id) {
            allTasks.remove(id);
    }

    public void deleteEpicById(int id) {
        Task task = allTasks.get(id);
        if (task instanceof Epic) {
            ArrayList<Subtask>subtasksToRemove = getAllSubtasksByEpic(id);
            for (Subtask subtask : subtasksToRemove) {
                allTasks.remove(subtask.getId());
            }
            allTasks.remove(id);
        }
    }

    public void deleteSubtaskById(int id) {
        Task task = allTasks.get(id);
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            int epicId = subtask.getEpicId();
            allTasks.remove(id);
            updateEpicStatus(epicId);
        }
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
        ArrayList<Subtask> allSubtasksList = new ArrayList<>();

        for (Task task : allTasks.values()) {
            if (task instanceof Subtask) {
                Subtask subtask = (Subtask) task;
                    allSubtasksList.add(subtask);
                }
            }
        return allSubtasksList;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpicsList = new ArrayList<>();

        for (Task task : allTasks.values()) {
            if (task instanceof Epic) {
                Epic epic = (Epic) task;
                allEpicsList.add(epic);
            }
        }
        return allEpicsList;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasksList = new ArrayList<>();

        for (Task task : allTasks.values()) {
            if (!(task instanceof Epic) && !(task instanceof Subtask)) {
                allTasksList.add(task);
            }
        }
        return allTasksList;
    }

    private void updateEpicStatus(int epicId) {
        Task task = allTasks.get(epicId);
        if (task == null) {
            System.out.println("\nЗадача с ID " + epicId + " не найдена.");
            return;
            }
        if (task instanceof Epic) {
            Epic epic = (Epic) task;
            ArrayList<Subtask> allSubtasksByEpic = getAllSubtasksByEpic(epicId);
            Status statusEpic;
            boolean allNew = true;
            boolean allDone = true;

            if (allSubtasksByEpic.isEmpty()) {
                epic.setStatus(Status.NEW);
                return;
            }

            for (Subtask sub : allSubtasksByEpic) {
                if (sub.getStatus() == Status.IN_PROGRESS) {
                    epic.setStatus(Status.IN_PROGRESS);
                    return;                }
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
            epic.setStatus(statusEpic);
        }
    }

    }

