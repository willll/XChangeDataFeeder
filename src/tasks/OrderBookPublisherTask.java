package tasks;

import java.io.IOException;
import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.exceptions.CurrencyPairNotValidException;
import org.zeromq.ZContext;

import com.fasterxml.jackson.core.JsonProcessingException;

import exchanges.IExchange;

/**
 * @author will
 *
 * @param <T>
 */
public class OrderBookPublisherTask<T> extends PublisherTask<T> implements Runnable {

	public OrderBookPublisherTask(final String id, final T exchange, Set<CurrencyPair> currencyPairs,
			final ZContext context, final long refreshRate) throws IOException  {
		super(id, exchange, currencyPairs, context, refreshRate);
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

			while (!Thread.currentThread().isInterrupted()) {
				logger.info(this.threadId + " : Start processing orderbook");
				CurrencyPair crashed_cp = null;
				try {
					for (CurrencyPair cp : this.currencyPairs) {
						try {
							pushData(((IExchange) exchange).getOrderBook(cp));
						} catch (IOException e) {
							logger.error(this.threadId + " : IOError : " + this.exchangeName + " : " + cp.toString());
						} catch (CurrencyPairNotValidException e) {
							crashed_cp = cp;
							throw e;
						}
					}
				} catch (CurrencyPairNotValidException e) {
					if (crashed_cp != null) {
						logger.error(this.threadId + " : " + this.exchangeName + " : CurrencyPairNotValidException : "
								+ crashed_cp.toString() + " Removed from list");
						this.currencyPairs.remove(crashed_cp);
					} else {
						logger.error(this.threadId + " : " + this.exchangeName
								+ " : CurrencyPairNotValidException : UNKNOWN CURRENCY PAIR :(");
					}
				}
				logger.info(this.threadId + " : End processing orderbook");

				// Sleep a second, that sound like enough for now !
				Thread.sleep(this.refreshRate);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(this.threadId + " : Interrupted : " + e.toString());
		}
	}
}
