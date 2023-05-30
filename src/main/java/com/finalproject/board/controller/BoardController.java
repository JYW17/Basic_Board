package com.finalproject.board.controller;



import com.finalproject.board.entity.Board;
import com.finalproject.board.service.BoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    // 게시판 홈페이지
    @GetMapping("/") //localhost:8080/
    public String home(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 사용자 이름을 모델에 추가하여 프로필 페이지에 전달
        model.addAttribute("username", username);

        return "board/home";
    }


    // 게시글 목록
    @GetMapping("/board/list") //localhost:8080/board/list
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
                            Pageable pageable,
                            String searchKeyword)
    {

        Page<Board> list = null;

        if (searchKeyword == null) {
            list = boardService.boardList(pageable);
        } else{
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1; // 0부터 시작하므로 1 추가
        int totalPage = list.getTotalPages() - 1; // 전체 페이지 수

        int startPage = Math.max(nowPage - 4, 1); // 음수 방지
        int endPage = Math.min(nowPage + 5, totalPage); // 최대 페이지 초과 방지


        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPage", totalPage);


        return "board/boardList";
    }

    // 게시글 작성 페이지
    @GetMapping("/board/write") //localhost:8080/board/write
    public String boardWriteForm(){

        return "board/boardWrite";
    }

    // 게시글 작성 처리

    @PostMapping("/board/writePro") //localhost:8080/board/writePro
    public String boardWrite(Board board, MultipartFile file, Model model) throws Exception{


        boardService.boardWrite(board, file);


        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("nextUrl", "/board/list");

        return "board/message";
    }

    // 게시글 상세보기
    @GetMapping("/board/view") //localhost:8080/board/view
    public String boardView(Model model, Integer id){

        model.addAttribute("board", boardService.boardView(id));
        return "board/boardView";
    }

    // 게시글 삭제
    @GetMapping("/board/delete") //localhost:8080/board/delete
    public String boardDelete(Integer id, Model model){

        boardService.boardDelete(id);

        model.addAttribute("message", "글 삭제가 완료되었습니다.");
        model.addAttribute("nextUrl", "/board/list");

        return "/board/message";
    }

    // 게시글 수정 페이지
    @GetMapping("/board/modify/{id}") //localhost:8080/board/update
    public String boardModify(@PathVariable("id") Integer id, Model model){

        model.addAttribute("board", boardService.boardView(id));

        return "board/boardModify";
    }

    // 게시글 수정 처리
    @PostMapping("/board/update/{id}") //localhost:8080/board/update
    public String boardUpdate(@PathVariable("id") Integer id, Board boardModified, MultipartFile file, Model model) throws Exception{

        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(boardModified.getTitle());
        boardTemp.setContent(boardModified.getContent());
        boardTemp.setFilepath(boardModified.getFilepath());
        boardTemp.setFilename(boardModified.getFilename());

        boardService.boardWrite(boardTemp, file);

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("nextUrl", "/board/list");

        return "/board/message";
    }
}
