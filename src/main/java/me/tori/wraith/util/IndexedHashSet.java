/*
 * Copyright (c) 2024 7orivorian.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package me.tori.wraith.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Predicate;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.0.0
 */
public class IndexedHashSet<E> {

    private final ArrayList<E> list;
    private final HashSet<E> set;

    public IndexedHashSet() {
        this.list = new ArrayList<>();
        this.set = new HashSet<>();
    }

    public boolean add(E element) {
        if (set.add(element)) {
            list.add(element);
            return true;
        }
        return false;
    }

    public boolean add(int index, E element) {
        if (set.add(element)) {
            list.add(index, element);
            return true;
        }
        return false;
    }

    public E get(int index) {
        return list.get(index);
    }

    public E remove(int index) {
        E element = list.remove(index);
        set.remove(element);
        return element;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean remove(Object element) {
        if (set.remove(element)) {
            list.remove(element);
            return true;
        }
        return false;
    }

    public boolean removeIf(Predicate<? super E> filter) {
        boolean removed = false;
        Iterator<E> it = list.iterator();
        while (it.hasNext()) {
            E element = it.next();
            if (filter.test(element)) {
                it.remove();
                set.remove(element);
                removed = true;
            }
        }
        return removed;
    }

    public int size() {
        return list.size();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean contains(Object element) {
        return set.contains(element);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public ListIterator<E> listIterator(int index) {
        return list.listIterator(index);
    }

    public void clear() {
        list.clear();
        set.clear();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}