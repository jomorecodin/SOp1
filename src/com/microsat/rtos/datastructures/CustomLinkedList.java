package com.microsat.rtos.datastructures;

/**
 * Implementación de una lista enlazada simple genérica.
 * @param <T> El tipo de dato que almacenará la lista.
 */
public class CustomLinkedList<T> {

    private Node<T> head;
    private int size;

    /**
     * Constructor que inicializa una lista vacía.
     */
    public CustomLinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Agrega un elemento al final de la lista.
     * @param data El dato a agregar.
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }

    /**
     * Verifica si la lista está vacía.
     * @return true si la lista no contiene elementos.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Devuelve el número de elementos en la lista.
     * @return El tamaño de la lista.
     */
    public int size() {
        return size;
    }

    /**
     * Devuelve el elemento en la posición especificada en esta lista.
     * @param index índice del elemento a devolver.
     * @return el elemento en la posición especificada.
     * @throws IndexOutOfBoundsException si el índice está fuera del rango (index < 0 || index >= size()).
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    // Aquí se podrían agregar más métodos como remove, etc. según se necesite.
}
