package tinyantu.prestabancobackend.services;

import tinyantu.prestabancobackend.entities.UserEntity;
import tinyantu.prestabancobackend.entities.CreditEntity;
import tinyantu.prestabancobackend.entities.DocumentEntity;
import tinyantu.prestabancobackend.repositories.CreditRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreditServiceTest {

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private DocumentService documentService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CreditService creditService;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //Test for save
    @Test
    public void testSaveCredit_1() {
        CreditEntity credit = CreditEntity.builder()
                .capital(100000)
                .annual_interest(5.0)
                .years(15.0)
                .type(1)
                .income(50000)
                .property_value(200000)
                .amount(150000)
                .debt(20000)
                .userId(1L)
                .build();
        when(creditRepository.save(any(CreditEntity.class))).thenReturn(credit);

        CreditEntity savedCredit = creditService.saveCredit(100000, 5.0, 15.0, 1, 50000, 200000, 150000, 20000, 1L);

        assertNotNull(savedCredit);
        assertEquals(100000, savedCredit.getCapital());
    }

    @Test
    public void testSaveCredit_2() {
        CreditEntity credit = CreditEntity.builder()
                .capital(100000)
                .annual_interest(5.0)
                .years(15.0)
                .type(1)
                .income(50000)
                .property_value(200000)
                .amount(150000)
                .debt(20000)
                .userId(2L)
                .build();
        when(creditRepository.save(any(CreditEntity.class))).thenReturn(credit);

        CreditEntity savedCredit = creditService.saveCredit(100000, 5.0, 15.0, 1, 50000, 200000, 150000, 20000, 2L);

        assertNotNull(savedCredit);
        assertEquals(2L, savedCredit.getUserId());
    }

    @Test
    public void testSaveCredit_3() {
        CreditEntity credit = CreditEntity.builder()
                .capital(100000)
                .annual_interest(5.0)
                .years(15.0)
                .type(1)
                .income(50000)
                .property_value(200000)
                .amount(150000)
                .debt(20000)
                .userId(1L)
                .build();
        when(creditRepository.save(any(CreditEntity.class))).thenReturn(credit);

        CreditEntity savedCredit = creditService.saveCredit(100000, 5.0, 15.0, 1, 50000, 200000, 150000, 20000, 1L);

        assertNotNull(savedCredit);
        assertEquals(100000, savedCredit.getCapital());
    }

    @Test
    public void testSaveCredit_4() {
        CreditEntity credit = CreditEntity.builder()
                .capital(100000)
                .annual_interest(5.0)
                .years(15.0)
                .type(1)
                .income(50000)
                .property_value(200000)
                .amount(150000)
                .debt(20000)
                .userId(1L)
                .build();
        when(creditRepository.save(any(CreditEntity.class))).thenReturn(credit);

        CreditEntity savedCredit = creditService.saveCredit(100000, 5.0, 15.0, 1, 50000, 200000, 150000, 20000, 1L);

        assertNotNull(savedCredit);
        assertEquals(5.0, savedCredit.getAnnual_interest());
    }

    @Test
    public void testSaveCredit_5() {
        CreditEntity credit = CreditEntity.builder()
                .capital(100000)
                .annual_interest(5.0)
                .years(15.0)
                .type(1)
                .income(50000)
                .property_value(200000)
                .amount(150000)
                .debt(20000)
                .userId(1L)
                .build();
        when(creditRepository.save(any(CreditEntity.class))).thenReturn(credit);

        CreditEntity savedCredit = creditService.saveCredit(100000, 5.0, 15.0, 1, 50000, 200000, 150000, 20000, 1L);

        assertNotNull(savedCredit);
        assertEquals(1, savedCredit.getType());
    }

    @Test
    public void testSaveCredit_6() {
        CreditEntity credit = CreditEntity.builder()
                .capital(100000)
                .annual_interest(5.0)
                .years(15.0)
                .type(1)
                .income(50000)
                .property_value(200000)
                .amount(150000)
                .debt(20000)
                .userId(1L)
                .build();
        when(creditRepository.save(any(CreditEntity.class))).thenReturn(credit);

        CreditEntity savedCredit = creditService.saveCredit(100000, 5.0, 15.0, 1, 50000, 200000, 150000, 20000, 1L);

        assertNotNull(savedCredit);
        assertEquals(15.0, savedCredit.getYears());
    }

    @Test
    public void testSaveCredit_7() {
        CreditEntity credit = CreditEntity.builder()
                .capital(100000)
                .annual_interest(5.0)
                .years(15.0)
                .type(1)
                .income(50000)
                .property_value(200000)
                .amount(150000)
                .debt(20000)
                .userId(1L)
                .build();
        when(creditRepository.save(any(CreditEntity.class))).thenReturn(credit);

        CreditEntity savedCredit = creditService.saveCredit(100000, 5.0, 15.0, 1, 50000, 200000, 150000, 20000, 1L);

        assertNotNull(savedCredit);
        assertEquals(50000, savedCredit.getIncome());
    }

    //Test for montly_Share

    @Test
    public void testMontlyShareCalculates() {
        Long result = creditService.montly_Share(100000, 5.0, 15.0);
        assertNotNull(result);
        assertTrue(result > 0);
    }

    @Test
    public void testMontlyShareEquals() {
        Long result = creditService.montly_Share(100000000, 4.5, 20.0);
        assertNotNull(result);
        assertEquals(result, 632649L);
    }

    @Test
    public void testMontlyShareNotFullYear() {
        Long result = creditService.montly_Share(100000000, 4.5, 20.5);
        assertNotNull(result);
        assertEquals(result, 623141L);
    }

    @Test
    public void testMontlyShareWithLowCapital() {
        Long actualMonthlyShare = creditService.montly_Share(1000, 5.0, 1.0);

        assertEquals(86L, actualMonthlyShare);
    }

    @Test
    public void testMontlyShareWithHighCapital() {
        Long actualMonthlyShare = creditService.montly_Share(100000, 4.5, 15.0);

        assertEquals(765L, actualMonthlyShare);
    }

    @Test
    public void testMontlyShareWithZeroInterest() {
        Long actualMonthlyShare = creditService.montly_Share(50000, 0.0, 10.0);

        assertEquals(0L, actualMonthlyShare);
    }

    @Test
    public void testMontlyShareWithLongerTerm() {
        Long actualMonthlyShare = creditService.montly_Share(20000, 6.0, 30.0);

        assertEquals(120L, actualMonthlyShare);
    }

    //Test for total_cost
    @Test
    public void testTotalCostReturn() {
        CreditEntity credit = CreditEntity.builder().capital(100000).annual_interest(5.0).years(15.0).amount(150000).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Long totalCost = creditService.total_cost(1L);
        assertNotNull(totalCost);
        assertTrue(totalCost > 0);
    }

    @Test
    public void testTotalCost_164835760() {
        CreditEntity credit = CreditEntity.builder().capital(100000000).annual_interest(4.5).years(20.0).amount(100000000).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Long totalCost = creditService.total_cost(1L);
        assertNotNull(totalCost);
        assertEquals(totalCost , 164835760);
    }

    @Test
    public void testTotalCost_5087440() {
        CreditEntity credit = CreditEntity.builder()
                .capital(1000000) // Example capital
                .annual_interest(5.0) // Example interest
                .years(15.0) // Example years
                .amount(1000000) // Example amount
                .build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Long totalCost = creditService.total_cost(1L);
        assertNotNull(totalCost);
        // Calculation based on provided formula and values
        // Expected Total Cost = Actual Monthly Share * Months + Administration Commission
        assertEquals(5087440, totalCost); // Replace with actual expected result after calculation
    }

    @Test
    public void testTotalCost_4921880() {
        CreditEntity credit = CreditEntity.builder()
                .capital(2000000)
                .annual_interest(4.0)
                .years(10.0)
                .amount(2000000)
                .build();
        when(creditRepository.findByIdCredit(2L)).thenReturn(credit);

        Long totalCost = creditService.total_cost(2L);
        assertNotNull(totalCost);
        // Calculate expected total cost manually or using the method
        assertEquals(4921880, totalCost); // Replace with actual expected result after calculation
    }

    @Test
    public void testTotalCost_10204320() {
        CreditEntity credit = CreditEntity.builder()
                .capital(3000000)
                .annual_interest(6.0)
                .years(20.0)
                .amount(3000000)
                .build();
        when(creditRepository.findByIdCredit(3L)).thenReturn(credit);

        Long totalCost = creditService.total_cost(3L);
        assertNotNull(totalCost);
        // Calculate expected total cost manually or using the method
        assertEquals(10204320, totalCost); // Replace with actual expected result after calculation
    }

    @Test
    public void testTotalCost_2879280() {
        CreditEntity credit = CreditEntity.builder()
                .capital(1500000)
                .annual_interest(3.5)
                .years(5.0)
                .amount(1500000)
                .build();
        when(creditRepository.findByIdCredit(4L)).thenReturn(credit);

        Long totalCost = creditService.total_cost(4L);
        assertNotNull(totalCost);
        // Calculate expected total cost manually or using the method
        assertEquals(2879280, totalCost); // Replace with actual expected result after calculation
    }

    @Test
    public void testTotalCost_2593832() {
        CreditEntity credit = CreditEntity.builder()
                .capital(500000)
                .annual_interest(7.0)
                .years(8.0)
                .amount(500000)
                .build();
        when(creditRepository.findByIdCredit(5L)).thenReturn(credit);

        Long totalCost = creditService.total_cost(5L);
        assertNotNull(totalCost);
        // Calculate expected total cost manually or using the method
        assertEquals(2593832, totalCost); // Replace with actual expected result after calculation
    }


    //Test ShareIncome
    @Test
    public void testShareIncome30() {
        Boolean result = creditService.share_income(3000L, 10000);
        assertTrue(result);
    }

    @Test
    public void testShareIncome20() {
        Boolean result = creditService.share_income(3000L, 10000);
        assertTrue(result);
    }

    @Test
    public void testShareIncome10() {
        Boolean result = creditService.share_income(1000L, 10000);
        assertTrue(result);
    }

    @Test
    public void testShareIncome35() {
        Boolean result = creditService.share_income(3500L, 10000);
        assertTrue(result);
    }

    @Test
    public void testShareIncome36() {
        Boolean result = creditService.share_income(3600L, 10000);
        assertFalse(result);
    }

    @Test
    public void testShareIncome40() {
        Boolean result = creditService.share_income(4000L, 10000);
        assertFalse(result);
    }

    @Test
    public void testShareIncome60() {
        Boolean result = creditService.share_income(6000L, 10000);
        assertFalse(result);
    }


    //Test for R1
    @Test
    public void testR1() {
        CreditEntity credit = CreditEntity.builder()
                .capital(100000)
                .annual_interest(5.0)
                .years(15.0)
                .income(50000)
                .build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R1(1L);
        assertNotNull(result);
    }
    @Test
    public void testR1WithNullId() {
        Long nullId = null;

        assertThrows(NullPointerException.class, () -> {
            creditService.R1(nullId);
        });
    }

    @Test
    public void testR1_WithValidId() {
        CreditEntity credit = CreditEntity.builder()
                .capital(100000)
                .annual_interest(5.0)
                .years(10.0)
                .income(3000)
                .build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R1(1L);
        assertFalse(result);
    }

    @Test
    public void testR1_WithHighShare() {
        CreditEntity credit = CreditEntity.builder()
                .capital(100000)
                .annual_interest(5.0)
                .years(10.0)
                .income(2000)
                .build();
        when(creditRepository.findByIdCredit(2L)).thenReturn(credit);

        Boolean result = creditService.R1(2L);
        assertFalse(result);
    }

    @Test
    public void testR1_WithLowIncome() {
        CreditEntity credit = CreditEntity.builder()
                .capital(50000)
                .annual_interest(3.0)
                .years(5.0)
                .income(1000)
                .build();
        when(creditRepository.findByIdCredit(3L)).thenReturn(credit);

        Boolean result = creditService.R1(3L);
        assertFalse(result);
    }

    @Test
    public void testR1_WithLowDebt() {
        CreditEntity credit = CreditEntity.builder()
                .capital(200000)
                .annual_interest(6.0)
                .years(15.0)
                .income(4000) // Set income for the share_income calculation
                .debt(3000) // High debt to see how it impacts the result
                .build();
        when(creditRepository.findByIdCredit(4L)).thenReturn(credit);

        Boolean result = creditService.R1(4L);
        assertFalse(result); // Should return false if the calculated share exceeds 35% of income
    }

    @Test
    public void testR1_WithHighDebt() {
        CreditEntity credit = CreditEntity.builder()
                .capital(200000)
                .annual_interest(6.0)
                .years(15.0)
                .income(4000)
                .debt(100)
                .build();
        when(creditRepository.findByIdCredit(4L)).thenReturn(credit);

        Boolean result = creditService.R1(4L);
        assertFalse(result);
    }

    //Test for R4
    @Test
    public void testR4Returns() {
        CreditEntity credit = CreditEntity.builder().capital(100000).annual_interest(5.0).years(15.0).income(50000).debt(20000).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R4(1L);
        assertNotNull(result);
    }

    @Test
    public void testR4True() {
        CreditEntity credit = CreditEntity.builder().capital(100000).annual_interest(5.0).years(15.0).income(50000).debt(0).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R4(1L);
        assertTrue(result);
    }

    @Test
    public void testR4False() {
        CreditEntity credit = CreditEntity.builder().capital(100000).annual_interest(5.0).years(15.0).income(50000).debt(200000).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R4(1L);
        assertFalse(result);
    }

    @Test
    public void testR4ZeroDebtAndShare() {
        CreditEntity credit = CreditEntity.builder().capital(100000).annual_interest(5.0).years(15.0).income(50000).debt(0).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R4(1L);
        assertTrue(result);
    }

    @Test
    public void testR4HalfOfIncomeDebt() {
        CreditEntity credit = CreditEntity.builder().capital(100000).annual_interest(5.0).years(15.0).income(50000).debt(25000).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R4(1L);
        assertFalse(result);
    }

    @Test
    public void testR4_3PartsOfIncomeDebt() {
        CreditEntity credit = CreditEntity.builder().capital(100000).annual_interest(5.0).years(15.0).income(50000).debt(37500).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R4(1L);
        assertFalse(result);
    }

    @Test
    public void testR4HighIncomeAndDebt() {
        CreditEntity credit = CreditEntity.builder().capital(100000).annual_interest(5.0).years(15.0).income(100000).debt(40000).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R4(1L);
        assertTrue(result);
    }

    //Test for R5

    @Test
    public void testR5Return() {
        CreditEntity credit = CreditEntity.builder().type(1).property_value(200000).amount(150000).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R5(1L);
        assertNotNull(result);
    }

    @Test
    public void testR5True() {
        CreditEntity credit = CreditEntity.builder().type(1).property_value(200000).amount(150000).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R5(1L);
        assertTrue(result);
    }

    @Test
    public void testR5False() {
        CreditEntity credit = CreditEntity.builder().type(1).property_value(200000).amount(1500000).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R5(1L);
        assertFalse(result);
    }

    @Test
    public void testR5SecondHomeJustUnderMax() {
        CreditEntity credit = CreditEntity.builder().type(2).property_value(200000).amount(139000).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R5(1L);
        assertTrue(result);
    }

    @Test
    public void testR5MarketPropertyJustOverMax() {
        CreditEntity credit = CreditEntity.builder().type(3).property_value(300000).amount(190001).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R5(1L);
        assertFalse(result);
    }

    @Test
    public void testR5RemodelingExactlyAtMax() {
        CreditEntity credit = CreditEntity.builder().type(4).property_value(100000).amount(50000).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R5(1L);
        assertTrue(result);
    }

    @Test
    public void testR5FalseInvalidCreditType() {
        CreditEntity credit = CreditEntity.builder().type(5).property_value(100000).amount(30000).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);

        Boolean result = creditService.R5(1L);
        assertFalse(result);
    }

    //Test for R6
    @Test
    public void testR6Return() {
        CreditEntity credit = CreditEntity.builder().userId(1L).years(20.0).build();
        UserEntity user = new UserEntity();
        user.setBirthdate(new Date(System.currentTimeMillis() - (long) 30 * 365 * 24 * 60 * 60 * 1000)); // 30 years
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(userService.findUserById(1L)).thenReturn(user);
        when(userService.AgeInYears(user.getBirthdate())).thenReturn(30);

        Boolean result = creditService.R6(1L);
        assertNotNull(result);
    }
    @Test
    public void testR6True() {
        CreditEntity credit = CreditEntity.builder().userId(1L).years(20.0).build();
        UserEntity user = new UserEntity();
        user.setBirthdate(new Date(System.currentTimeMillis() - (long) 30 * 365 * 24 * 60 * 60 * 1000)); // 30 years
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(userService.findUserById(1L)).thenReturn(user);
        when(userService.AgeInYears(user.getBirthdate())).thenReturn(30);

        Boolean result = creditService.R6(1L);
        assertTrue(result);
    }

    @Test
    public void testR6False() {
        CreditEntity credit = CreditEntity.builder().userId(1L).years(50.0).build();
        UserEntity user = new UserEntity();
        user.setBirthdate(new Date(System.currentTimeMillis() - (long) 30 * 365 * 24 * 60 * 60 * 1000)); // 30 years
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(userService.findUserById(1L)).thenReturn(user);
        when(userService.AgeInYears(user.getBirthdate())).thenReturn(30);

        Boolean result = creditService.R6(1L);
        assertFalse(result);
    }

    @Test
    public void testR6_70() {
        CreditEntity credit = CreditEntity.builder().userId(1L).years(40.0).build();
        UserEntity user = new UserEntity();
        user.setBirthdate(new Date(System.currentTimeMillis() - (long) 30 * 365 * 24 * 60 * 60 * 1000)); // 30 years
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(userService.findUserById(1L)).thenReturn(user);
        when(userService.AgeInYears(user.getBirthdate())).thenReturn(30);

        Boolean result = creditService.R6(1L);
        assertFalse(result);
    }

    @Test
    public void testR6_69() {
        CreditEntity credit = CreditEntity.builder().userId(1L).years(39.0).build();
        UserEntity user = new UserEntity();
        user.setBirthdate(new Date(System.currentTimeMillis() - (long) 30 * 365 * 24 * 60 * 60 * 1000)); // 30 years
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(userService.findUserById(1L)).thenReturn(user);
        when(userService.AgeInYears(user.getBirthdate())).thenReturn(30);

        Boolean result = creditService.R6(1L);
        assertTrue(result);
    }

    @Test
    public void testR6_40() {
        CreditEntity credit = CreditEntity.builder().userId(1L).years(10.0).build();
        UserEntity user = new UserEntity();
        user.setBirthdate(new Date(System.currentTimeMillis() - (long) 30 * 365 * 24 * 60 * 60 * 1000)); // 30 years
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(userService.findUserById(1L)).thenReturn(user);
        when(userService.AgeInYears(user.getBirthdate())).thenReturn(30);

        Boolean result = creditService.R6(1L);
        assertTrue(result);
    }

    @Test
    public void testR6_90() {
        CreditEntity credit = CreditEntity.builder().userId(1L).years(60.0).build();
        UserEntity user = new UserEntity();
        user.setBirthdate(new Date(System.currentTimeMillis() - (long) 30 * 365 * 24 * 60 * 60 * 1000)); // 30 years
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(userService.findUserById(1L)).thenReturn(user);
        when(userService.AgeInYears(user.getBirthdate())).thenReturn(30);

        Boolean result = creditService.R6(1L);
        assertFalse(result);
    }

    //Test for E1
    @Test
    public void testE1Returns() {
        CreditEntity credit = CreditEntity.builder().idCredit(1L).type(1).capital(100000).annual_interest(5.0).years(15.0)
                .income(50000).level(1).property_value(200000).amount(150000).debt(20000).userId(1L).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(documentService.getDocuments(1L)).thenReturn(List.of(new DocumentEntity(), new DocumentEntity(), new DocumentEntity()));

        Boolean result = creditService.E1(1L);
        assertNotNull(result);
    }
    @Test
    public void testE1True() {
        CreditEntity credit = CreditEntity.builder().idCredit(1L).type(1).capital(100000).annual_interest(5.0).years(15.0)
                .income(50000).level(1).property_value(200000).amount(150000).debt(20000).userId(1L).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(documentService.getDocuments(1L)).thenReturn(List.of(new DocumentEntity(), new DocumentEntity(), new DocumentEntity()));

        Boolean result = creditService.E1(1L);
        assertTrue(result);
    }

    @Test
    public void testE1NoCreditId() {
        CreditEntity credit = CreditEntity.builder().type(1).capital(100000).annual_interest(5.0).years(15.0)
                .income(50000).level(1).property_value(200000).amount(150000).debt(20000).userId(1L).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(documentService.getDocuments(1L)).thenReturn(List.of(new DocumentEntity(), new DocumentEntity(), new DocumentEntity()));

        Boolean result = creditService.E1(1L);
        assertFalse(result);
    }

    @Test
    public void testE1False() {
        CreditEntity credit = CreditEntity.builder().idCredit(3L).type(null).capital(100000).annual_interest(5.0).years(15.0)
                .income(50000).level(1).property_value(200000).amount(150000).debt(20000).userId(1L).build();
        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(documentService.getDocuments(1L)).thenReturn(List.of(new DocumentEntity(), new DocumentEntity(), new DocumentEntity()));

        Boolean result = creditService.E1(1L);
        assertFalse(result);
    }

    @Test
    public void testE1SecondHomeInsufficientDocuments() {
        CreditEntity credit = CreditEntity.builder()
                .idCredit(1L)
                .type(2) // Second home
                .capital(100000)
                .annual_interest(5.0)
                .years(15.0)
                .income(50000)
                .level(1)
                .property_value(200000)
                .amount(150000)
                .debt(20000)
                .userId(1L)
                .build();

        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(documentService.getDocuments(1L)).thenReturn(List.of(new DocumentEntity(), new DocumentEntity())); // Only 2 documents

        Boolean result = creditService.E1(1L);
        assertFalse(result);
    }

    @Test
    public void testE1InvalidCreditType() {
        CreditEntity credit = CreditEntity.builder()
                .idCredit(1L)
                .type(5) // Invalid credit type
                .capital(100000)
                .annual_interest(5.0)
                .years(15.0)
                .income(50000)
                .level(1)
                .property_value(200000)
                .amount(150000)
                .debt(20000)
                .userId(1L)
                .build();

        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(documentService.getDocuments(1L)).thenReturn(List.of(new DocumentEntity(), new DocumentEntity(), new DocumentEntity())); // 3 documents

        Boolean result = creditService.E1(1L);
        assertFalse(result);
    }

    @Test
    public void testE1RemodelingNoDocuments() {
        CreditEntity credit = CreditEntity.builder()
                .idCredit(1L)
                .type(4) // Remodeling
                .capital(100000)
                .annual_interest(5.0)
                .years(15.0)
                .income(50000)
                .level(1)
                .property_value(200000)
                .amount(150000)
                .debt(20000)
                .userId(1L)
                .build();

        when(creditRepository.findByIdCredit(1L)).thenReturn(credit);
        when(documentService.getDocuments(1L)).thenReturn(List.of()); // No documents

        Boolean result = creditService.E1(1L);
        assertFalse(result);
    }

    //Test for delete
    @Test
    public void testDeleteCredit() throws Exception {
        Long creditId = 1L;
        doNothing().when(creditRepository).deleteById(creditId);

        Boolean deleted = creditService.deleteCredit(creditId);
        assertTrue(deleted);
        verify(creditRepository, times(1)).deleteById(creditId);
    }

    @Test
    void testDeleteCreditThrowsException() {
        doThrow(new RuntimeException("Credit not found")).when(creditRepository).deleteById(anyLong());
        Exception exception = assertThrows(Exception.class, () -> creditService.deleteCredit(1L));
        assertEquals("Credit not found", exception.getMessage());
    }

    @Test
    public void testDeleteCreditThrowsException2() throws Exception {
        Long creditId = 1L;
        doThrow(new RuntimeException("Database error")).when(creditRepository).deleteById(creditId);

        Exception exception = assertThrows(Exception.class, () -> {
            creditService.deleteCredit(creditId);
        });

        assertEquals("Database error", exception.getMessage());
        verify(creditRepository, times(1)).deleteById(creditId);
    }

    @Test
    public void testDeleteCreditNonExistentId() throws Exception {
        Long creditId = 999L; // Assume this ID does not exist
        doThrow(new RuntimeException("Credit with ID 999 does not exist")).when(creditRepository).deleteById(creditId);

        Exception exception = assertThrows(Exception.class, () -> creditService.deleteCredit(creditId));
        assertEquals("Credit with ID 999 does not exist", exception.getMessage());
        verify(creditRepository, times(1)).deleteById(creditId);
    }


    @Test
    public void testDeleteCreditInvalidArgument() throws Exception {
        Long creditId = -1L; // Invalid credit ID
        doThrow(new IllegalArgumentException("Invalid ID")).when(creditRepository).deleteById(creditId);

        Exception exception = assertThrows(Exception.class, () -> creditService.deleteCredit(creditId));
        assertEquals("Invalid ID", exception.getMessage());
        verify(creditRepository, times(1)).deleteById(creditId);
    }

    @Test
    public void testDeleteCreditLogsSuccess() throws Exception {
        Long creditId = 1L;
        doNothing().when(creditRepository).deleteById(creditId);

        creditService.deleteCredit(creditId);
    }

    @Test
    public void testDeleteCreditAlreadyDeleted() throws Exception {
        Long creditId = 1L;
        doThrow(new RuntimeException("Credit already deleted")).when(creditRepository).deleteById(creditId);

        Exception exception = assertThrows(Exception.class, () -> {
            creditService.deleteCredit(creditId);
        });

        assertEquals("Credit already deleted", exception.getMessage());
        verify(creditRepository, times(1)).deleteById(creditId);
    }

    //Test for update

    @Test
    public void testUpdateCreditWithNonExistentId() {
        Long nonExistentId = 999L;
        CreditEntity credit = new CreditEntity();

        when(creditRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            creditService.updateCredit(nonExistentId, credit);
        });

        verify(creditRepository, times(1)).existsById(nonExistentId);
    }
    @Test
    public void testUpdateCredit() {
        CreditEntity credit = CreditEntity.builder().idCredit(1L).capital(100000).build();
        when(creditRepository.existsById(1L)).thenReturn(true);
        when(creditRepository.save(any(CreditEntity.class))).thenReturn(credit);

        CreditEntity updatedCredit = creditService.updateCredit(1L, credit);
        assertNotNull(updatedCredit);
        assertEquals(100000, updatedCredit.getCapital());
    }

    @Test
    public void testStepUpCredit() {
        // Credit with e 2
        CreditEntity existingCredit = CreditEntity.builder().idCredit(1L).capital(100000).e(2).build();
        CreditEntity creditToUpdate = CreditEntity.builder().idCredit(1L).capital(100000).build();

        // We simulate that credit actually exist
        when(creditRepository.existsById(1L)).thenReturn(true);
        when(creditRepository.findById(1L)).thenReturn(Optional.of(existingCredit));

        // We simulate that saves makes the e +1
        when(creditRepository.save(any(CreditEntity.class))).thenAnswer(invocation -> {
            CreditEntity savedCredit = invocation.getArgument(0);
            savedCredit.setE(existingCredit.getE() + 1); // We make the e higher manually for test reasons
            return savedCredit;
        });

        CreditEntity updatedCredit = creditService.updateCredit(1L, creditToUpdate);

        // We simulate that saves makes the e +1
        assertNotNull(updatedCredit);
        assertEquals(100000, updatedCredit.getCapital());
        assertEquals(3, updatedCredit.getE()); // We verify the amount
    }

    @Test
    public void testChangeDebt() {
        // Credit with e 2
        CreditEntity existingCredit = CreditEntity.builder().idCredit(1L).capital(100000).debt(0).build();
        CreditEntity creditToUpdate = CreditEntity.builder().idCredit(1L).capital(100000).build();

        // We simulate that credit actually exist
        when(creditRepository.existsById(1L)).thenReturn(true);
        when(creditRepository.findById(1L)).thenReturn(Optional.of(existingCredit));

        // We simulate that saves makes the debt +1000
        when(creditRepository.save(any(CreditEntity.class))).thenAnswer(invocation -> {
            CreditEntity savedCredit = invocation.getArgument(0);
            savedCredit.setDebt(existingCredit.getDebt() + 1000); // We make the e higher manually for test reasons
            return savedCredit;
        });

        CreditEntity updatedCredit = creditService.updateCredit(1L, creditToUpdate);

        // We simulate that saves makes the debt +1000
        assertNotNull(updatedCredit);
        assertEquals(100000, updatedCredit.getCapital());
        assertEquals(1000, updatedCredit.getDebt()); // We verify the amount
    }

    @Test
    public void testLevelUpCredit() {
        // Credit with level 1
        CreditEntity existingCredit = CreditEntity.builder().idCredit(1L).capital(100000).level(1).build();
        CreditEntity creditToUpdate = CreditEntity.builder().idCredit(1L).capital(100000).build();

        // We simulate that credit actually exist
        when(creditRepository.existsById(1L)).thenReturn(true);
        when(creditRepository.findById(1L)).thenReturn(Optional.of(existingCredit));

        // We simulate that saves makes the level +1
        when(creditRepository.save(any(CreditEntity.class))).thenAnswer(invocation -> {
            CreditEntity savedCredit = invocation.getArgument(0);
            savedCredit.setLevel(existingCredit.getLevel() + 1); // We make the level higher manually for test reasons
            return savedCredit;
        });

        CreditEntity updatedCredit = creditService.updateCredit(1L, creditToUpdate);

        // We verify is the level up is right
        assertNotNull(updatedCredit);
        assertEquals(100000, updatedCredit.getCapital());
        assertEquals(2, updatedCredit.getLevel()); //  We verify the amount
    }

    @Test
    public void testChangeEToCancel() {
        // Credit with level 1
        CreditEntity existingCredit = CreditEntity.builder().idCredit(1L).capital(100000).e(4).build();
        CreditEntity creditToUpdate = CreditEntity.builder().idCredit(1L).capital(100000).build();

        // We simulate that credit actually exist
        when(creditRepository.existsById(1L)).thenReturn(true);
        when(creditRepository.findById(1L)).thenReturn(Optional.of(existingCredit));

        // We simulate that saves makes the e == 8
        when(creditRepository.save(any(CreditEntity.class))).thenAnswer(invocation -> {
            CreditEntity savedCredit = invocation.getArgument(0);
            savedCredit.setE(8); // We make the level higher manually for test reasons
            return savedCredit;
        });

        CreditEntity updatedCredit = creditService.updateCredit(1L, creditToUpdate);

        // We verify is the is right
        assertNotNull(updatedCredit);
        assertEquals(100000, updatedCredit.getCapital());
        assertEquals(8, updatedCredit.getE()); //  We verify the amount
    }

    @Test
    public void testChangeEDisapproved() {
        // Credit with level 1
        CreditEntity existingCredit = CreditEntity.builder().idCredit(1L).capital(100000).e(1).build();
        CreditEntity creditToUpdate = CreditEntity.builder().idCredit(1L).capital(100000).build();

        // We simulate that credit actually exist
        when(creditRepository.existsById(1L)).thenReturn(true);
        when(creditRepository.findById(1L)).thenReturn(Optional.of(existingCredit));

        // We simulate that saves makes the e == 7
        when(creditRepository.save(any(CreditEntity.class))).thenAnswer(invocation -> {
            CreditEntity savedCredit = invocation.getArgument(0);
            savedCredit.setE(7); // We make the level higher manually for test reasons
            return savedCredit;
        });

        CreditEntity updatedCredit = creditService.updateCredit(1L, creditToUpdate);

        // We verify is the is right
        assertNotNull(updatedCredit);
        assertEquals(100000, updatedCredit.getCapital());
        assertEquals(7, updatedCredit.getE()); //  We verify the amount
    }

    //Test for GetById
    @Test
    public void testGetCreditsSize() {
        List<CreditEntity> credits = List.of(new CreditEntity(), new CreditEntity());
        when(creditRepository.findByUserId(1L)).thenReturn(credits);

        List<CreditEntity> result = creditService.getCredits(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void testNoneCredits() {
        List<CreditEntity> credits = List.of();
        when(creditRepository.findByUserId(1L)).thenReturn(credits);

        List<CreditEntity> result = creditService.getCredits(1L);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetCreditsByUserId() {
        Long userId = 1L;
        CreditEntity credit1 = new CreditEntity();
        CreditEntity credit2 = new CreditEntity();
        when(creditRepository.findByUserId(userId)).thenReturn(Arrays.asList(credit1, credit2));

        List<CreditEntity> result = creditService.getCredits(userId);


        assertNotNull(result);
        assertEquals(2, result.size());
        verify(creditRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testGetCreditsUserIdMultipleCredits() {
        Long userId = 1L;
        CreditEntity credit1 = new CreditEntity();
        CreditEntity credit2 = new CreditEntity();
        List<CreditEntity> credits = Arrays.asList(credit1, credit2);

        when(creditRepository.findByUserId(userId)).thenReturn(credits);

        List<CreditEntity> result = creditService.getCredits(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(credit1, result.get(0));
        assertSame(credit2, result.get(1));
        verify(creditRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testGetCreditsNullUserIdReturnsEmptyList() {

        List<CreditEntity> result = creditService.getCredits(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(creditRepository, never()).findByUserId(anyLong());
    }

    @Test
    public void testGetCreditsUserIdSingleCredit() {
        Long userId = 1L;
        CreditEntity credit = new CreditEntity();

        when(creditRepository.findByUserId(userId)).thenReturn(Collections.singletonList(credit));

        List<CreditEntity> result = creditService.getCredits(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(credit, result.get(0));
        verify(creditRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testGetCreditsUserIdWithDifferentCredits() {
        Long userId = 2L; // User ID that does not exist
        CreditEntity credit1 = new CreditEntity(); // Assume this credit is for user ID 1
        List<CreditEntity> credits = Arrays.asList(credit1);

        when(creditRepository.findByUserId(userId)).thenReturn(credits);

        List<CreditEntity> result = creditService.getCredits(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(credit1, result.get(0));
        verify(creditRepository, times(1)).findByUserId(userId);
    }

    //Test for getAllcredit withoutid
    @Test
    public void testGetAllCredits() {
        // Arrange
        CreditEntity credit1 = new CreditEntity(); // Initialize with proper fields
        CreditEntity credit2 = new CreditEntity(); // Initialize with proper fields
        when(creditRepository.findAll()).thenReturn(Arrays.asList(credit1, credit2));

        // Act
        List<CreditEntity> result = creditService.getCredits();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(creditRepository, times(1)).findAll();
    }

    @Test
    public void testGetCreditsEmptyList() {
        when(creditRepository.findAll()).thenReturn(Collections.emptyList());

        List<CreditEntity> result = creditService.getCredits();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(creditRepository, times(1)).findAll();
    }

    @Test
    public void testGetCreditsSingleCredit() {
        CreditEntity credit = new CreditEntity(); // Initialize with proper fields
        when(creditRepository.findAll()).thenReturn(Collections.singletonList(credit));

        List<CreditEntity> result = creditService.getCredits();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(credit, result.get(0));
        verify(creditRepository, times(1)).findAll();
    }

    @Test
    public void testGetCreditsMultipleCredits() {
        CreditEntity credit1 = new CreditEntity(); // Initialize with proper fields
        CreditEntity credit2 = new CreditEntity(); // Initialize with proper fields
        when(creditRepository.findAll()).thenReturn(Arrays.asList(credit1, credit2));

        List<CreditEntity> result = creditService.getCredits();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(credit1));
        assertTrue(result.contains(credit2));
        verify(creditRepository, times(1)).findAll();
    }

    @Test
    public void testGetCreditsRepositoryCalledOnce() {
        CreditEntity credit1 = new CreditEntity();
        CreditEntity credit2 = new CreditEntity();
        when(creditRepository.findAll()).thenReturn(Arrays.asList(credit1, credit2));

        creditService.getCredits();

        verify(creditRepository, times(1)).findAll();
    }

    @Test
    public void testGetCreditsThrowsException() {
        when(creditRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> creditService.getCredits());
        assertEquals("Database error", exception.getMessage());
        verify(creditRepository, times(1)).findAll();
    }

    @Test
    public void testGetCreditsReturnType() {
        CreditEntity credit1 = new CreditEntity(); // Initialize with proper fields
        when(creditRepository.findAll()).thenReturn(Arrays.asList(credit1));

        List<CreditEntity> result = creditService.getCredits();

        assertNotNull(result);
        assertTrue(result instanceof List<?>);
        assertEquals(CreditEntity.class, result.get(0).getClass());
        verify(creditRepository, times(1)).findAll();
    }

    //Test for Financing

    @Test
    public void testFinancingReturn(){
        Integer result = creditService.financing(1000,100);
        assertNotNull(result);
    }

    @Test
    public void testFinancing_10(){
        Integer result = creditService.financing(1000,100);
        assertEquals(result, 10);
    }

    @Test
    public void testFinancing_1(){
        Integer result = creditService.financing(10000,100);
        assertEquals(result, 1);
    }

    @Test
    public void testFinancing_100(){
        Integer result = creditService.financing(100,100);
        assertEquals(result, 100);
    }

    @Test
    public void testFinancing_1000(){
        Integer result = creditService.financing(10,100);
        assertEquals(result, 1000);
    }

    @Test
    public void testFinancing_minus1000(){
        Integer result = creditService.financing(-10,100);
        assertEquals(result, -1000);
    }

    @Test
    public void testFinancing_0(){
        Integer result = creditService.financing(-10,100);
        assertNotEquals(0, result);
    }

}