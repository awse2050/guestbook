package org.zerock.guestbook.service;

import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

public interface GuestbookService {
    // 추가
    Long register(GuestbookDTO dto);
    // 읽기
    public GuestbookDTO read(Long gno);
    //삭제
    public void remove(Long gno);
    //수정
    public void modify(GuestbookDTO dto);
    // 목록처리
    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    // 구현클래스에서 동작가능한 메서드
    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    default GuestbookDTO entityToDto(Guestbook entity) {
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;

    }
}
