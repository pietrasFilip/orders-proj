package com.app.persistence.data.reader.loader.repository.db.generic;

import com.app.persistence.model.user.Role;
import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import org.atteo.evo.inflector.English;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public abstract class AbstractCrudRepository<T, ID> implements CrudRepository<T, ID> {
    protected final Jdbi jdbi;

    @SuppressWarnings("unchecked")
    private final Class<T> entityType =
            (Class<T>) ((ParameterizedType) super.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Override
    public Optional<T> save(T t) {
        try {
            var insertedRow = new AtomicReference<Optional<T>>(Optional.empty());
            jdbi.useTransaction(handle -> {
                var sql = "insert into %s %s values %s;".formatted(
                        getTableName(),
                        getColumnNamesForInsert(),
                        getValuesForInsert(t));
                var insertedRowCount = jdbi.withHandle(h -> h.execute(sql));
                if (insertedRowCount > 0) {
                    insertedRow.set(findLast(1).stream().findFirst());
                }
            });
            return insertedRow.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public List<T> saveAll(List<T> items) {
        var insertedRows = new AtomicReference<List<T>>(List.of());
        jdbi.useTransaction(handle -> {
            var sql = "insert into %s %s values %s;".formatted(
                    getTableName(),
                    getColumnNamesForInsert(),
                    items
                            .stream()
                            .map(this::getValuesForInsert)
                            .collect(Collectors.joining(", "))
            );
            var insertedRowCount = jdbi.withHandle(h -> h.execute(sql));
            if (insertedRowCount > 0) {
                insertedRows.set(findLast(insertedRowCount));
            }
        });
        return insertedRows.get();
    }

    @Override
    public Optional<T> update(ID id, T item) {
        var sql = "update %s set %s where %s;".formatted(
                getTableName(),
                getColumnsAndValuesForUpdate(item),
                whereSection(id));
        var updatedRow = jdbi.withHandle(handle -> handle
                .execute(sql));

        if (updatedRow == 0) {
            return Optional.empty();
        }
        return findById(id);
    }

    @Override
    public List<T> updateAll(List<T> items) {
        var updatedRows = new AtomicReference<List<T>>(List.of());
        jdbi.useTransaction(handle -> {
            var batch = handle.createBatch();
            items.forEach(item -> batch.add(
                    "update %s set %s %s;".formatted(
                            getTableName(),
                            getColumnsAndValuesForUpdate(item),
                            whereSectionForSpecifiedObject(item)
                    )));
            batch.execute();
        });
        return updatedRows.get();
    }

    @Override
    public Optional<T> findById(ID id) {
        var sql = "select * from %s where %s;".formatted(
                getTableName(),
                whereSection(id)
        );

        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .mapToBean(entityType)
                .findFirst()
        );
    }

    @Override
    public List<T> findLast(int n) {
        var sql = "select * from %s order by id desc limit %d;".formatted(getTableName(), n);
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .mapToBean(entityType)
                .list()
        );
    }

    @Override
    public List<T> findAll() {
        var sql = "select * from %s;".formatted(getTableName());
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .mapToBean(entityType)
                .list()
        );
    }

    @Override
    public Optional<T> deleteById(ID id) {
        var deletedRow = new AtomicReference<Optional<T>>(Optional.empty());
        jdbi.useTransaction(handle ->
                findById(id)
                        .ifPresent(toDelete -> {
                            var sql = "delete from %s %s".formatted(
                                    getTableName(),
                                    whereSection(id)
                            );
                            var deletedRowCount = jdbi.withHandle(h -> h.execute(sql));
                            if (deletedRowCount > 0) {
                                deletedRow.set(Optional.of(toDelete));
                            }
                        })
        );
        return deletedRow.get();
    }

    @Override
    public List<T> deleteAll() {
        return null;
    }

    // PRIVATE AUXILIARY METHODS

    protected String getTableName() {
        var entityName = entityType.getSimpleName().replace("DataDb", "");
        return English.plural(CaseFormat.UPPER_CAMEL.to(
                CaseFormat.LOWER_UNDERSCORE, entityName
        ));
    }

    private static String getFieldName(String fieldName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
    }

    private String getColumnNamesForInsert() {
        var cols = Arrays
                .stream(entityType.getDeclaredFields())
                .map(field -> getFieldName(field.getName()))
                .filter(fieldName -> !fieldName.equalsIgnoreCase("id"))
                .collect(Collectors.joining(", "));
        return "( %s )".formatted(cols);
    }

    private String getValuesForInsert(T t) {
        var values = Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> !field.getName().equalsIgnoreCase("id"))
                .map(field -> {
                    try {
                        field.setAccessible(true);

                        if (field.get(t) == null) {
                            return "NULL";
                        }
                        if (List.of(String.class, Role.class, LocalDate.class).contains(field.getType())) {
                            return "'%s'".formatted(field.get(t));
                        }

                        return field.get(t).toString();

                    } catch (Exception e) {
                        throw new IllegalStateException(e.getMessage());
                    }
                }).collect(Collectors.joining(", "));
        return "( %s )".formatted(values);
    }

    protected String getColumnsAndValuesForUpdate(T t) {
        var values = Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> {
                    try {
                        field.setAccessible(true);
                        return !getFieldName(field.getName()).equalsIgnoreCase("id") && field.get(t) != null;
                    } catch (Exception e) {
                        throw new IllegalStateException(e.getMessage());
                    }
                })
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        if (List.of(String.class, Role.class, LocalDate.class).contains(field.getType())) {
                            return "%s = '%s'".formatted(getFieldName(field.getName()), field.get(t));
                        }
                        return getFieldName(field.getName()) + " = " + field.get(t);

                    } catch (Exception e) {
                        throw new IllegalStateException(e.getMessage());
                    }
                }).collect(Collectors.joining(", "));
        return "%s".formatted(values);
    }

    private String whereSection(ID id) {
        if (!id.toString().matches("\\d+")) {
            throw new IllegalStateException("Id is not correct");
        }
        return " id = %s".formatted(id);
    }

    protected String whereSectionForSpecifiedObject(T t) {
        var id = Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> {
                    try {
                        field.setAccessible(true);
                        return field.getName().equalsIgnoreCase("id");
                    } catch (Exception e) {
                        throw new IllegalStateException(e.getMessage());
                    }
                })
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        return field.get(t);
                    } catch (Exception e) {
                        throw new IllegalStateException(e.getMessage());
                    }
                })
                .findFirst()
                .orElseThrow()
                .toString();
        return "where id = %s".formatted(id);
    }
}
