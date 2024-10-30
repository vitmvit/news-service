package ru.clevertec.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.clevertec.news.model.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
}