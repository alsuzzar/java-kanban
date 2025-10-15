package manager;

import model.Node;
import model.Task;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public class InMemoryHistoryManager implements HistoryManager
{

    private final Map<Integer, Node> historyMap = new HashMap<>();
    private Node head = null;
    private Node tail = null;


    @Override
    public void addToHistory(Task task)
    {
        if (task == null)
        {
            return;
        }
        if (historyMap.containsKey(task.getId()))
        {
            removeNode(historyMap.get(task.getId()));
        }
        Node node = addNode(task);
        historyMap.put(task.getId(), node);
    }

    @Override
    public ArrayList<Task> getHistory()
    {
        ArrayList<Task> historyList = new ArrayList<>();
        Node current = head;
        while (current != null)
        {
            historyList.add(current.task);
            current = current.next;
        }
        return historyList;
    }

    public Node addNode(Task task)
    {
        Node newNode = new Node(task);
        if (head == null && tail == null)
        {
            head = newNode;
            tail = newNode;
        } else
        {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        return newNode;
    }

    @Override
    public void removeFromHistory(int id)
    {
        Node node = historyMap.get(id);
        if (node != null)
        {
            removeNode(node);
        }
    }

    public void removeNode(Node node)
    {
        if (node.prev != null)
        {
            node.prev.next = node.next;
        } else
        {
            head = node.next;
        }
        if (node.next != null)
        {
            node.next.prev = node.prev;
        } else
        {
            tail = node.prev;
        }
        historyMap.remove(node.task.getId());
    }


}
