package exchanges.factories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.ZContext;

import exchanges.factories.EntryPoint.Exchanges;
import features.ILogger;
import tasks.OrderBookPublisherTask;
import tasks.TickerPublisherTask;
import utils.Constants;

public class GenericFactory implements IFactory, ILogger {

	protected Exchanges exchange;
	protected String ticker_pub;
	protected String orderbook_pub;
	protected Boolean can_handle_multiple_threads = true;
	protected long refresh_rate = Constants.EXCHANGE_UPDATE_DELAY;

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
}
