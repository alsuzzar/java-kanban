import java.util.ArrayList;
public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;

    public Epic(String name, String description, int id, Status status, ArrayList<Integer> subtaskIds) {
        super(name, description, id, status);
        this.subtaskIds = subtaskIds;
    }

    public ArrayList<Integer> getSubtaskIds() {

        return subtaskIds;
    }
}
