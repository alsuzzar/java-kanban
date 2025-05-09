package model;

public class Subtask extends Task {
    private int epicId;

    public Subtask() {
        super();
    }


    public Subtask(Subtask otherSubtask) {
        this.id = otherSubtask.id;
        this.name = otherSubtask.name;
        this.description = otherSubtask.description;
        this.status = otherSubtask.status;
        this.epicId = otherSubtask.epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}