package uz.pdp.apptransfermoney.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apptransfermoney.payload.ApiResponse;
import uz.pdp.apptransfermoney.payload.CardDto;
import uz.pdp.apptransfermoney.service.CardService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/card")
public class CardController {
    @Autowired
    CardService cardService;

    @PostMapping
    public HttpEntity<?> add(@RequestBody CardDto cardDto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = cardService.add(cardDto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED :
                HttpStatus.CONFLICT).body(apiResponse.getMessage());
    }

    @GetMapping
    public HttpEntity<?> getAll(HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = cardService.getAll(httpServletRequest);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse.getMessage());

    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = cardService.getByID(id, httpServletRequest);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse.getMessage());
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(Integer id, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = cardService.delete(id, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse.getMessage());
    }


}
