package exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coinmarketcap.pro.v1.CmcExchange;
import org.knowm.xchange.coinmarketcap.pro.v1.dto.marketdata.CmcCurrency;
import org.knowm.xchange.coinmarketcap.pro.v1.service.CmcMarketDataService;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;

import utils.Config;
import utils.Constants;

public class Coinmarketcap extends GenericExchange {
	public Coinmarketcap() {
		try {
			this.exchange = ExchangeFactory.INSTANCE.createExchange(CmcExchange.class.getName(),
					Config.getInstance().get(Constants.coinmarketcap_API),
					Config.getInstance().get(Constants.coinmarketcap_secret));
		} catch (IOException e) {
			logger.error("Could not access to : " + e.getMessage() + " Cannot read property file");
			this.exchange = ExchangeFactory.INSTANCE.createExchange(CmcExchange.class.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.IExchange#getCurrencyPairs()
	 */
	/*
	 * @Override public ArrayList<CurrencyPair> getCurrencyPairs() throws
	 * IOException {
	 * 
	 * List<CmcCurrency> currencyList = ((CmcMarketDataService)
	 * this.exchange.getMarketDataService()) .getCmcCurrencyList();
	 * ArrayList<CurrencyPair> res = new ArrayList<CurrencyPair>();
	 * 
	 * for (CmcCurrency currency : currencyList) { if (currency.isActive() &&
	 * !currency.getSymbol().equals("USD")) res.add(new
	 * CurrencyPair(Currency.getInstance(currency.getSymbol()), Currency.USD)); }
	 * 
	 * return res; }
	 */
}
