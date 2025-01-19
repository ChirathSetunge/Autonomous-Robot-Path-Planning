public class CustomArrayList<T> {

    private T[] data;  // Array to store elements
    private int size;   // Current size of the list

    public CustomArrayList() {
        // Initial capacity is set to 10
        data = (T[]) new Object[10];
        size = 0;
    }
    public int size() {
        return size;
    }
    public void add(T element) {
        // Check if capacity needs to be increased
        if (size == data.length) {
            // Resize the data array
            T[] newData = (T[]) new Object[data.length * 2];
            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }

        // Add the element to the end of the array
        data[size++] = element;
    }
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        // Shift elements to the left to overwrite the removed element
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        // Decrement size to shrink the array to save the memory
        size--;

    }
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return data[index];
    }
}