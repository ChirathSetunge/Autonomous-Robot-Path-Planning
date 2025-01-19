public class CustomPriorityQueue<T> {

    private T[] data; // Array to store elements
    private int size; // Current number of elements in the queue

    public CustomPriorityQueue() {
        this.data = (T[]) new Object[16]; // Initial size
        this.size = 0;
    }
    //Getters and Setters

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    // method to resize the array
    private void resize(int newCapacity) {
        T[] newData = (T[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    // Offer a new element to the queue
    public void offer(T element) {
        if (size == data.length) {
            resize(data.length * 2); // Double the size when full
        }
        data[size] = element;
        size++;
        // Sift up the newly added element to maintain priority order
        siftUp(size - 1);
    }

    // Sift up a specific element to maintain min-heap property (lower F-score at top)
    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (compare(data[index], data[parentIndex]) >= 0) {
                break; // Reached correct position (child F-score is greater than or equal to parent)
            }
            // Swap element with its parent (lower F-score goes up)
            T temp = data[index];
            data[index] = data[parentIndex];
            data[parentIndex] = temp;
            index = parentIndex;
        }
    }

    // Poll (remove and return) the element with the highest priority (lowest F-score)
    public T poll() {
        if (isEmpty()) {
            return null;
        }
        T element = data[0]; // Top element has the highest priority
        data[0] = data[size - 1]; // Move the last element to the top
        size--;
        // Sift down the element at the top to maintain priority order
        siftDown(0);
        return element;
    }

    // Sift down an element to maintain min-heap property
    private void siftDown(int index) {
        while (2 * index + 1 < size) { // While there is at least a left child
            int childIndex = 2 * index + 1;
            if (childIndex + 1 < size && compare(data[childIndex], data[childIndex + 1]) > 0) {
                // Right child has lower F-score, so consider it instead
                childIndex++;
            }
            if (compare(data[index], data[childIndex]) <= 0) {
                break; // Reached correct position (parent F-score is less than or equal to child)
            }
            // Swap element with its child with lower F-score
            T temp = data[index];
            data[index] = data[childIndex];
            data[childIndex] = temp;
            index = childIndex;
        }
    }

    // Define a comparison function to compare elements based on their priority (F-score)
    private int compare(T a, T b) {
        return Double.compare(((Cell) a).getFScore(), ((Cell) b).getFScore());
    }
    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(element)) {
                return true;
            }
        }
        return false;
    }
}
