package exchanges.factories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.ZContext;

import exchanges.factories.EntryPoint.Exchanges;
import features.ILogger;
import tasks.OrderBookPublisherTask;
import tasks.TickerPublisherTask;
import utils.Config;
import utils.Constants;
import utils.CurrencyPairs;

public abstract class GenericFactory implements IFactory, IPollingFactory, ILogger {

	protected Exchanges exchange;
	protected String ticker_pub;
	protected String orderbook_pub;
	protected Boolean can_handle_multiple_threads = true;
	protected long refresh_rate = Constants.EXCHANGE_UPDATE_DELAY;

	/**
	 * @param _gf
	 * @return
	 * @throws IOException
	 */
	public static Boolean isEnabled(GenericFactory _gf) throws IOException {
		return Config.isEnabled(_gf.getEnabled());
	}
	
	/**
	 * @param _gf
	 * @return
	 * @throws IOException
	 */
	public static Boolean isTickerEnabled(GenericFactory _gf) throws IOException {
		return Config.isEnabled(_gf.getTickerEnabled());
	}
	
	/**
	 * @param _gf
	 * @return
	 * @throws IOException
	 */
	public static Boolean isOrderbookEnabled(GenericFactory _gf) throws IOException {
		return Config.isEnabled(_gf.getOrderbookEnabled());
	}
	
	/**
	 * @param _refresh_rate
	 */
	public void setRefreshRate(long _refresh_rate) {
		this.refresh_rate = _refresh_rate;
	}
	
	/**
	 * @return
	 */
	public Exchanges getExchange() {
		return this.exchange;
	}
	
	/**
	 * @author will
	 *
	 * @param <T>
	 */
	@SuppressWarnings("unused")
	protected static class ticker_publisher_task<T> extends TickerPublisherTask<T> implements Runnable {
		public ticker_publisher_task(T exchange, Set<CurrencyPair> currencyPair, ZContext context,
				final String pub, final long refresh_rate) {

			super(pub, exchange, currencyPair, context, refresh_rate);
		}
	}

	/**
	 * @author will
	 *
	 * @param <T>
	 */
	@SuppressWarnings("unused")
	protected static class Orderbook_publisher_task<T> extends OrderBookPublisherTask<T> implements Runnable {
		public Orderbook_publisher_task(T exchange, Set<CurrencyPair> currencyPair, ZContext context,
				final String pub, final long refresh_rate) {

			super(pub, exchange, currencyPair, context, refresh_rate);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * exchanges.factories.IFactory#create_orderbook_feeders(exchanges.factories.
	 * EntryPoint, org.zeromq.ZContext, java.util.ArrayList)
	 */
	@SuppressWarnings("unused")
	public ArrayList<Thread> create_orderbook_feeders(EntryPoint ep, ZContext context, Set<CurrencyPair> cp)
			throws IOException {
		int max_cp_per_thread = Constants.CP_PER_THREAD;
		if (!can_handle_multiple_threads)
			max_cp_per_thread = cp.size();

		ArrayList<Thread> thds = new ArrayList<Thread>();
		for (; cp.size() != 0;) {
			int lastElt = cp.size() > max_cp_per_thread ? max_cp_per_thread : cp.size();
			Set<CurrencyPair> tmp = cp.stream()
				    .limit(lastElt)
				    .collect(Collectors.toSet());
			
			cp = cp.stream()
				   .skip(lastElt) // the offset
				   .collect(Collectors.toSet());
			logger.debug(exchange.name() + " : Create new Thread : " + tmp.toString());
			Thread thd = new Thread(new Orderbook_publisher_task<exchanges.IExchange>(ep.getExchange(exchange), tmp,
					context, orderbook_pub, refresh_rate));
			thds.add(thd);
			thd.start();
		}
		return thds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see exchanges.factories.IFactory#create_ticker_feeders(exchanges.factories.
	 * EntryPoint, org.zeromq.ZContext, java.util.ArrayList)
	 */
	@SuppressWarnings("unused")
	public ArrayList<Thread> create_ticker_feeders(EntryPoint ep, ZContext context, Set<CurrencyPair> cp)
			throws IOException {

		return create_ticker_feeders(ep, context, cp, this.exchange, this.ticker_pub);
	}

	/**
	 * @param ep
	 * @param context
	 * @param cp
	 * @param exchange
	 * @param ticker_pub
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	protected ArrayList<Thread> create_ticker_feeders(EntryPoint ep, ZContext context, Set<CurrencyPair> cp,
			Exchanges exchange, String ticker_pub) throws IOException {

		int max_cp_per_thread = Constants.CP_PER_THREAD;
		if (!can_handle_multiple_threads)
			max_cp_per_thread = cp.size();
		ArrayList<Thread> thds = new ArrayList<Thread>();

		for (; cp.size() != 0;) {
			int lastElt = cp.size() > max_cp_per_thread ? max_cp_per_thread : cp.size();
			Set<CurrencyPair> tmp = cp.stream()
				    .limit(lastElt)
				    .collect(Collectors.toSet());
			cp = cp.stream()
				   .skip(lastElt) // the offset
				   .collect(Collectors.toSet());
			logger.debug(exchange.name() + " : Create new Thread : " + tmp.toString());
			Thread thd = new Thread(new ticker_publisher_task<exchanges.IExchange>(ep.getExchange(exchange), tmp,
					context, ticker_pub, refresh_rate));
			thds.add(thd);
			thd.start();
		}
		return thds;
	}
	

	/**
	 * @param _listCmd
	 * @param _ep
	 * @param _cp
	 * @param _thds
	 * @param _ctx
	 * @param _gf
	 * @throws IOException
	 */
	public static void start(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx, GenericFactory _gf) throws IOException {
		
		Exchanges exchange_ = _gf.getExchange();
		
		if (isEnabled(_gf)) {
			if (_listCmd) {
				CurrencyPairs.displayCurrencyPairs(exchange_, _ep.getExchange(exchange_).getCurrencyPairs());
			} else {
				Set<CurrencyPair> cp_ = _cp;
				String bscp = Config.getInstance().get(_gf.getCurrencyPairs());
				if (bscp != null && !bscp.isEmpty()) {
					if (bscp.trim().toLowerCase().equals("all")) {
						cp_ = _ep.getExchange(exchange_).getCurrencyPairs();
					} else {
						cp_ = new HashSet<>();
						for (String pair : bscp.split(",")) {
							cp_.add(new CurrencyPair(pair));
						}
					}
				} else {
					// Merge the currency pairs
					cp_ = _ep.getExchange(exchange_).getCurrencyPairs();
					Iterator<CurrencyPair> pair = _cp.iterator();
					while (pair.hasNext()) {
						CurrencyPair p = pair.next();
						if (!_cp.contains(p)) {
							pair.remove();
						}
					}
				}

				// Set refresh time
				String refresh_timer = Config.getInstance().get(_gf.getRefreshRate());
                if (refresh_timer != null) {
					_gf.setRefreshRate(Long.parseLong(refresh_timer) * 1000);
				}

				// Create a ticker task
				if (isTickerEnabled(_gf)) {
					_thds.addAll(_gf.create_ticker_feeders(_ep, _ctx, cp_));
				}

				// Create an orderbook task
				if (isOrderbookEnabled(_gf)) {
					_thds.addAll(_gf.create_orderbook_feeders(_ep, _ctx, cp_));
				}
			}
		}
	}
}
