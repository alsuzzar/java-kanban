package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic() {
        super();
        this.subtaskIds = new ArrayList<>();
    }

    public Epic(Epic otherEpic) {
        this.id = otherEpic.id;
        this.name = otherEpic.name;
        this.description = otherEpic.description;
        this.status = otherEpic.status;
        this.subtaskIds = new ArrayList<>(otherEpic.subtaskIds);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }
}
