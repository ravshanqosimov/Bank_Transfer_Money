package uz.pdp.apptransfermoney.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.apptransfermoney.entity.Card;
import uz.pdp.apptransfermoney.entity.Income;
import uz.pdp.apptransfermoney.entity.Outcome;
import uz.pdp.apptransfermoney.payload.ApiResponse;
import uz.pdp.apptransfermoney.payload.TransferMoneyDto;
import uz.pdp.apptransfermoney.repository.CardRepository;
import uz.pdp.apptransfermoney.repository.IncomeRepository;
import uz.pdp.apptransfermoney.repository.OutcomeRepository;
import uz.pdp.apptransfermoney.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransferMoneyService {
    @Autowired
    CardRepository cardRepository;

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    IncomeRepository incomeRepository;
    @Autowired
    OutcomeRepository outcomeRepository;

    public ApiResponse transferMoney(TransferMoneyDto moneyDto, HttpServletRequest httpServletRequest) {
        //kommissiya 1%
        final double PERCENTAGE = 0.01F;

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUserNameFromToken(token);


        Optional<Card> optionalFromCard = cardRepository.findByNumber(moneyDto.getFromCard());
        if (!optionalFromCard.isPresent())
            return new ApiResponse("Card not found", false, null);

        Optional<Card> optionalToCard = cardRepository.findByNumber(moneyDto.getToCard());
        if (!optionalToCard.isPresent()) return new ApiResponse("Card not found", false, null);


        Card fromCard = optionalFromCard.get();
        Card toCard = optionalToCard.get();

        if (!fromCard.getUsername().equals(username) || !fromCard.getNumber().equals(toCard.getNumber()))
            return new ApiResponse("The card does not belong to you or error in card numbers", false, null);


        double balance = fromCard.getBalance();
        double amount = moneyDto.getAmount();
        double commissionAmount = amount * PERCENTAGE;
        double totalAmount = amount + commissionAmount;
        if (balance < totalAmount) return new ApiResponse("not enough money", false, null);

        fromCard.setBalance(balance - totalAmount);
        toCard.setBalance(toCard.getBalance() + amount);
        Card savedFromCard = cardRepository.save(fromCard);
        Card savedToCard = cardRepository.save(toCard);


        Outcome outcome = new Outcome();
        outcome.setAmount(amount);
        outcome.setFromCard(savedFromCard);
        outcome.setToCard(savedToCard);
        outcome.setCommissionAmount(commissionAmount);
        outcome.setDate(new Date());
        outcomeRepository.save(outcome);


        Income income = new Income();
        income.setAmount(amount);
        income.setDate(new Date());
        income.setFromCard(savedFromCard);
        income.setToCard(savedToCard);
        incomeRepository.save(income);

        return new ApiResponse("Transaction sent successfully", true, optionalFromCard);
    }

    public ApiResponse getIncome(String number, HttpServletRequest httpServletRequest) {


        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUserNameFromToken(token);

        Optional<Card> optionalCard = cardRepository.findByNumber(number);
        if (!optionalCard.isPresent() || !optionalCard.get().getUsername().equals(username))
            return new ApiResponse("The card does not belong to you or card not found", false, null);

        List<Income> incomeList = incomeRepository.findAllByToCard(optionalCard.get());
        return new ApiResponse("Income list", true, incomeList);


    }  public ApiResponse getOutcome(String number, HttpServletRequest httpServletRequest) {


        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUserNameFromToken(token);

        Optional<Card> optionalCard = cardRepository.findByNumber(number);
        if (!optionalCard.isPresent() || !optionalCard.get().getUsername().equals(username))
            return new ApiResponse("The card does not belong to you or card not found", false, null);

        List<Outcome>  outcomeList = outcomeRepository.findAllByFromCard(optionalCard.get());
        return new ApiResponse("Outcome list", true, outcomeList);


    }


}
