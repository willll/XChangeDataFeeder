package exchanges;

import java.io.IOException;
import java.util.ArrayList;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;

import info.bitrich.xchangestream.core.StreamingExchange;

public interface IStreamingExchange {

	StreamingExchange getStreamingExchange();

	String getStreamingName() throws IOException;

	boolean isTickerSupported();

	boolean isOrderbookupported();

	boolean isTradeSupported();

	OrderBook getStreamingOrderBook(CurrencyPair curr) throws IOException;

	Ticker getStreamingTicker(CurrencyPair curr) throws IOException;

	Trades getStreamingTrades(CurrencyPair curr) throws IOException;

	ArrayList<CurrencyPair> getStreamingCurrencyPairs() throws IOException;

}
