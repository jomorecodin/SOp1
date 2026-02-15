package com.microsat.rtos.datastructures;

/**
 * Representa un nodo genérico para ser usado en estructuras de datos enlazadas.
 * @param <T> El tipo de dato almacenado en el nodo.
 */
public class Node<T> {

    /**
     * El dato contenido en este nodo.
     */
    private T data;

    /**
     * Referencia al siguiente nodo en la estructura.
     */
    private Node<T> next;

    /**
     * Constructor para crear un nuevo nodo.
     * @param data El dato a almacenar en el nodo.
     */
    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    // --- Getters y Setters ---

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}
