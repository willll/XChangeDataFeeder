package tasks;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

import org.influxdb.dto.Point;
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
	public TickerPublisherStreamingTask(final String id, final T exchange, Set<CurrencyPair> currencyPairs,
			final ZContext context, final StreamingExchange streamingExchange) throws IOException {
		super(id, exchange, currencyPairs, context, streamingExchange);
	}

	/**
	 * @param tck
	 * @param cp
	 * @throws JsonProcessingException
	 */
	public void pushData(final Ticker tck, final CurrencyPair cp) throws JsonProcessingException {
		if (isZeroMQEnable()) {
			socket.sendMore(this.id);
			socket.send(mapper.writeValueAsString(tck), 0);
		}
		if (isInfluxDBEnable()) {

			if (tck.getTimestamp() != null) {
				
				long timestamp = tck.getTimestamp().getTime();
				
				influxDB.write(Point.measurement(cp.toString()).time(timestamp, TimeUnit.MILLISECONDS)
						.addField("open", tck.getOpen()).build());

				influxDB.write(Point.measurement(cp.toString()).time(timestamp, TimeUnit.MILLISECONDS)
						.addField("last", tck.getLast()).build());

				influxDB.write(Point.measurement(cp.toString()).time(timestamp, TimeUnit.MILLISECONDS)
						.addField("bid", tck.getBid()).build());

				influxDB.write(Point.measurement(cp.toString()).time(timestamp, TimeUnit.MILLISECONDS)
						.addField("ask", tck.getAsk()).build());

				influxDB.write(Point.measurement(cp.toString()).time(timestamp, TimeUnit.MILLISECONDS)
						.addField("high", tck.getHigh()).build());

				influxDB.write(Point.measurement(cp.toString()).time(timestamp, TimeUnit.MILLISECONDS)
						.addField("low", tck.getLow()).build());

				influxDB.write(Point.measurement(cp.toString()).time(timestamp, TimeUnit.MILLISECONDS)
						.addField("vwap", tck.getVwap()).build());

				influxDB.write(Point.measurement(cp.toString()).time(timestamp, TimeUnit.MILLISECONDS)
						.addField("volume", tck.getVolume()).build());

				influxDB.write(Point.measurement(cp.toString()).time(timestamp, TimeUnit.MILLISECONDS)
						.addField("quoteVolume", tck.getQuoteVolume()).build());
			} else {

				// If timestamp is null, just push to influxDB without any time, by default it
				// will add the current time
				influxDB.write(Point.measurement(cp.toString()).addField("open", tck.getOpen()).build());

				influxDB.write(Point.measurement(cp.toString()).addField("last", tck.getLast()).build());

				influxDB.write(Point.measurement(cp.toString()).addField("bid", tck.getBid()).build());

				influxDB.write(Point.measurement(cp.toString()).addField("ask", tck.getAsk()).build());

				influxDB.write(Point.measurement(cp.toString()).addField("high", tck.getHigh()).build());

				influxDB.write(Point.measurement(cp.toString()).addField("low", tck.getLow()).build());

				influxDB.write(Point.measurement(cp.toString()).addField("vwap", tck.getVwap()).build());

				influxDB.write(Point.measurement(cp.toString()).addField("volume", tck.getVolume()).build());

				influxDB.write(Point.measurement(cp.toString()).addField("quoteVolume", tck.getQuoteVolume()).build());
			}

		}
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
