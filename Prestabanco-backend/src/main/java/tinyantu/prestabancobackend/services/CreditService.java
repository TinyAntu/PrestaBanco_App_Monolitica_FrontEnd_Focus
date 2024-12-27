package tinyantu.prestabancobackend.services;
import tinyantu.prestabancobackend.entities.CreditEntity;
import tinyantu.prestabancobackend.entities.DocumentEntity;
import tinyantu.prestabancobackend.entities.UserEntity;
import tinyantu.prestabancobackend.exceptions.BadRequestException;
import tinyantu.prestabancobackend.repositories.CreditRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditService {
    @Autowired
    CreditRepository creditRepository;

    @Autowired
    DocumentService documentService;

    @Autowired
    UserService userService;


    public CreditEntity saveCredit(Integer capital, Double annual_interest, Double years, Integer type,
                                   Integer income, Integer property_value, Integer debt, Long userId) {
        CreditEntity credit = CreditEntity.builder()
                .capital(capital)
                .annual_interest(annual_interest)
                .years(years)
                .type(type)
                .income(income)
                .property_value(property_value)
                .debt(debt)
                .userId(userId)
                .build();
        credit.setLevel(1);
        credit.setE(1);
        return creditRepository.save(credit);
    }


    public Long montly_Share(Integer Capital, Double Annual_interest, Double Years){

        if (Capital < 0) {

            throw new BadRequestException("Por favor, ingrese algún valor no negativo en el Capital");
        }

        Double M = 0.0;
        Double r = Annual_interest/100/12;
        Double n = Years*12;
        M = Capital*r* Math.pow(1+r,n) / (Math.pow(1 + r, n) -1);
        return Math.round(M);
    }

    public Long total_cost(Long id){
        CreditEntity Credit = creditRepository.findByIdCredit(id);
        Integer FireHazardSecure = 20000;
        Long Montly_share = montly_Share(Credit.getCapital(),Credit.getAnnual_interest(), Credit.getYears());
        Double Administration_comision = Credit.getCapital()*0.01;
        Double DeductionSecure = Credit.getCapital()*0.0003;
        Double Months = Credit.getYears()*12;
        Double Actual_Montly_share = Montly_share + DeductionSecure+ FireHazardSecure;
        Double Total_Cost = Actual_Montly_share*Months + Administration_comision;
        return Math.round(Total_Cost);
    }

    public Boolean share_income(Long share, Integer income){
        Double relation = (share/(double)income)*100;
        return relation <= 35;
    }

    public Boolean R1(Long id){
        CreditEntity Credit = creditRepository.findByIdCredit(id);
        Long Share = montly_Share(Credit.getCapital(), Credit.getAnnual_interest(), Credit.getYears());
        return share_income(Share, Credit.getIncome());
    }

    public Boolean R4(Long id){
        CreditEntity Credit = creditRepository.findByIdCredit(id);
        Integer Debt = Credit.getDebt();
        Integer Income = Credit.getIncome();
        Long Share = montly_Share(Credit.getCapital(), Credit.getAnnual_interest(), Credit.getYears());
        return Income * 0.5 >= Debt + Share;
    }

    public Boolean R5(Long id){
        CreditEntity Credit = creditRepository.findByIdCredit(id);
        Integer Credit_type = Credit.getType();
        Integer Financing = financing(Credit.getProperty_value(), Credit.getCapital());
        return switch (Credit_type) {
            case 1 -> // First home
                    Financing <= 80;
            case 2 -> // Second home
                    Financing <= 70;
            case 3 -> // Market property
                    Financing <= 60;
            case 4 -> // Remodeling
                    Financing <= 50;
            default -> false; // it exceeds the maximum
        };
    }

    public Boolean R6(Long id){
        CreditEntity Credit = creditRepository.findByIdCredit(id);
        UserEntity user = userService.findUserById(Credit.getUserId());
        Integer user_age = userService.AgeInYears(user.getBirthdate());

        return user_age + Credit.getYears() < 70;
    }

    public Boolean E1(Long id) {
        CreditEntity Credit = creditRepository.findByIdCredit(id);
        Integer type_credit = Credit.getType();
        List<DocumentEntity> Docs = documentService.getDocuments(id);

        //If anything (except state) is null return false
        if (Credit.getIdCredit() == null ||
                Credit.getCapital() == null ||
                Credit.getAnnual_interest() == null ||
                Credit.getYears() == null ||
                Credit.getType() == null ||
                Credit.getIncome() == null ||
                Credit.getLevel() == null ||
                Credit.getProperty_value() == null ||
                Credit.getDebt() == null ||
                Credit.getUserId() == null) {
            return false;
        }

        return switch (type_credit) {
            case 1 -> // First home
                    Docs.size() >= 3;
            case 2 -> // Second home
                    Docs.size() >= 4;
            case 3 -> // Market property
                    Docs.size() >= 4;
            case 4 -> // Remodeling
                    Docs.size() >= 3;
            default -> false; // Invalid credit type
        };
    }


    public Integer financing(Integer value, Integer capital ){
        return  (int) ((capital / (double) value) * 100);
    }

    public boolean deleteCredit(Long id) throws Exception{
        try{
            creditRepository.deleteById(id);
            return true;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<CreditEntity> getCredits(Long id){
        return creditRepository.findByUserId(id);
    }

    public CreditEntity updateCredit(Long id, CreditEntity credit) {
        // Check if the credit exists
        if (!creditRepository.existsById(id)) {
            throw new EntityNotFoundException("Credit not found with ID: " + id);
        }

        // Set the ID of the credit to be updated
        credit.setIdCredit(id);

        // Save and return the updated credit entity
        return creditRepository.save(credit);
    }

    public List<CreditEntity> getCredits(){
        return creditRepository.findAll();
    }

}
