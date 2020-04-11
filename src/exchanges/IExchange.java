package exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.service.marketdata.params.CurrencyPairsParam;

public interface IExchange {

	public String getName() throws IOException;

	public Exchange getExchange();

	public OrderBook getOrderBook(CurrencyPair curr) throws IOException;

	public Ticker getTicker(CurrencyPair curr) throws IOException;

	public List<Ticker> getTickers(CurrencyPairsParam pairsParam) throws IOException;

	public Trades getTrades(CurrencyPair curr) throws IOException;

	public ArrayList<CurrencyPair> getCurrencyPairs() throws IOException;

}
