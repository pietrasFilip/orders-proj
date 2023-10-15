package com.app.persistence.data.reader.loader.order.txt;

import com.app.persistence.data.reader.loader.exception.TxtLoaderException;
import com.app.persistence.data.reader.model.CustomerData;
import com.app.persistence.data.reader.model.OrderData;
import com.app.persistence.data.reader.model.ProductData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Long.valueOf;

@Component
public class OrderDataTxtLoaderImpl implements OrderDataTxtLoader{

    @Override
    public List<OrderData> load(String path) {
        try (var lines = Files.lines(Paths.get(path))){
            return lines
                    .map(line -> {
                        var items = line.split(";");
                        return new OrderData(
                                valueOf(items[0]),
                                CustomerData.of(valueOf(items[1]), items[2], items[3], parseInt(items[4]), items[5]),
                                ProductData.of(valueOf(items[6]), items[7], new BigDecimal(items[8]), items[9]),
                                parseInt(items[10]), items[11]
                        );
                    }).toList();
        } catch (Exception e) {
            throw new TxtLoaderException(e.getMessage());
        }
    }
}
