package tasks;

import java.util.ArrayList;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.zeromq.ZContext;

import com.fasterxml.jackson.core.JsonProcessingException;

import features.ILogger;
import info.bitrich.xchangestream.core.StreamingExchange;

/**
 * @author will
 *
 * @param <T>
 */
public class TickerPublisherStreamingTask<T> extends PublisherStreamingTask<T> implements ITask, ILogger, Runnable {

	/**
	 * @param id
	 * @param exchange
	 * @param currencyPairs
	 * @param context
	 * @param streamingExchange
	 */
	public TickerPublisherStreamingTask(final String id, final T exchange, ArrayList<CurrencyPair> currencyPairs,
			final ZContext context, final StreamingExchange streamingExchange) {
		super(id, exchange, currencyPairs, context, streamingExchange);
	}

	/**
	 * @param tck
	 * @param cp
	 * @throws JsonProcessingException
	 */
	public void pushData(final Ticker tck, final CurrencyPair cp) throws JsonProcessingException {
		socket.sendMore(this.id);
		socket.send(mapper.writeValueAsString(tck), 0);
		logger.info("Sending:" + cp.toString() + " " + mapper.writeValueAsString(tck.toString()));
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
				this.streamingExchange.getStreamingMarketDataService().getTicker(c).subscribe(ticker -> {
					pushData(ticker, c);
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
