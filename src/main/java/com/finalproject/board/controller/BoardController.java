package com.finalproject.board.controller;

import com.finalproject.board.entity.Board;
import com.finalproject.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    // 게시글 목록
    @GetMapping("/board/list") //localhost:8080/board/list
    public String boardList(Model model){

        model.addAttribute("list", boardService.boardList());

        return "boardList";
    }

    // 게시글 작성 페이지
    @GetMapping("/board/write") //localhost:8080/board/write
    public String boardWriteForm(){

        return "boardWrite";
    }

    // 게시글 작성 처리
    @PostMapping("board/writePro") //localhost:8080/board/writePro
    public String boardWritePro(Board board, Model model){

        boardService.boardWrite(board);

        model.addAttribute("message", "게시글이 등록되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    // 게시글 상세보기
    @GetMapping("/board/view") //localhost:8080/board/view
    public String boardView(Model model, Integer id){

        model.addAttribute("board", boardService.boardView(id));
        return "boardView";
    }

    // 게시글 삭제
    @GetMapping("/board/delete") //localhost:8080/board/delete
    public String boardDelete(Integer id){

        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    // 게시글 수정 페이지
    @GetMapping("/board/modify/{id}") //localhost:8080/board/update
    public String boardModify(@PathVariable("id") Integer id, Model model){

        model.addAttribute("board", boardService.boardView(id));

        return "boardModify";
    }

    // 게시글 수정 처리
    @PostMapping("/board/update/{id}") //localhost:8080/board/update
    public String boardUpdate(@PathVariable("id") Integer id, Board boardModified){

        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(boardModified.getTitle());
        boardTemp.setContent(boardModified.getContent());

        boardService.boardWrite(boardTemp);

        return "redirect:/board/list";
    }
}
