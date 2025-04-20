package backend.models;

import java.util.Objects;

public class LimitLinkedList implements Comparable<LimitLinkedList> {
    public Limit getHead() {
        return head;
    }

    public Limit getTail() {
        return tail;
    }

    private Limit head = null;
    private Limit tail = null;

    public LimitLinkedList(Limit head) {
        this.head = head;
        this.tail = head;
    }

    public long hashPrice() {
        return head.scalePrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LimitLinkedList that = (LimitLinkedList) o;
        return hashPrice() == that.hashPrice();
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashPrice());
    }

    @Override
    public int compareTo(LimitLinkedList limit) {
        return head.compareTo(limit.head);
    }

    public void insert(Limit limit) {
        tail.next = limit;
        limit.prev = tail;
        tail = limit;
    }

    public void delete(Limit limit) {
        if(head==null) return;
        if(head==tail) {
            head = tail = null;
            return;
        }
        if(limit==head) {
            head.next.prev = null;
            head = head.next;
            return;
        }
        if(limit==tail) {
            tail.prev.next = null;
            tail = tail.prev;
            return;
        }
        limit.prev.next = limit.next;
        limit.next.prev = limit.prev;
    }

    public int size() {
        int sz = 0;
        Limit cur = head;
        while(cur != null) {
            sz++;
            cur = cur.next;
        }
        return sz;
    }
}
