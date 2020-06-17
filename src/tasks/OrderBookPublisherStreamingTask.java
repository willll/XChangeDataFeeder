package tasks;

import java.util.Set;
import java.io.IOException;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.zeromq.ZContext;

import com.fasterxml.jackson.core.JsonProcessingException;

import info.bitrich.xchangestream.core.StreamingExchange;

/**
 * @author will
 *
 * @param <T>
 */
public class OrderBookPublisherStreamingTask<T> extends PublisherStreamingTask<T> implements Runnable {

	public OrderBookPublisherStreamingTask(final String id, final T exchange, Set<CurrencyPair> currencyPairs,
			final ZContext context, final StreamingExchange streamingExchange) throws IOException {
		super(id, exchange, currencyPairs, context, streamingExchange);
	}

	/**
	 * @param ob
	 * @throws JsonProcessingException
	 */
	public void pushData(final OrderBook ob) throws JsonProcessingException {
		String jsonOutput = mapper.writeValueAsString(ob);
		if (isZeroMQEnable()) {
			socket.sendMore(this.id);
			socket.send(jsonOutput);
		}
		logger.debug(this.threadId + " : Sent : " + this.exchangeName + " : " + jsonOutput);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tasks.ITask#run()
	 */
	@Override
	public void run() {
		try {
			this.threadId = Thread.currentThread().getId();

			currencyPairs.forEach(c -> {
				logger.debug(this.threadId + " : " + this.exchangeName + " : " + c.toString() + " added to list");
				this.streamingExchange.getStreamingMarketDataService().getOrderBook(c).subscribe(orderBook -> {

					pushData(orderBook);
				}, throwable -> {
					logger.error(this.threadId + " : " + this.exchangeName + " : CurrencyPairNotValidException : "
							+ c.toString() + " Removed from list");
					this.currencyPairs.remove(c);
				});
			});

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(this.threadId + " : Interrupted : " + e.toString());
		}
	}
}
