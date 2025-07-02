package manager;

import model.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY_SIZE = 10;
    private final ArrayList<Task> historyList = new ArrayList<>();

    @Override
    public void addToHistory(Task task) {
        if (task == null) {
            return;
        }
        Task taskCopy = new Task(task);
        historyList.add(taskCopy);
        if (historyList.size() > MAX_HISTORY_SIZE) {
            historyList.remove(0);
        }
    }

    @Override
    public ArrayList<Task> getHistory(){
        return new ArrayList<>(historyList);
    }
}
