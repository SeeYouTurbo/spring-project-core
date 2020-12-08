package com.king.common.util;

import org.springframework.lang.Nullable;

import java.io.Writer;

public class FastStringWriter extends Writer {
    private StringBuilder builder;

    public FastStringWriter() {
        this.builder = new StringBuilder(64);
    }

    public FastStringWriter(final int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Negative builderfer size");
        } else {
            this.builder = new StringBuilder(capacity);
        }
    }

    public FastStringWriter(@Nullable final StringBuilder builder) {
        this.builder = builder != null ? builder : new StringBuilder(64);
    }

    public StringBuilder getBuilder() {
        return this.builder;
    }

    @Override
    public void write(int c) {
        this.builder.append((char) c);
    }

    @Override
    public void write(char[] cbuilder, int off, int len) {
        if (off >= 0 && off <= cbuilder.length && len >= 0 && off + len <= cbuilder.length && off + len >= 0) {
            if (len != 0) {
                this.builder.append(cbuilder, off, len);
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void write(String str) {
        this.builder.append(str);
    }

    @Override
    public void write(String str, int off, int len) {
        this.builder.append(str.substring(off, off + len));
    }

    @Override
    public FastStringWriter append(CharSequence csq) {
        if (csq == null) {
            this.write("null");
        } else {
            this.write(csq.toString());
        }

        return this;
    }

    @Override
    public FastStringWriter append(CharSequence csq, int start, int end) {
        CharSequence cs = csq == null ? "null" : csq;
        this.write(((CharSequence) cs).subSequence(start, end).toString());
        return this;
    }

    @Override
    public FastStringWriter append(char c) {
        this.write(c);
        return this;
    }

    @Override
    public String toString() {
        return this.builder.toString();
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
        this.builder.setLength(0);
        this.builder.trimToSize();
    }
}
