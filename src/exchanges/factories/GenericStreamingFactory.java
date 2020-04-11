package exchanges.factories;

import java.io.IOException;
import java.util.ArrayList;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.ZContext;

import exchanges.GenericStreamingExchange;
import exchanges.factories.EntryPoint.Exchanges;
import features.ILogger;
import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.ProductSubscription.ProductSubscriptionBuilder;
import tasks.OrderBookPublisherStreamingTask;
import tasks.OrderBookPublisherTask;
import tasks.TickerPublisherTask;
import tasks.TickerPublisherStreamingTask;

/**
 * @author will
 *
 */
public class GenericStreamingFactory implements IFactory, ILogger {

	protected Exchanges exchange;
	protected String ticker_pub;
	protected String orderbook_pub;

	@SuppressWarnings("unused")
	protected static class ticker_publisher_task<T> extends TickerPublisherTask<T> implements Runnable {
		/**
		 * @param exchange
		 * @param currencyPair
		 * @param context
		 * @param pub
		 */
		public ticker_publisher_task(T exchange, ArrayList<CurrencyPair> currencyPair, ZContext context,
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
		public Orderbook_publisher_task(T exchange, ArrayList<CurrencyPair> currencyPair, ZContext context,
				final String pub, final long refresh_rate) {
			super(pub, exchange, currencyPair, context, refresh_rate);
		}
	}

	@SuppressWarnings("unused")
	public void create_ticker_stream_feeders(EntryPoint ep, ZContext context, ArrayList<CurrencyPair> cp)
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
	public ArrayList<Thread> create_orderbook_feeders(EntryPoint ep, ZContext context, ArrayList<CurrencyPair> cp)
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

	public OrderBookPublisherStreamingTask create_runnable_orderbook_feeders(EntryPoint ep, ZContext context,
			ArrayList<CurrencyPair> cp, ProductSubscriptionBuilder subscriptionBuilder)
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
	public ArrayList<Thread> create_ticker_feeders(EntryPoint ep, ZContext context, ArrayList<CurrencyPair> cp)
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
			ArrayList<CurrencyPair> cp, Exchanges exchange, String ticker_pub)
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
	public TickerPublisherStreamingTask create_runnable_ticker_feeders(EntryPoint ep, ZContext context,
			ArrayList<CurrencyPair> cp, Exchanges exchange, ProductSubscriptionBuilder subscriptionBuilder)
			throws IOException, NullPointerException {

		cp.forEach(c -> {
			subscriptionBuilder.addTicker(c);
		});

		logger.debug(exchange.name() + " : Create new Runnable: " + cp.toString());
		return new TickerPublisherStreamingTask<exchanges.IExchange>(this.ticker_pub,
				ep.getExchange(exchange), cp, context, null);

	}
}
