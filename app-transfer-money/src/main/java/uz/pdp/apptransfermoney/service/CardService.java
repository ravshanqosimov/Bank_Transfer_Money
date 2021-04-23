package uz.pdp.apptransfermoney.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.apptransfermoney.entity.Card;
import uz.pdp.apptransfermoney.payload.ApiResponse;
import uz.pdp.apptransfermoney.payload.CardDto;
import uz.pdp.apptransfermoney.repository.CardRepository;
import uz.pdp.apptransfermoney.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    @Autowired
    CardRepository cardRepository;

    @Autowired
    JwtProvider jwtProvider;


    public ApiResponse add(CardDto cardDto, HttpServletRequest httpServletRequest) {
        if (cardRepository.existsByNumber(cardDto.getNumber()))
            return new ApiResponse("such a card is available", false, new Card());

        String token = httpServletRequest.getHeader("Authorization");

            token = token.substring(7);

                String username = jwtProvider.getUserNameFromToken(token);
                Card card = new Card();
                card.setUsername(username);
                card.setNumber(cardDto.getNumber());
                card.setExpiredDate(cardDto.getExpiredDate());
                Card savedCard = cardRepository.save(card);
                return new ApiResponse("Cars added", true, savedCard);

    }

    public ApiResponse getAll(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");

            token = token.substring(7);
            String username = jwtProvider.getUserNameFromToken(token);
        List<Card> cardList = cardRepository.findAllByUsername(username);
        if (cardList.size()==0)return new ApiResponse("Cars not found",false,null);
        return new ApiResponse("list of cards",true,cardList);

    }


    public ApiResponse getByID(Integer id, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
            token = token.substring(7);
            String username = jwtProvider.getUserNameFromToken(token);
            Optional<Card> optionalCard = cardRepository.findById(id);

            if (optionalCard.isPresent() && optionalCard.get().getUsername().equals(username))
                return new ApiResponse("Card: ",true,optionalCard.get());
         return new ApiResponse("not found",false,null);
    }

    public ApiResponse delete(Integer id, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");

            token = token.substring(7);
            String username = jwtProvider.getUserNameFromToken(token);
            Optional<Card> optionalCard = cardRepository.findById(id);
            if (optionalCard.isPresent() && optionalCard.get().getUsername().equals(username)) {
                cardRepository.deleteById(id);
                return new ApiResponse("Card deleted", true, null);

        }

        return new ApiResponse("card not found",false,null);
    }



}


