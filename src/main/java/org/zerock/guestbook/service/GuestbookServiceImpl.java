package org.zerock.guestbook.service;

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
import org.zerock.guestbook.repository.GuestbookRepository;

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
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        log.info("getList....");
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        Page<Guestbook> result = repository.findAll(pageable);
        log.info("Page<Guestbook> result = " + result);
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));
        log.info("Function<Guestbook, GuestbookDTO> fn : " + fn);

        return new PageResultDTO<>(result, fn);
    }
}
