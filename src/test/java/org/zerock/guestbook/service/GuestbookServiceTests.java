package org.zerock.guestbook.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

@SpringBootTest
@Log4j2
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService service;

    @Test
    public void testRegister() {

        GuestbookDTO dto = GuestbookDTO.builder()
                .title("sample Title ...")
                .content("sample Content..")
                .writer("user0")
                .build();

        System.out.println(service.register(dto));
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1).size(10).build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("prev : " +resultDTO.isPrev());
        System.out.println("next : " + resultDTO.isNext());
        System.out.println("total : " + resultDTO.getTotalPage());

        System.out.println("-------------------------------------------");

        for (GuestbookDTO dto : resultDTO.getDtoList()) {
            System.out.println(dto);
        }

        System.out.println("-=----------------------------------------");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

    @Test
    public void testSearch() {
        PageRequestDTO requestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc")
                .keyword("한글")
                .build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(requestDTO);

        System.out.println("prev : " + resultDTO.isPrev());
        System.out.println("next: " +resultDTO.isNext());
        System.out.println("total : " +resultDTO.getTotalPage());

        System.out.println("===================================");
        for (GuestbookDTO dto : resultDTO.getDtoList()) {
            System.out.println(dto);
        }

        System.out.println("==============================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));

    }
}
