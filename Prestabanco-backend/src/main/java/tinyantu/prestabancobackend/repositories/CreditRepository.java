package tinyantu.prestabancobackend.repositories;


import tinyantu.prestabancobackend.entities.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<CreditEntity, Long> {

    List<CreditEntity> findByUserId(Long ID);
    CreditEntity findByIdCredit(Long id);


}
