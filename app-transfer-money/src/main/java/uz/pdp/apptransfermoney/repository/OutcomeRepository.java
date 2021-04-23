package uz.pdp.apptransfermoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apptransfermoney.entity.Card;
import uz.pdp.apptransfermoney.entity.Outcome;

import java.util.List;

public interface OutcomeRepository extends JpaRepository<Outcome,Integer> {

    List<Outcome> findAllByFromCard(Card fromCard);
}
