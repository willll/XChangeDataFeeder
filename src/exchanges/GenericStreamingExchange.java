package exchanges;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;

import info.bitrich.xchangestream.core.StreamingExchange;

/**
 * @author will
 *
 */
public abstract class GenericStreamingExchange extends GenericExchange implements IStreamingExchange {

	protected StreamingExchange streamingExchange;

	protected boolean tickerSupported = true;
	protected boolean orderbookupported = true;
	protected boolean tradeSupported = true;

	/**
	 * 
	 */
	public GenericStreamingExchange() {
		super();
		this.streamingExchange = null; // Should be set in the daughter classes
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.IStreamingExchange#isOrderbookupported()
	 */
	@Override
	public boolean isOrderbookupported() {
		return orderbookupported;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.IStreamingExchange#isTickerSupported()
	 */
	@Override
	public boolean isTickerSupported() {
		return tickerSupported;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.IStreamingExchange#isTradeSupported()
	 */
	@Override
	public boolean isTradeSupported() {
		return tradeSupported;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.IStreamingExchange#getStreamingExchange()
	 */
	@Override
	public StreamingExchange getStreamingExchange() {
		return this.streamingExchange;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.IStreamingExchange#getStreamingName()
	 */
	@Override
	public String getStreamingName() throws IOException {
		return this.streamingExchange.getDefaultExchangeSpecification().getExchangeName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * exchanges.IStreamingExchange#getStreamingOrderBook(org.knowm.xchange.currency
	 * .CurrencyPair)
	 */
	@Override
	public OrderBook getStreamingOrderBook(CurrencyPair curr) throws IOException {
		return this.streamingExchange.getMarketDataService().getOrderBook(curr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * exchanges.IStreamingExchange#getStreamingTicker(org.knowm.xchange.currency.
	 * CurrencyPair)
	 */
	@Override
	public Ticker getStreamingTicker(CurrencyPair curr) throws IOException {
		return this.streamingExchange.getMarketDataService().getTicker(curr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * exchanges.IStreamingExchange#getStreamingTrades(org.knowm.xchange.currency.
	 * CurrencyPair)
	 */
	@Override
	public Trades getStreamingTrades(CurrencyPair curr) throws IOException {
		return this.streamingExchange.getMarketDataService().getTrades(curr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.IStreamingExchange#getStreamingCurrencyPairs()
	 */
	@Override
	public Set<CurrencyPair> getStreamingCurrencyPairs() throws IOException {
		return new HashSet<CurrencyPair>(this.streamingExchange.getExchangeSymbols());
	}

}
