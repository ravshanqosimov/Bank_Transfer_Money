package uz.pdp.apptransfermoney.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apptransfermoney.payload.ApiResponse;
import uz.pdp.apptransfermoney.payload.TransferMoneyDto;
import uz.pdp.apptransfermoney.service.TransferMoneyService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
public class TransferMoneyController {
    @Autowired
    TransferMoneyService transferMoneyService;


    @PostMapping("/transfer")
    public HttpEntity<?> transfer(@RequestBody TransferMoneyDto moneyDto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = transferMoneyService.transferMoney(moneyDto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK :
                HttpStatus.CONFLICT).body(apiResponse.getMessage());
    }

    @GetMapping("/outcome")

    public HttpEntity<?> getOutcome(@RequestParam String number, HttpServletRequest httpServletRequest) {

        ApiResponse apiResponse = transferMoneyService.getOutcome(number, httpServletRequest);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse.getMessage());


    } @GetMapping("/income")

    public HttpEntity<?> getIncome(@RequestParam String number, HttpServletRequest httpServletRequest) {

        ApiResponse apiResponse = transferMoneyService.getIncome(number, httpServletRequest);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse.getMessage());


    }

}
