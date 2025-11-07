package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> allTasks = new HashMap<>();
    private HashMap<Integer, Epic> allEpics = new HashMap<>();
    private HashMap<Integer, Subtask> allSubtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int taskCount = 0;

    private int generateId() {
        return ++taskCount;
    }

    public Task createTaskWithManualId(Task task) {
        int idManualTask = task.getId();
        allTasks.put(idManualTask, task);
        if (idManualTask >= taskCount) {
            taskCount = idManualTask + 1;
        }
        return task;
    }

    public Epic createEpicWithManualId(Epic epic) {
        int idManualEpic = epic.getId();
        allEpics.put(idManualEpic, epic);
        if (idManualEpic >= taskCount) {
            taskCount = idManualEpic + 1;
        }
        return epic;
    }

    public Subtask createSubtaskWithManualId(Subtask subtask) {
        int idManualSubtask = subtask.getId();
        int epicId = subtask.getEpicId();

        Epic epic = allEpics.get(epicId);
        if (epic == null) {
            System.out.println("Эпик с ID " + epicId + " не найден.");
            return null;
        }

        if (epicId == idManualSubtask) {
            System.out.println("Subtask не может быть своим же эпиком");
            return null;
        }

        allSubtasks.put(idManualSubtask, subtask);
        if (idManualSubtask >= taskCount) {
            taskCount = idManualSubtask + 1;
        }
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        subtaskIds.add(idManualSubtask);
        updateEpicStatus(epicId);
        return subtask;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        allTasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        allEpics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        int epicId = subtask.getEpicId();

        Epic epic = allEpics.get(epicId);
        if (epic == null) {
            System.out.println("Эпик с ID " + epicId + " не найден.");
            return null;
        }
        int subtaskId = subtask.getId();

        if (epicId == subtaskId) {
            System.out.println("Subtask не может быть своим же эпиком");
            return null;
        }
        allSubtasks.put(subtask.getId(), subtask);
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        subtaskIds.add(subtaskId);
        updateEpicStatus(epicId);

        return subtask;
    }

    @Override
    public void updateTaskById(Task task) {
        int id = task.getId();
        Task existingTask = allTasks.get(id);
        if (existingTask == null) {
            System.out.println("Задача с ID " + id + " не найдена.");
            return;
        }
        allTasks.put(id, task);
        System.out.println("Задача с ID " + id + " успешно обновлена.");
    }

    @Override
    public void updateSubtaskById(Subtask subtask) {
        int id = subtask.getId();
        Subtask existingSubtask = allSubtasks.get(id);
        if (existingSubtask == null) {
            System.out.println("\nЗадача с ID " + id + " не найдена.");
            return;
        }
        int epicId = existingSubtask.getEpicId();
        allSubtasks.put(id, subtask);
        updateEpicStatus(epicId);
        System.out.println("\nПодзадача с ID " + id + " успешно обновлена.");
    }

    @Override
    public void updateEpicById(Epic epic) {
        int id = epic.getId();
        Epic existingEpic = allEpics.get(id);
        if (existingEpic == null) {
            System.out.println("\nЗадача с ID " + id + " не найдена.");
            return;
        }
        Status currentStatus = existingEpic.getStatus();
        epic.setStatus(currentStatus);
        ArrayList<Integer> subtaskIds = existingEpic.getSubtaskIds();
        epic.setSubtaskIds(subtaskIds);
        allEpics.put(id, epic);
        System.out.println("\nЭпик с ID " + id + " успешно обновлен.");
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(allEpics.values());

    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(allSubtasks.values());

    }

    @Override
    public void deleteAllTasks() {
        List<Integer> taskKeySet = new ArrayList<>(allTasks.keySet());
        for (Integer id : taskKeySet) {
            historyManager.removeFromHistory(id);
        }
        allTasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        List<Integer> epicKeySet = new ArrayList<>(allEpics.keySet());
        for (Integer id : epicKeySet) {
            historyManager.removeFromHistory(id);
        }
        deleteAllSubtasks();
        allEpics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        List<Integer> subtaskKeySet = new ArrayList<>(allSubtasks.keySet());
        for (Integer id : subtaskKeySet) {
            historyManager.removeFromHistory(id);
        }
        allSubtasks.clear();
        for (Epic epic : allEpics.values()) {
            epic.getSubtaskIds().clear();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        allTasks.remove(id);
        historyManager.removeFromHistory(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = allEpics.get(id);
        ArrayList<Subtask> subtasksToRemove = getAllSubtasksByEpic(id);
        for (Subtask subtask : subtasksToRemove) {
            allSubtasks.remove(subtask.getId());
        }
        allEpics.remove(id);
        historyManager.removeFromHistory(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = allSubtasks.get(id);
        int epicId = subtask.getEpicId();
        allSubtasks.remove(id);
        updateEpicStatus(epicId);
        historyManager.removeFromHistory(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task getTaskById(int id) {
        Task taskById = allTasks.get(id);
        historyManager.addToHistory(taskById);
        return taskById;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epicById = allEpics.get(id);
        historyManager.addToHistory(epicById);
        return epicById;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtaskById = allSubtasks.get(id);
        historyManager.addToHistory(subtaskById);
        return subtaskById;
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksByEpic(int epicId) {
        ArrayList<Subtask> allSubtasksByEpic = new ArrayList<>();
        Epic epic = allEpics.get(epicId);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = allSubtasks.get(subtaskId);
                if (subtask != null) {
                    allSubtasksByEpic.add(subtask);
                }
            }
        }
        return allSubtasksByEpic;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = allEpics.get(epicId);
        if (epic == null) {
            System.out.println("\nЗадача с ID " + epicId + " не найдена.");
            return;
        }
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
                return;
            }
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

