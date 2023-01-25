package it.polimi.emall.emsp.bookingmanagementservice.utils;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public abstract class BaseManager <T extends Identifiable<K>, K, D>{
    protected final CrudRepository<T, K> crudRepository;

    protected BaseManager(CrudRepository<T, K> crudRepository) {
        this.crudRepository = crudRepository;
    }
    public T getEntityByKey(K key){
        return crudRepository.findById(key).orElseThrow();
    }

    public void delete(K key){
        if(crudRepository.existsById(key)) {
            preDelete(key);
            crudRepository.deleteById(key);
        }
    }

    public void deleteAll(){
        StreamSupport.stream(crudRepository.findAll().spliterator(), false)
                        .forEach(entity -> preDelete(entity.getId()));
        crudRepository.deleteAll();
    }

    public T update(K key, D dto){
        return updateEntity(getEntityByKey(key), dto);
    }

    public T update(Supplier<Optional<T>> searchCriteria, D dto){
        return updateEntity(searchCriteria.get().orElseThrow(), dto);
    }

    protected void preDelete(K key){}

    protected abstract T updateEntity(T currentState, D desiredState);
}
