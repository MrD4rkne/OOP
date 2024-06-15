package stockMarket.investors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import stockMarket.companies.StockCompany;
import stockMarket.core.ShareVm;
import stockMarket.orders.Order;
import stockMarket.orders.OrderType;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SmaInvestorTest {
    @Test
    void noSignal(){
        // Arrange
        SmaCalculator smaCalculator = mock(SmaCalculator.class);

        StockCompany company = new StockCompany(0, "ABCDE");

        Random random = mock(Random.class);
        SmaInvestor smaInvestor = new SmaInvestor(smaCalculator,random);
        ITransactionInfoProvider transactionInfoProvider = mock(ITransactionInfoProvider.class);
        when(transactionInfoProvider.getStock(0)).thenReturn(company);
        when(transactionInfoProvider.getStock(1)).thenReturn(company);
        when(transactionInfoProvider.getStock(2)).thenReturn(company);
        
        when(transactionInfoProvider.getCurrentRoundNo()).thenReturn(0);
        when(transactionInfoProvider.getLastTransactionPrice(0)).thenReturn(100);
        when(smaCalculator.getSignal(0, transactionInfoProvider)).thenReturn(SmaSignal.NONE);
        when(smaCalculator.getSignal(1, transactionInfoProvider)).thenReturn(SmaSignal.NONE);
        when(smaCalculator.getSignal(2, transactionInfoProvider)).thenReturn(SmaSignal.NONE);
        
        InvestorWalletVm wallet = mock(InvestorWalletVm.class);
        when(wallet.getStocksCount()).thenReturn(3);
        when(wallet.getShares(0)).thenReturn(new ShareVm(company, 100));
        when(wallet.getShares(1)).thenReturn(new ShareVm(company, 100));
        when(wallet.getShares(2)).thenReturn(new ShareVm(company, 100));
        when(wallet.getFunds()).thenReturn(1000);
        
        doThrow(IllegalStateException.class).when(transactionInfoProvider).addOrder(any(), any());
        
        // Act & assert
        Assertions.assertDoesNotThrow(()->{
            smaInvestor.makeOrder(transactionInfoProvider, wallet);
        });
    }

    @Test
    void buySignal(){
        // Arrange
        SmaCalculator smaCalculator = mock(SmaCalculator.class);

        StockCompany company = new StockCompany(0, "ABCDE");

        Random random = mock(Random.class);

        SmaInvestor smaInvestor = new SmaInvestor(smaCalculator,random);
        ITransactionInfoProvider transactionInfoProvider = mock(ITransactionInfoProvider.class);
        when(transactionInfoProvider.getStock(0)).thenReturn(company);
        when(transactionInfoProvider.getStock(1)).thenReturn(company);
        when(transactionInfoProvider.getStock(2)).thenReturn(company);

        when(transactionInfoProvider.getCurrentRoundNo()).thenReturn(0);
        when(transactionInfoProvider.getLastTransactionPrice(0)).thenReturn(100);
        when(transactionInfoProvider.getLastTransactionPrice(1)).thenReturn(100);
        when(transactionInfoProvider.getLastTransactionPrice(2)).thenReturn(100);
        
        when(smaCalculator.getSignal(0, transactionInfoProvider)).thenReturn(SmaSignal.NONE);
        when(smaCalculator.getSignal(1, transactionInfoProvider)).thenReturn(SmaSignal.BUY);
        when(smaCalculator.getSignal(2, transactionInfoProvider)).thenReturn(SmaSignal.NONE);

        InvestorWalletVm wallet = mock(InvestorWalletVm.class);
        when(wallet.getStocksCount()).thenReturn(3);
        when(wallet.getShares(0)).thenReturn(new ShareVm(company, 100));
        when(wallet.getShares(1)).thenReturn(new ShareVm(company, 100));
        when(wallet.getShares(2)).thenReturn(new ShareVm(company, 100));
        when(wallet.getFunds()).thenReturn(1000);
        
        when(random.nextInt(100-10, 100+10 + 1)).thenReturn(100);
        when(random.nextDouble()).thenReturn(0.2);

        ArgumentCaptor<Order> valueCapture = ArgumentCaptor.forClass(Order.class);
        
        doNothing().when(transactionInfoProvider).addOrder(any(),valueCapture.capture());

        // Act & assert
        Assertions.assertDoesNotThrow(()->{
            smaInvestor.makeOrder(transactionInfoProvider, wallet);
        });

        Order order = valueCapture.getValue();
        Assertions.assertEquals(0, order.getInvestorId());
        Assertions.assertEquals(OrderType.BUY, order.getType());
        Assertions.assertEquals(company, order.getStockCompany());
        Assertions.assertEquals(10, order.getAmount());
        Assertions.assertEquals(100, order.getLimit());
    }

    @Test
    void noFunds(){
        // Arrange
        SmaCalculator smaCalculator = mock(SmaCalculator.class);

        StockCompany company = new StockCompany(0, "ABCDE");

        Random random = mock(Random.class);
        SmaInvestor smaInvestor = new SmaInvestor(smaCalculator,random);
        ITransactionInfoProvider transactionInfoProvider = mock(ITransactionInfoProvider.class);
        when(transactionInfoProvider.getStock(0)).thenReturn(company);
        when(transactionInfoProvider.getStock(1)).thenReturn(company);
        when(transactionInfoProvider.getStock(2)).thenReturn(company);

        when(transactionInfoProvider.getCurrentRoundNo()).thenReturn(0);
        when(transactionInfoProvider.getLastTransactionPrice(0)).thenReturn(100);
        when(transactionInfoProvider.getLastTransactionPrice(1)).thenReturn(100);
        when(transactionInfoProvider.getLastTransactionPrice(2)).thenReturn(100);
        when(smaCalculator.getSignal(0, transactionInfoProvider)).thenReturn(SmaSignal.BUY);
        when(smaCalculator.getSignal(1, transactionInfoProvider)).thenReturn(SmaSignal.NONE);
        when(smaCalculator.getSignal(2, transactionInfoProvider)).thenReturn(SmaSignal.NONE);

        InvestorWalletVm wallet = mock(InvestorWalletVm.class);
        when(wallet.getStocksCount()).thenReturn(3);
        when(wallet.getShares(0)).thenReturn(new ShareVm(company, 100));
        when(wallet.getShares(1)).thenReturn(new ShareVm(company, 100));
        when(wallet.getShares(2)).thenReturn(new ShareVm(company, 100));
        when(wallet.getFunds()).thenReturn(100-10-1);

        doThrow(IllegalStateException.class).when(transactionInfoProvider).addOrder(any(), any());

        // Act & assert
        Assertions.assertDoesNotThrow(()->{
            smaInvestor.makeOrder(transactionInfoProvider, wallet);
        });
    }

    @Test
    void noStock(){
        // Arrange
        SmaCalculator smaCalculator = mock(SmaCalculator.class);

        StockCompany company = new StockCompany(0, "ABCDE");

        Random random = mock(Random.class);
        SmaInvestor smaInvestor = new SmaInvestor(smaCalculator,random);
        ITransactionInfoProvider transactionInfoProvider = mock(ITransactionInfoProvider.class);
        when(transactionInfoProvider.getStock(0)).thenReturn(company);
        when(transactionInfoProvider.getStock(1)).thenReturn(company);
        when(transactionInfoProvider.getStock(2)).thenReturn(company);

        when(transactionInfoProvider.getCurrentRoundNo()).thenReturn(0);
        when(transactionInfoProvider.getLastTransactionPrice(0)).thenReturn(100);
        when(transactionInfoProvider.getLastTransactionPrice(1)).thenReturn(100);
        when(transactionInfoProvider.getLastTransactionPrice(2)).thenReturn(100);
        when(smaCalculator.getSignal(0, transactionInfoProvider)).thenReturn(SmaSignal.NONE);
        when(smaCalculator.getSignal(1, transactionInfoProvider)).thenReturn(SmaSignal.NONE);
        when(smaCalculator.getSignal(2, transactionInfoProvider)).thenReturn(SmaSignal.SELL);

        InvestorWalletVm wallet = mock(InvestorWalletVm.class);
        when(wallet.getStocksCount()).thenReturn(3);
        when(wallet.getShares(0)).thenReturn(new ShareVm(company, 100));
        when(wallet.getShares(1)).thenReturn(new ShareVm(company, 100));
        when(wallet.getShares(2)).thenReturn(new ShareVm(company, 0));
        when(wallet.getFunds()).thenReturn(1000);

        doThrow(IllegalStateException.class).when(transactionInfoProvider).addOrder(any(), any());

        // Act & assert
        Assertions.assertDoesNotThrow(()->{
            smaInvestor.makeOrder(transactionInfoProvider, wallet);
        });
    }

    @Test
    void sellSignal(){
        // Arrange
        SmaCalculator smaCalculator = mock(SmaCalculator.class);

        StockCompany company = new StockCompany(0, "ABCDE");

        Random random = mock(Random.class);

        SmaInvestor smaInvestor = new SmaInvestor(smaCalculator,random);
        ITransactionInfoProvider transactionInfoProvider = mock(ITransactionInfoProvider.class);
        when(transactionInfoProvider.getStock(0)).thenReturn(company);
        when(transactionInfoProvider.getStock(1)).thenReturn(company);
        when(transactionInfoProvider.getStock(2)).thenReturn(company);

        when(transactionInfoProvider.getCurrentRoundNo()).thenReturn(0);
        when(transactionInfoProvider.getLastTransactionPrice(0)).thenReturn(100);
        when(transactionInfoProvider.getLastTransactionPrice(1)).thenReturn(100);
        when(transactionInfoProvider.getLastTransactionPrice(2)).thenReturn(100);

        when(smaCalculator.getSignal(0, transactionInfoProvider)).thenReturn(SmaSignal.NONE);
        when(smaCalculator.getSignal(1, transactionInfoProvider)).thenReturn(SmaSignal.SELL);
        when(smaCalculator.getSignal(2, transactionInfoProvider)).thenReturn(SmaSignal.NONE);

        InvestorWalletVm wallet = mock(InvestorWalletVm.class);
        when(wallet.getStocksCount()).thenReturn(3);
        when(wallet.getShares(0)).thenReturn(new ShareVm(company, 100));
        when(wallet.getShares(1)).thenReturn(new ShareVm(company, 100));
        when(wallet.getShares(2)).thenReturn(new ShareVm(company, 100));
        when(wallet.getFunds()).thenReturn(1000);

        when(random.nextInt(100-10, 100+10 + 1)).thenReturn(100);
        when(random.nextInt(100)).thenReturn(9);
        when(random.nextDouble()).thenReturn(0.2);

        ArgumentCaptor<Order> valueCapture = ArgumentCaptor.forClass(Order.class);

        doNothing().when(transactionInfoProvider).addOrder(any(),valueCapture.capture());

        // Act & assert
        Assertions.assertDoesNotThrow(()->{
            smaInvestor.makeOrder(transactionInfoProvider, wallet);
        });

        Order order = valueCapture.getValue();
        Assertions.assertEquals(0, order.getInvestorId());
        Assertions.assertEquals(OrderType.SALE, order.getType());
        Assertions.assertEquals(company, order.getStockCompany());
        Assertions.assertEquals(10, order.getAmount());
        Assertions.assertEquals(100, order.getLimit());
    }
}
