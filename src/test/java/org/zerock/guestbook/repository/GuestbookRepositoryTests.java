package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository repository;

    @Test
    public void insetDummies() {

        IntStream.rangeClosed(1, 300).forEach( i-> {
            Guestbook book = Guestbook.builder()
                    .title("title.."+i)
                    .content("content.."+i)
                    .writer("writer.."+i)
                    .build();

            log.info(repository.save(book));
        });
    }
    //수정 테스트
    @Test
    public void testUpdate() {
        Optional<Guestbook> result = repository.findById(300L);

        if(result.isPresent()) {
            Guestbook book = result.get();
            log.info("book : " + book);

            book.changeContent(" changed content");
            book.changeTitle(" changed title ");

            repository.save(book);
        }
    }

    // 검색 테스트
    @Test
    public void testQuery1() {
        // 페이징관련 처리
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        // Q도메인 클래스를 얻어온다
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";
        // where문에 들어가는 조건을 넣어주는 컨테이너 역할
        BooleanBuilder builder = new BooleanBuilder();
        // 원하는 조건은 필드 값과 같이 결합해서 생성
        BooleanExpression expression = qGuestbook.title.contains(keyword);
        // 만들어진 조건을 where문의 and  나 or 같은 키워드로 조합
        builder.and(expression);
        // BooleanBuilder는 repository에 추가된 QuerydslPredicateExcutor의 findAll을 사용가능.
        Page<Guestbook> result = repository.findAll(builder, pageable);

        result.stream().forEach( book -> {
            log.info(book);
        });
    }

    //다중항목 검색테스트
    // 제목or 내용에 특정 단어 + gno가 0보다 크다
    @Test
    public void testQuery2() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        // Q도메인 클래스를 얻어온다
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";
        // where문에 들어가는 조건을 넣어주는 컨테이너 역할
        BooleanBuilder builder = new BooleanBuilder();
        // 원하는 조건은 필드 값과 같이 결합해서 생성
        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent = qGuestbook.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent);

        // 만들어진 조건을 where문의 and  나 or 같은 키워드로 조합
        builder.and(exAll);
        builder.and(qGuestbook.gno.gt(0L));

        // BooleanBuilder는 repository에 추가된 QuerydslPredicateExcutor의 findAll을 사용가능.
        Page<Guestbook> result = repository.findAll(builder, pageable);

        result.stream().forEach( book -> {
            log.info(book);
        });

    }
}
