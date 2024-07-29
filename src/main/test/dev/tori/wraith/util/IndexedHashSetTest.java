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

package dev.tori.wraith.util;

import dev.tori.wraith.util.IndexedHashSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.0.0
 */
public class IndexedHashSetTest {

    @Test
    public void testNoDuplicatesAllowed() {
        IndexedHashSet<String> set = new IndexedHashSet<>();
        set.add("A");
        set.add("A");
        set.add("B");

        Assertions.assertEquals(2, set.size());
    }

    @Test
    public void testInsert() {
        IndexedHashSet<String> set = new IndexedHashSet<>() {{
            add("A");
            add("B");
            add("C");
        }};
        set.add(0, "Z");

        Assertions.assertEquals("Z", set.get(0));
        Assertions.assertEquals("A", set.get(1));
        Assertions.assertEquals("B", set.get(2));
        Assertions.assertEquals("C", set.get(3));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> set.get(4));
    }

    @Test
    public void testIndexBounds() {
        IndexedHashSet<String> set = new IndexedHashSet<>() {{
            add("A");
            add("B");
            add("C");
        }};

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> set.get(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> set.get(3));
    }

    @Test
    public void testRemoveIf() {
        IndexedHashSet<String> set = new IndexedHashSet<>() {{
            add("A");
            add("B");
            add("C");
            add("a");
            add("b");
            add("c");
        }};
        set.removeIf(s -> Objects.equals(s, s.toUpperCase()));

        Assertions.assertEquals(3, set.size());
        Assertions.assertEquals("a", set.get(0));
        Assertions.assertEquals("b", set.get(1));
        Assertions.assertEquals("c", set.get(2));
    }
}