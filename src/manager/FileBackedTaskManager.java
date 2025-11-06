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

    protected File file = new File("data.csv");

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    @Override
    public Task createTaskWithManualId(Task task) {
        Task createdTask = super.createTaskWithManualId(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return createdTask;
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return createdTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return createdEpic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask createdSubtask = super.createSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return createdSubtask;
    }

    @Override
    public void updateTaskById(Task task) {
        super.updateTaskById(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEpicById(Epic epic) {
        super.updateEpicById(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSubtaskById(Subtask subtask) {
        super.updateSubtaskById(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
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
                    manager.createEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.createSubtask((Subtask) task);
                } else if (task instanceof Task) {
                    manager.createTask(task);
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
            task.setType(type);
            return task;

        } else if (type == Type.EPIC) {
            Epic epic = new Epic();
            epic.setId(id);
            epic.setName(name);
            epic.setStatus(status);
            epic.setDescription(description);
            epic.setType(type);
            return epic;

        } else if (type == Type.SUBTASK) {
            int epicId = Integer.parseInt(parts[5]);
            Subtask subtask = new Subtask();
            subtask.setId(id);
            subtask.setName(name);
            subtask.setStatus(status);
            subtask.setDescription(description);
            subtask.setType(type);
            subtask.setEpicId(epicId);
            return subtask;
        }
        return null;
    }
}