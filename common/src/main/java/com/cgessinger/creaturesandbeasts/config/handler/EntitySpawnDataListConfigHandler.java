package com.cgessinger.creaturesandbeasts.config.handler;

import com.cgessinger.creaturesandbeasts.config.EntitySpawnData;
import com.google.common.collect.ImmutableList;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySpawnDataListConfigHandler implements IConfigElementHandler<List<EntitySpawnData>, List<Config>> {
    public static final EntitySpawnDataListConfigHandler INSTANCE = new EntitySpawnDataListConfigHandler();

    private EntitySpawnDataListConfigHandler() {}

    @Override
    public IConfigElement<List<EntitySpawnData>> create(Field field) {
        return new ConfigElement<>(field, this);
    }

    @Override
    public IConfigElement<List<EntitySpawnData>> update(IConfigElement<List<EntitySpawnData>> element, @Nullable List<EntitySpawnData> obj) {
        if (obj != null) {
            element.set(ImmutableList.copyOf(obj));
        }

        return element;
    }

    @Override
    public List<Config> serialize(IConfigElement<List<EntitySpawnData>> element) {
        List<EntitySpawnData> value = element.getFromField();
        if (value == null) {
            value = element.getDefault();
        }
        return value.stream().map(EntitySpawnData::toConfig).collect(ImmutableList.toImmutableList());
    }

    @Override
    public @Nullable List<EntitySpawnData> deserialize(List<Config> obj) {
        if (obj != null) {
            return obj.stream().map(EntitySpawnData::fromConfig).collect(ImmutableList.toImmutableList());
        }
        return null;
    }

    @Override
    public boolean canHandle(Class<?> clazz) {
        return clazz == List.class || List.class.isAssignableFrom(clazz);
    }

}
