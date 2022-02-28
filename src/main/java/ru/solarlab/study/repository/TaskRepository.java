package ru.solarlab.study.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.solarlab.study.entity.Task;

import java.util.List;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Integer> {

    List<Task> findByNameLike(String name);

}
