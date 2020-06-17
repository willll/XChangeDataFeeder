package tasks;

import java.util.Set;
import java.io.IOException;

import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.ZContext;

import info.bitrich.xchangestream.core.StreamingExchange;
import utils.Constants;

/**
 * @author will
 *
 * @param <T>
 */
public class PublisherStreamingTask<T> extends PublisherTask<T> {

	/**
	 * @param id
	 * @param exchange
	 * @param currencyPairs
	 * @param context
	 */
	public PublisherStreamingTask(final String id, final T exchange, Set<CurrencyPair> currencyPairs,
			final ZContext context, final StreamingExchange streamingExchange) throws IOException {
		super(id, exchange, currencyPairs, context, Constants.EXCHANGE_UPDATE_DELAY);
		this.streamingExchange = streamingExchange;
	}

	StreamingExchange streamingExchange = null;
	
	public void SetstreamingExchange(StreamingExchange streamingExchange) {
		this.streamingExchange = streamingExchange;
	}
}
