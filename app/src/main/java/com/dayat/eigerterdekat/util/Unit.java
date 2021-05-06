package com.dayat.eigerterdekat.util;


// ini adalah interface yg dijadikan sebagai
// template untuk object callback
public interface Unit<T> {

    // fungsi invokasi
    public void invoke(T o);
}
