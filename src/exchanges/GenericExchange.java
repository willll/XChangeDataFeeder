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

import features.ILogger;

/**
 * @author will
 *
 */
public abstract class GenericExchange implements IExchange, ILogger {

	protected Exchange exchange;

	/**
	 * 
	 */
	public GenericExchange() {
		super();
		this.exchange = null; // Should be implemented in the daughter classes
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.IExchange#getExchange()
	 */
	@Override
	public Exchange getExchange() {
		return this.exchange;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.IExchange#getName()
	 */
	@Override
	public String getName() throws IOException {
		return this.exchange.getDefaultExchangeSpecification().getExchangeName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * exchanges.IExchange#getOrderBook(org.knowm.xchange.currency.CurrencyPair)
	 */
	@Override
	public OrderBook getOrderBook(CurrencyPair curr) throws IOException {
		return this.exchange.getMarketDataService().getOrderBook(curr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.IExchange#getTicker(org.knowm.xchange.currency.CurrencyPair)
	 */
	@Override
	public Ticker getTicker(CurrencyPair curr) throws IOException {
		return this.exchange.getMarketDataService().getTicker(curr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.IExchange#getTicker(org.knowm.xchange.currency.CurrencyPair)
	 */
	@Override
	public List<Ticker> getTickers(CurrencyPairsParam pairsParam) throws IOException {
		return this.exchange.getMarketDataService().getTickers(pairsParam);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.IExchange#getTrades(org.knowm.xchange.currency.CurrencyPair)
	 */
	@Override
	public Trades getTrades(CurrencyPair curr) throws IOException {
		return this.exchange.getMarketDataService().getTrades(curr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.IExchange#getCurrencyPairs()
	 */
	@Override
	public ArrayList<CurrencyPair> getCurrencyPairs() throws IOException {
		try {
			return new ArrayList<CurrencyPair>(this.exchange.getExchangeSymbols());
		} catch (NullPointerException e) {
			logger.error(this.exchange.toString() + " : getCurrencyPairs failed !");
			return new ArrayList<CurrencyPair>();
		}
	}

}
