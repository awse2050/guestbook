package org.zerock.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@Log4j2
@RequestMapping("/guestbook")
@RequiredArgsConstructor
public class GuestBookController {

    private final GuestbookService service;

    @GetMapping("/")
    public String index() {
        return "redirect:/guestbook/list";
    }

    @GetMapping({"/", "/list"})
    public void list(PageRequestDTO requestDTO, Model model) {
        log.info("list ........... " + requestDTO);
        log.info("page : " + requestDTO.getPage());
        log.info("size : " + requestDTO.getSize());

        model.addAttribute("result", service.getList(requestDTO));
    }

    @GetMapping("/register")
    public void register() {
        log.info("register page ....");
    }

    @GetMapping({"/read", "/modify"})
    public void readOrModify(Long gno, @ModelAttribute("requestDTO")PageRequestDTO requestDTO, Model model) {
        log.info("requestDTO : " + requestDTO);
        log.info("gno ... : " + gno);

        GuestbookDTO dto = service.read(gno);

        model.addAttribute("dto" , dto);
    }

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto , RedirectAttributes rttr) {
        log.info("dto .... : " + dto);

        // 새로 추가된 엔티티의 번호
        // 해당 번호를 가져옴.
        Long gno = service.register(dto);
        log.info("gno ... : " + gno);

        rttr.addFlashAttribute("msg", gno);
        return "redirect:/guestbook/list";
    }

    @PostMapping("/remove")
    public String remove(Long gno, RedirectAttributes rttr) {
        log.info("in Controller Remove method..");
        log.info("gno : " + gno);

        service.remove(gno);

        rttr.addFlashAttribute("msg" , gno);

        return "redirect:/guestbook/list";
    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto,
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes rttr) {
        log.info("post modify...");
        log.info("dto : " + dto);

        service.modify(dto);

        rttr.addAttribute("gno", dto.getGno());
        rttr.addAttribute("page", requestDTO.getPage());
        rttr.addAttribute("type", requestDTO.getType());
        rttr.addAttribute("keyword", requestDTO.getKeyword());

        return "redirect:/guestbook/read";
    }
}
