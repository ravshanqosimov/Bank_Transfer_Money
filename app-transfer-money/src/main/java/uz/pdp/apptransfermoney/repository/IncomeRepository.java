package uz.pdp.apptransfermoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apptransfermoney.entity.Card;
import uz.pdp.apptransfermoney.entity.Income;

import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income,Integer> {


    List<Income> findAllByToCard(Card fromCard);

}
