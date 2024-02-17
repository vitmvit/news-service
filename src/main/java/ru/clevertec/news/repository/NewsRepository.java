package ru.clevertec.news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.news.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    Page<News> findByTextContaining(Pageable pageable, String fragment);

    Page<News> findByTitleContaining(Pageable pageable, String fragment);
}
