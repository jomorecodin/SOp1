package com.microsat.rtos.datastructures;

/**
 * Implementación de una cola (Queue) genérica utilizando una lista enlazada.
 * Sigue el principio FIFO (First-In, First-Out).
 *
 * @param <T> El tipo de dato que almacenará la cola.
 */
public class CustomQueue<T> {

    private Node<T> head; // Frente de la cola
    private Node<T> tail; // Final de la cola
    private int size;

    /**
     * Constructor que inicializa una cola vacía.
     */
    public CustomQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param data El dato a encolar.
     */
    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    /**
     * Remueve y devuelve el elemento al frente de la cola.
     * @return El dato al frente de la cola, o null si la cola está vacía.
     */
    public T dequeue() {
        if (isEmpty()) {
            return null; // O lanzar una excepción como NoSuchElementException
        }
        T data = head.getData();
        head = head.getNext();
        if (head == null) {
            tail = null; // La cola quedó vacía
        }
        size--;
        return data;
    }

    /**
     * Verifica si la cola está vacía.
     * @return true si la cola no tiene elementos.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Devuelve el número de elementos en la cola.
     * @return El tamaño actual de la cola.
     */
    public int size() {
        return size;
    }
}
