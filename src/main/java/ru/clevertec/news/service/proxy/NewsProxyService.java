package ru.clevertec.news.service.proxy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.clevertec.news.cache.Cache;
import ru.clevertec.news.dto.NewsDto;

@Slf4j
@Aspect
@AllArgsConstructor
public class NewsProxyService {

    private final Cache<Long, NewsDto> cache;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Pointcut("execution(* ru.clevertec.news.service.impl.NewsServiceImpl.findNewsById(..)))")
    public void getMethod() {

    }

    @Pointcut("execution(* ru.clevertec.news.service.impl.NewsServiceImpl.create(..)))")
    public void createMethod() {

    }

    @Pointcut("execution(* ru.clevertec.news.service.impl.NewsServiceImpl.update(..)))")
    public void updateMethod() {

    }

    @Pointcut("execution(* ru.clevertec.news.service.impl.NewsServiceImpl.delete(..)))")
    public void deleteMethod() {

    }

    /**
     * Возвращает объект NewsDto по указанному идентификатору.
     * Если объект есть в кэше, метод возвращает его из кэша.
     * В противном случае, вызывает метод из оригинального сервиса и кэширует результат.
     *
     * @param pjp прокси-объект для вызова оригинального метода
     * @return объект NewsDto
     * @throws Throwable при возникновении исключения при выполнении оригинального метода
     */
    @Around("getMethod()")
    public Object doGet(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("Proxy news aop: get method");
        var id = (Long) pjp.getArgs()[0];
        if (cache.get(id) == null) {
            var newsDto = (NewsDto) pjp.proceed();
            cache.put(id, newsDto);
            return newsDto;
        } else {
            return cache.get(id);
        }
    }

    /**
     * Выполняет создание объекта с кэшированием результата.
     * После выполнения метода, результат добавляется в кэш.
     *
     * @param pjp прокси-объект для вызова оригинального метода
     * @return объект, созданный методом, с кэшированным результатом
     * @throws Throwable если возникла ошибка при выполнении оригинального метода
     */
    @Around("createMethod()")
    public Object doCreate(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("Proxy news aop: post method");
        var newsDto = (NewsDto) pjp.proceed();
        cache.put(newsDto.getId(), newsDto);
        return newsDto;
    }

    /**
     * Выполняет обновление объекта с кэшированием результата.
     * После выполнения метода, результат обновления добавляется в кэш.
     *
     * @param pjp прокси-объект для вызова оригинального метода
     * @return объект, обновленный методом, с кэшированным результатом
     * @throws Throwable если возникла ошибка при выполнении оригинального метода
     */
    @Around("updateMethod()")
    public Object doUpdate(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("Proxy news aop: put method");
        var newsDto = (NewsDto) pjp.proceed();
        cache.put(newsDto.getId(), newsDto);
        return newsDto;
    }

    /**
     * Выполняет удаление объекта с кэшированием результата.
     * После выполнения метода, соответствующий объект удаляется из кэша.
     *
     * @param pjp прокси-объект для вызова оригинального метода
     * @return идентификатор удаленного объекта
     * @throws Throwable если возникла ошибка при выполнении оригинального метода
     */
    @Around("deleteMethod()")
    public Object doDelete(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("Proxy news aop: delete method");
        var id = (Long) pjp.getArgs()[0];
        pjp.proceed();
        cache.remove(id);
        return id;
    }
}