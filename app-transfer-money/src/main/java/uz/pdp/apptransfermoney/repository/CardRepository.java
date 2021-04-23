package uz.pdp.apptransfermoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apptransfermoney.entity.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card,Integer> {

     boolean existsByNumber(String number);

     Optional<Card> findByNumber(String number);

     List<Card> findAllByUsername(String username);


}
