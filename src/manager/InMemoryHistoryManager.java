package manager;

import model.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> historyList = new ArrayList<>();

    @Override
    public void addToHistory(Task task) {
        Task taskCopy = new Task(task);
        historyList.add(taskCopy);
        if (historyList.size() > 10) {
                historyList.remove(0);
            }
        }

    @Override
    public ArrayList<Task> getHistory(){
        return new ArrayList<>(historyList);
    }
}
