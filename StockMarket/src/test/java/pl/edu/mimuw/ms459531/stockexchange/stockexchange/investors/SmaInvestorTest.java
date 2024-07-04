package pl.edu.mimuw.ms459531.stockexchange.stockexchange.investors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pl.edu.mimuw.ms459531.stockexchange.companies.StockCompany;
import pl.edu.mimuw.ms459531.stockexchange.core.ShareVm;
import pl.edu.mimuw.ms459531.stockexchange.investors.*;
import pl.edu.mimuw.ms459531.stockexchange.orders.Order;
import pl.edu.mimuw.ms459531.stockexchange.orders.OrderType;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SmaInvestorTest {

    @Test
    void noSignal() {
        // Arrange
        SmaCalculator smaCalculator = mock(SmaCalculator.class);
        StockCompany company = new StockCompany(0, "ABCDE");
        Random random = mock(Random.class);
        SmaInvestor smaInvestor = new SmaInvestor(smaCalculator, random);
        ITransactionInfoProvider transactionInfoProvider = mock(ITransactionInfoProvider.class);

        when(transactionInfoProvider.getStock(anyInt())).thenReturn(company);
        when(transactionInfoProvider.getCurrentRoundNo()).thenReturn(0);
        when(transactionInfoProvider.getLastTransactionPrice(anyInt())).thenReturn(100);
        when(smaCalculator.getSignal(anyInt(), eq(transactionInfoProvider))).thenReturn(SmaSignal.NONE);

        InvestorWalletVm wallet = mock(InvestorWalletVm.class);
        when(wallet.getStocksCount()).thenReturn(3);
        when(wallet.getShares(anyInt())).thenReturn(new ShareVm(company, 100));
        when(wallet.getFunds()).thenReturn(1000);

        doThrow(IllegalStateException.class).when(transactionInfoProvider).addOrder(any(), any());

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> smaInvestor.makeOrder(transactionInfoProvider, wallet));
    }

    @Test
    void buySignal() {
        // Arrange
        SmaCalculator smaCalculator = mock(SmaCalculator.class);
        StockCompany company = new StockCompany(0, "ABCDE");
        Random random = mock(Random.class);
        SmaInvestor smaInvestor = new SmaInvestor(smaCalculator, random);
        ITransactionInfoProvider transactionInfoProvider = mock(ITransactionInfoProvider.class);

        when(transactionInfoProvider.getStock(anyInt())).thenReturn(company);
        when(transactionInfoProvider.getCurrentRoundNo()).thenReturn(0);
        when(transactionInfoProvider.getLastTransactionPrice(anyInt())).thenReturn(100);
        when(smaCalculator.getSignal(anyInt(), eq(transactionInfoProvider))).thenReturn(SmaSignal.NONE, SmaSignal.BUY, SmaSignal.NONE);

        InvestorWalletVm wallet = mock(InvestorWalletVm.class);
        when(wallet.getStocksCount()).thenReturn(3);
        when(wallet.getShares(anyInt())).thenReturn(new ShareVm(company, 100));
        when(wallet.getFunds()).thenReturn(1000);
        when(random.nextInt(90, 111)).thenReturn(100);
        when(random.nextDouble()).thenReturn(0.2);

        ArgumentCaptor<Order> valueCapture = ArgumentCaptor.forClass(Order.class);
        doNothing().when(transactionInfoProvider).addOrder(any(), valueCapture.capture());

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> smaInvestor.makeOrder(transactionInfoProvider, wallet));

        Order order = valueCapture.getValue();
        Assertions.assertEquals(0, order.getInvestorId());
        Assertions.assertEquals(OrderType.BUY, order.getType());
        Assertions.assertEquals(company, order.getStockCompany());
        Assertions.assertEquals(10, order.getAmount());
        Assertions.assertEquals(100, order.getLimit());
    }

    @Test
    void noFunds() {
        // Arrange
        SmaCalculator smaCalculator = mock(SmaCalculator.class);
        StockCompany company = new StockCompany(0, "ABCDE");
        Random random = mock(Random.class);
        SmaInvestor smaInvestor = new SmaInvestor(smaCalculator, random);
        ITransactionInfoProvider transactionInfoProvider = mock(ITransactionInfoProvider.class);

        when(transactionInfoProvider.getStock(anyInt())).thenReturn(company);
        when(transactionInfoProvider.getCurrentRoundNo()).thenReturn(0);
        when(transactionInfoProvider.getLastTransactionPrice(anyInt())).thenReturn(100);
        when(smaCalculator.getSignal(anyInt(), eq(transactionInfoProvider))).thenReturn(SmaSignal.BUY, SmaSignal.NONE, SmaSignal.NONE);

        InvestorWalletVm wallet = mock(InvestorWalletVm.class);
        when(wallet.getStocksCount()).thenReturn(3);
        when(wallet.getShares(anyInt())).thenReturn(new ShareVm(company, 100));
        when(wallet.getFunds()).thenReturn(89);

        doThrow(IllegalStateException.class).when(transactionInfoProvider).addOrder(any(), any());

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> smaInvestor.makeOrder(transactionInfoProvider, wallet));
    }

    @Test
    void noStock() {
        // Arrange
        SmaCalculator smaCalculator = mock(SmaCalculator.class);
        StockCompany company = new StockCompany(0, "ABCDE");
        Random random = mock(Random.class);
        SmaInvestor smaInvestor = new SmaInvestor(smaCalculator, random);
        ITransactionInfoProvider transactionInfoProvider = mock(ITransactionInfoProvider.class);

        when(transactionInfoProvider.getStock(anyInt())).thenReturn(company);
        when(transactionInfoProvider.getCurrentRoundNo()).thenReturn(0);
        when(transactionInfoProvider.getLastTransactionPrice(anyInt())).thenReturn(100);
        when(smaCalculator.getSignal(anyInt(), eq(transactionInfoProvider))).thenReturn(SmaSignal.NONE, SmaSignal.NONE, SmaSignal.SELL);

        InvestorWalletVm wallet = mock(InvestorWalletVm.class);
        when(wallet.getStocksCount()).thenReturn(3);
        when(wallet.getShares(anyInt())).thenReturn(new ShareVm(company, 0));
        when(wallet.getFunds()).thenReturn(1000);

        doThrow(IllegalStateException.class).when(transactionInfoProvider).addOrder(any(), any());

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> smaInvestor.makeOrder(transactionInfoProvider, wallet));
    }

    @Test
    void sellSignal() {
        // Arrange
        SmaCalculator smaCalculator = mock(SmaCalculator.class);
        StockCompany company = new StockCompany(0, "ABCDE");
        Random random = mock(Random.class);
        SmaInvestor smaInvestor = new SmaInvestor(smaCalculator, random);
        ITransactionInfoProvider transactionInfoProvider = mock(ITransactionInfoProvider.class);

        when(transactionInfoProvider.getStock(anyInt())).thenReturn(company);
        when(transactionInfoProvider.getCurrentRoundNo()).thenReturn(0);
        when(transactionInfoProvider.getLastTransactionPrice(anyInt())).thenReturn(100);
        when(smaCalculator.getSignal(anyInt(), eq(transactionInfoProvider))).thenReturn(SmaSignal.NONE, SmaSignal.SELL, SmaSignal.NONE);

        InvestorWalletVm wallet = mock(InvestorWalletVm.class);
        when(wallet.getStocksCount()).thenReturn(3);
        when(wallet.getShares(anyInt())).thenReturn(new ShareVm(company, 100));
        when(wallet.getFunds()).thenReturn(1000);
        when(random.nextInt(90, 111)).thenReturn(100);
        when(random.nextInt(100)).thenReturn(9);
        when(random.nextDouble()).thenReturn(0.2);

        ArgumentCaptor<Order> valueCapture = ArgumentCaptor.forClass(Order.class);
        doNothing().when(transactionInfoProvider).addOrder(any(), valueCapture.capture());

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> smaInvestor.makeOrder(transactionInfoProvider, wallet));

        Order order = valueCapture.getValue();
        Assertions.assertEquals(0, order.getInvestorId());
        Assertions.assertEquals(OrderType.SALE, order.getType());
        Assertions.assertEquals(company, order.getStockCompany());
        Assertions.assertEquals(10, order.getAmount());
        Assertions.assertEquals(100, order.getLimit());
    }
}
