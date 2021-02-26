package org.zerock.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
public class GuestbookServiceImpl implements GuestbookService {

    @Autowired
    private GuestbookRepository repository;

    @Override
    public Long register(GuestbookDTO dto) {

        log.info("DTO....");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);

        log.info(entity);

        repository.save(entity);
        return entity.getGno();
    }

    @Override
    public GuestbookDTO read(Long gno) {
        log.info("read gno ... : " + gno);
        // 컨트롤러에서 요청.
        // 키값 하나를 가지고 메모리상 존재하는 엔티티객체를 확인
        Optional<Guestbook> result = repository.findById(gno);
        // 그리고 있을 경우 DTO객체로 변환시켜서 보내준다.
        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    @Override
    public void remove(Long gno) {
        log.info("remove gno ... : " + gno);

        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {
        log.info("modify dto ... :" + dto);
        // 수정할때 들어오는 dto데이터를 가지고
        // entity객체로 받아서 확인한다.
        Optional<Guestbook> result = repository.findById(dto.getGno());
        // 데이터가 있으면 진행
        if (result.isPresent()) {
            Guestbook entity = result.get();
            // 수정할떄 들어온 gno값을 가진 메모리상의 데이터를 가져와서
            log.info("entity : " + entity);
            // 변경메서드를 통해서 메모리상의 데이터를 변경한다
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
            // 변경 시키고 메모리상에서 직접 확인 후 변경 또는 그대로 둔다.
            repository.save(entity);
        }
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        log.info("getList....");
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder builder = getSearch(requestDTO); // 검색처리 추가

        Page<Guestbook> result = repository.findAll(builder, pageable); //entity 객체로 목록을 뽑아온다.
        log.info("Page<Guestbook> result = " + result);
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity)); // 객체타입을 변환시킨다.
        log.info("Function<Guestbook, GuestbookDTO> fn : " + fn);

        return new PageResultDTO<>(result, fn);
    }

    // 검색처리 메서드
    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {
        //Querydsl 처리
        String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();
        // 객체생성
        QGuestbook qGuestbook = QGuestbook.guestbook;
        // 검색 컨테이너 생성
        BooleanBuilder builder = new BooleanBuilder();
        // gno가 0보다 큰 조건
        BooleanExpression expression = qGuestbook.gno.gt(0);
        //  where gno > 0 and
        builder.and(expression);
        if(type==null || type.trim().length() == 0) {
            return builder;
        }

        //검색 조건 작성
        BooleanBuilder searchBuilder = new BooleanBuilder();

        if(type.contains("t")) {
            searchBuilder.or(qGuestbook.title.contains(keyword));
        }
        if(type.contains("c")) {
            searchBuilder.or(qGuestbook.content.contains(keyword));
        }
        if(type.contains("w")) {
            searchBuilder.or(qGuestbook.writer.contains(keyword));
        }

        builder.and(searchBuilder);

        return builder;
    }
}
