package com.app.persistence.data.reader.loader.order.json;

import com.app.persistence.data.reader.loader.FromJsonToObjectLoader;
import com.app.persistence.data.reader.model.OrderData;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class OrderDataJsonLoaderImpl extends FromJsonToObjectLoader<List<OrderData>> implements OrderDataJsonLoader{
    public OrderDataJsonLoaderImpl(Gson gson) {
        super(gson);
    }

    @Override
    public List<OrderData> load(String path) {
        return loadObject(path);
    }
}
