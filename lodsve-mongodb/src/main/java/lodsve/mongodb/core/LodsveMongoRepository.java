/*
 * Copyright 2010-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.mongodb.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Repository base implementation for Mongo.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Thomas Darimont
 */
public class LodsveMongoRepository<T, ID extends Serializable> implements MongoRepository<T, ID> {
    private MongoEntityInformation<T, ID> entityInformation;
    @Autowired
    protected MongoOperations mongoOperations;

    public void setEntityInformation(MongoEntityInformation<T, ID> entityInformation) {
        this.entityInformation = entityInformation;
    }

    @Override
    public <S extends T> S save(S entity) {
        Assert.notNull(entity, "Entity must not be null!");

        if (entityInformation.isNew(entity)) {
            mongoOperations.insert(entity, entityInformation.getCollectionName());
        } else {
            mongoOperations.save(entity, entityInformation.getCollectionName());
        }

        return entity;
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");

        List<S> result = convertIterableToList(entities);
        boolean allNew = true;

        for (S entity : entities) {
            if (allNew && !entityInformation.isNew(entity)) {
                allNew = false;
            }
        }

        if (allNew) {
            mongoOperations.insertAll(result);
        } else {
            for (S entity : result) {
                save(entity);
            }
        }

        return result;
    }

    @Override
    public T findOne(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        return mongoOperations.findById(id, entityInformation.getJavaType(), entityInformation.getCollectionName());
    }

    private Query getIdQuery(Object id) {
        return new Query(getIdCriteria(id));
    }

    private Criteria getIdCriteria(Object id) {
        return where(entityInformation.getIdAttribute()).is(id);
    }

    @Override
    public boolean exists(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        return mongoOperations.exists(getIdQuery(id), entityInformation.getJavaType(),
                entityInformation.getCollectionName());
    }

    @Override
    public long count() {
        return mongoOperations.getCollection(entityInformation.getCollectionName()).count();
    }

    @Override
    public void delete(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        mongoOperations.remove(getIdQuery(id), entityInformation.getJavaType(), entityInformation.getCollectionName());
    }

    @Override
    public void delete(T entity) {
        Assert.notNull(entity, "The given entity must not be null!");
        delete(entityInformation.getId(entity));
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");

        for (T entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        mongoOperations.remove(new Query(), entityInformation.getCollectionName());
    }

    @Override
    public List<T> findAll() {
        return findAll(new Query());
    }

    @Override
    public Iterable<T> findAll(Iterable<ID> ids) {
        Set<ID> parameters = new HashSet<>(tryDetermineRealSizeOrReturn(ids, 10));
        for (ID id : ids) {
            parameters.add(id);
        }

        return findAll(new Query(new Criteria(entityInformation.getIdAttribute()).in(parameters)));
    }

    @Override
    public Page<T> findAll(final Pageable pageable) {
        Long count = count();
        List<T> list = findAll(new Query().with(pageable));

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public List<T> findAll(Sort sort) {
        return findAll(new Query().with(sort));
    }

    @Override
    public <S extends T> S insert(S entity) {
        Assert.notNull(entity, "Entity must not be null!");

        mongoOperations.insert(entity, entityInformation.getCollectionName());
        return entity;
    }

    @Override
    public <S extends T> List<S> insert(Iterable<S> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");

        List<S> list = convertIterableToList(entities);

        if (list.isEmpty()) {
            return list;
        }

        mongoOperations.insertAll(list);
        return list;
    }

    private List<T> findAll(Query query) {
        if (query == null) {
            return Collections.emptyList();
        }

        return mongoOperations.find(query, entityInformation.getJavaType(), entityInformation.getCollectionName());
    }

    private static <T> List<T> convertIterableToList(Iterable<T> entities) {
        if (entities instanceof List) {
            return (List<T>) entities;
        }

        int capacity = tryDetermineRealSizeOrReturn(entities, 10);

        if (capacity == 0 || entities == null) {
            return Collections.emptyList();
        }

        List<T> list = new ArrayList<>(capacity);
        for (T entity : entities) {
            list.add(entity);
        }

        return list;
    }

    private static int tryDetermineRealSizeOrReturn(Iterable<?> iterable, int defaultSize) {
        return iterable == null ? 0 : (iterable instanceof Collection) ? ((Collection<?>) iterable).size() : defaultSize;
    }
}
