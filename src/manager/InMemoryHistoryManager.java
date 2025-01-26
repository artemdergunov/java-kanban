package manager;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> viewedTasksMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public void add(Task task) {
        if (task != null) {
            linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(viewedTasksMap.get(id));
    }

    private void linkLast(Task task) {
        if (viewedTasksMap.containsKey(task.getId())) {
            removeNode(viewedTasksMap.get(task.getId()));
        }
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(task, tail, null);
        tail = newNode;
        viewedTasksMap.put(task.getId(), newNode);
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new LinkedList<>();
        Node<Task> currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.getData());
            currentNode = currentNode.getNext();
        }
        return tasks;
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.getNext();
            final Node<Task> previous = node.getPrevious();
            node.setData(null);

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node && tail != node) {
                head = next;
                head.setPrevious(null);
            } else if (head != node && tail == node) {
                tail = previous;
                tail.setNext(null);
            } else {
                previous.setNext(next);
                next.setPrevious(previous);
            }
        }
    }
}

