package exchanges.factories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.ZContext;

import exchanges.GenericStreamingExchange;
import exchanges.IExchange;
import exchanges.factories.EntryPoint.Exchanges;
import features.ILogger;
import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.ProductSubscription.ProductSubscriptionBuilder;
import info.bitrich.xchangestream.core.StreamingExchange;
import tasks.OrderBookPublisherStreamingTask;
import tasks.OrderBookPublisherTask;
import tasks.TickerPublisherStreamingTask;
import tasks.TickerPublisherTask;
import utils.Config;
import utils.CurrencyPairs;

/**
 * @author will
 *
 */
public abstract class GenericStreamingFactory implements IFactory, ILogger {

	protected Exchanges exchange;
	protected String ticker_pub;
	protected String orderbook_pub;

	/**
	 * @param _gf
	 * @return
	 * @throws IOException
	 */
	public static Boolean isEnabled(GenericStreamingFactory _gf) throws IOException {
		return Config.isEnabled(_gf.getEnabled());
	}
	
	/**
	 * @param _gf
	 * @return
	 * @throws IOException
	 */
	public static Boolean isTickerEnabled(GenericStreamingFactory _gf) throws IOException {
		return Config.isEnabled(_gf.getTickerEnabled());
	}
	
	/**
	 * @param _gf
	 * @return
	 * @throws IOException
	 */
	public static Boolean isOrderbookEnabled(GenericStreamingFactory _gf) throws IOException {
		return Config.isEnabled(_gf.getOrderbookEnabled());
	}
	
	/**
	 * @return
	 */
	public Exchanges getExchange() {
		return this.exchange;
	}
	
	@SuppressWarnings("unused")
	protected static class ticker_publisher_task<T> extends TickerPublisherTask<T> implements Runnable {
		/**
		 * @param exchange
		 * @param currencyPair
		 * @param context
		 * @param pub
		 */
		public ticker_publisher_task(T exchange, Set<CurrencyPair> currencyPair, ZContext context,
				final String pub, final long refresh_rate) throws IOException {
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
				final String pub, final long refresh_rate) throws IOException {
			super(pub, exchange, currencyPair, context, refresh_rate);
		}
	}

	@SuppressWarnings("unused")
	public void create_ticker_stream_feeders(EntryPoint ep, ZContext context, Set<CurrencyPair> cp)
			throws IOException {
		if (!(ep.getExchange(exchange) instanceof GenericStreamingExchange)) {
			throw new IllegalArgumentException();
		}
		if (!((GenericStreamingExchange) ep.getExchange(exchange).getExchange()).isTickerSupported()) {
			throw new IllegalArgumentException();
		}

		create_ticker_feeders(ep, context, cp, this.exchange, this.ticker_pub);
	}

	@Override
	@SuppressWarnings("unused")
	public ArrayList<Thread> create_orderbook_feeders(EntryPoint ep, ZContext context, Set<CurrencyPair> cp)
			throws IOException {

		StreamingExchange streamingExchange = ep.getStreamingExchange(exchange).getStreamingExchange();
		ProductSubscriptionBuilder subscriptionBuilder = ProductSubscription.create();

		cp.forEach(c -> {
			subscriptionBuilder.addTicker(c);
		});
		ProductSubscription subscription = subscriptionBuilder.build();
		streamingExchange.connect(subscription).blockingAwait();

		ArrayList<Thread> thds = new ArrayList<Thread>();

		logger.debug(exchange.name() + " : Create new Thread : " + cp.toString());
		Thread thd = new Thread(new OrderBookPublisherStreamingTask<exchanges.IExchange>(exchange.toString(),
				ep.getExchange(exchange), cp, context, streamingExchange));
		thds.add(thd);
		thd.start();

		return thds;
	}

	public OrderBookPublisherStreamingTask<IExchange> create_runnable_orderbook_feeders(EntryPoint ep, ZContext context,
			Set<CurrencyPair> cp, ProductSubscriptionBuilder subscriptionBuilder)
			throws IOException {

		cp.forEach(c -> {
			subscriptionBuilder.addTicker(c);
		});
	
		logger.debug(exchange.name() + " : Create new Runnable : " + cp.toString());
		return new OrderBookPublisherStreamingTask<exchanges.IExchange>(this.ticker_pub,
				ep.getExchange(exchange), cp, context, null);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * exchanges.factories.GenericFactory#create_ticker_feeders(exchanges.factories.
	 * EntryPoint, org.zeromq.ZContext, java.util.ArrayList)
	 */
	@Override
	@SuppressWarnings("unused")
	public ArrayList<Thread> create_ticker_feeders(EntryPoint ep, ZContext context, Set<CurrencyPair> cp)
			throws IOException {
		try {
			return create_ticker_feeders(ep, context, cp, this.exchange, this.ticker_pub);
		} catch (NullPointerException e) {
			logger.equals("create_ticker_feeders failed ! :(((");
			return new ArrayList<Thread>();
		}
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
	protected static ArrayList<Thread> create_ticker_feeders(EntryPoint ep, ZContext context,
			Set<CurrencyPair> cp, Exchanges exchange, String ticker_pub)
			throws IOException, NullPointerException {

		StreamingExchange streamingExchange = ep.getStreamingExchange(exchange).getStreamingExchange();
		ProductSubscriptionBuilder subscriptionBuilder = ProductSubscription.create();

		cp.forEach(c -> {
			subscriptionBuilder.addTicker(c);
		});
		ProductSubscription subscription = subscriptionBuilder.build();
		streamingExchange.connect(subscription).blockingAwait();

		ArrayList<Thread> thds = new ArrayList<Thread>();

		logger.debug(exchange.name() + " : Create new Thread : " + cp.toString());
		Thread thd = new Thread(new TickerPublisherStreamingTask<exchanges.IExchange>(ticker_pub,
				ep.getExchange(exchange), cp, context, streamingExchange));
		thds.add(thd);
		thd.start();

		return thds;
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
	public TickerPublisherStreamingTask<IExchange> create_runnable_ticker_feeders(EntryPoint ep, ZContext context,
			Set<CurrencyPair> cp, Exchanges exchange, ProductSubscriptionBuilder subscriptionBuilder)
			throws IOException, NullPointerException {

		cp.forEach(c -> {
			subscriptionBuilder.addTicker(c);
		});

		logger.debug(exchange.name() + " : Create new Runnable: " + cp.toString());
		return new TickerPublisherStreamingTask<exchanges.IExchange>(this.ticker_pub,
				ep.getExchange(exchange), cp, context, null);

	}
	
	/**
	 * @param _listCmd
	 * @param _ep
	 * @param _cp
	 * @param _thds
	 * @param _ctx
	 * @param gf_
	 * @throws IOException
	 */
	public static void start(Boolean _listCmd, EntryPoint _ep, Set<CurrencyPair> _cp, ArrayList<Thread> _thds,
	        ZContext _ctx, GenericStreamingFactory _gf) throws IOException {
		
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
						if (!cp_.contains(p)) {
							pair.remove();
						}
					}
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
