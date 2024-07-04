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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.0.0
 */
public class IndexedHashSet<E> extends ArrayList<E> {

    private final HashMap<E, Boolean> map;

    public IndexedHashSet() {
        this.map = new HashMap<>();
    }

    @Override
    public boolean add(E element) {
        Objects.requireNonNull(element);
        if (map.put(element, true) == null) {
            super.add(element);
            return true;
        }
        return false;
    }

    @Override
    public void add(int index, E element) {
        Objects.requireNonNull(element);
        rangeCheckForAdd(index);
        if (map.put(element, true) == null) {
            super.add(index, element);
        }
    }

    @Override
    public E remove(int index) {
        E element = super.remove(index);
        map.remove(element);
        return element;
    }

    @Override
    public boolean remove(Object element) {
        if (map.remove(element) != null) {
            super.remove(element);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E element = it.next();
            if (filter.test(element)) {
                it.remove();
                map.remove(element);
                removed = true;
            }
        }
        return removed;
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean contains(Object element) {
        return map.containsKey(element);
    }

    @Override
    public void clear() {
        super.clear();
        map.clear();
    }

    public ArrayList<E> asList() {
        return new ArrayList<>(this);
    }

    private void rangeCheckForAdd(int index) {
        if ((index > size()) || (index < 0)) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size();
    }
}