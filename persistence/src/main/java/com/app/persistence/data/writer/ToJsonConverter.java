package com.app.persistence.data.writer;

public interface ToJsonConverter <T>{
    void to(T t);
}
