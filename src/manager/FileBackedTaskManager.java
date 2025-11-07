package manager;

import model.Epic;
import model.Subtask;
import model.Status;
import model.Type;
import model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    protected File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public FileBackedTaskManager() {
        this(new File("data.csv"));
    }

    @Override
    public Task createTaskWithManualId(Task task) {
        Task createdTask = super.createTaskWithManualId(task);
        save();
        return createdTask;
    }

    @Override
    public Epic createEpicWithManualId(Epic epic) {
        Epic createdEpic = super.createEpicWithManualId(epic);
        save();
        return createdEpic;
    }

    @Override
    public Subtask createSubtaskWithManualId(Subtask subtask) {
        Subtask createdSubtask = super.createSubtaskWithManualId(subtask);
        save();
        return createdSubtask;
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        save();
        return createdTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        save();
        return createdEpic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask createdSubtask = super.createSubtask(subtask);
        save();
        return createdSubtask;
    }

    private void addTaskWithoutSave(Task task) {
        super.createTaskWithManualId(task);
    }

    private void addEpicWithoutSave(Epic epic) {
        super.createEpicWithManualId(epic);
    }

    private void addSubtaskWithoutSave(Subtask subtask) {
        super.createSubtaskWithManualId(subtask);
    }


    @Override
    public void updateTaskById(Task task) {
        super.updateTaskById(task);
        save();
    }

    @Override
    public void updateEpicById(Epic epic) {
        super.updateEpicById(epic);
        save();
    }

    @Override
    public void updateSubtaskById(Subtask subtask) {
        super.updateSubtaskById(subtask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    public String toString(Task task) {
        if (task instanceof Subtask) {
            return task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus() + "," + task.getDescription() + "," + ((Subtask) task).getEpicId();
        } else {
            return task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus() + "," + task.getDescription() + "";
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                Task task = manager.fromString(line);

                if (task instanceof Epic) {
                    manager.addEpicWithoutSave((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addSubtaskWithoutSave((Subtask) task);
                } else if (task instanceof Task) {
                    manager.addTaskWithoutSave(task);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return manager;
    }

    public Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        Type type = Type.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        if (parts[1] == null || parts[1].equals("null")) {
            throw new IllegalArgumentException("Тип задачи не может быть null");
        }
        if (type == Type.TASK) {
            Task task = new Task();
            task.setId(id);
            task.setName(name);
            task.setStatus(status);
            task.setDescription(description);
            return task;

        } else if (type == Type.EPIC) {
            Epic epic = new Epic();
            epic.setId(id);
            epic.setName(name);
            epic.setStatus(status);
            epic.setDescription(description);
            return epic;

        } else if (type == Type.SUBTASK) {
            int epicId = Integer.parseInt(parts[5]);
            Subtask subtask = new Subtask();
            subtask.setId(id);
            subtask.setName(name);
            subtask.setStatus(status);
            subtask.setDescription(description);
            subtask.setEpicId(epicId);
            return subtask;
        }
        return null;
    }
}