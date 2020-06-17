package tasks;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.exceptions.CurrencyPairNotValidException;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.marketdata.params.CurrencyPairsParam;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import com.fasterxml.jackson.core.JsonProcessingException;

import exchanges.IExchange;

/**
 * @author will
 *
 * @param <T>
 */
public class TickerPublisherTask<T> extends PublisherTask<T> implements Runnable {

	public TickerPublisherTask(final String id, final T exchange, Set<CurrencyPair> currencyPairs,
			final ZContext context, final long refreshRate) throws IOException {
		super(id, exchange, currencyPairs, context, refreshRate);
	}

	/**
	 * @param tck
	 * @param cp
	 * @throws JsonProcessingException
	 */
	public void pushData(final Ticker tck, final CurrencyPair cp) throws JsonProcessingException {
		if (isZeroMQEnable()) {
			socket.sendMore(this.id);
			socket.send(mapper.writeValueAsString(tck), ZMQ.DONTWAIT);
		}
		
		if (isInfluxDBEnable()) {
			long timestamp = tck.getTimestamp().getTime();

			if (timestamp != 0) {

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
		logger.info("Sending:" + mapper.writeValueAsString(tck));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tasks.ITask#run()
	 */
	@SuppressWarnings("unused")
	@Override
	public void run() {
		try {
			this.threadId = Thread.currentThread().getId();
			CurrencyPairsParam pairsParam = () -> {
				Set<CurrencyPair> pairs = new HashSet<CurrencyPair>(this.currencyPairs);
				return pairs;
			};
			while (!Thread.currentThread().isInterrupted() && !exit) {
				logger.info(this.threadId + " : Start processing tickers (" + ((IExchange) exchange).getName() + ")");
				CurrencyPair crashed_cp = null;
				try {
					if (this.currencyPairs.size() != 0) {
						// try {
						for (CurrencyPair cp : this.currencyPairs) {
							try {
								pushData(((IExchange) exchange).getTicker(cp), cp);
							} catch (IOException e) {
								logger.error(
										this.threadId + " : IOError : " + this.exchangeName + " : " + cp.toString());
							} catch (CurrencyPairNotValidException e) {
								crashed_cp = cp;
								throw e;
							} catch (ExchangeException e) {
								logger.error(
										this.threadId + " : ExchangeException : " + this.exchangeName + " : " + cp.toString());
								// TBD : Cmc raised this : You've exceeded your API Key's HTTP request rate limit. Rate limits reset every minute. - ErrorCode: 1008
								// Just exit the loop !
								break;
							}
						}

						/*
						 * List<Ticker> tickerList = ((IExchange) exchange).getTickers(pairsParam);
						 * 
						 * for (Ticker ticker : tickerList) { try { pushData(ticker); } catch
						 * (IOException e) { // logger.error(this.threadId + " : IOError : " +
						 * this.exchangeName + " : " + // cp.toString()); } catch
						 * (CurrencyPairNotValidException e) { // crashed_cp = cp; throw e; } catch
						 * (NotAvailableFromExchangeException e) { // Ignore ! // crashed_cp = cp; //
						 * throw e; } catch (ExchangeException e) { // Ignore ! crashed_cp =
						 * ticker.getCurrencyPair(); logger.error(this.threadId +
						 * " : ExchangeException : " + this.exchangeName + " : " + crashed_cp.toString()
						 * + " : " + e);
						 * 
						 * throw new NotAvailableFromExchangeException(); } }
						 */
					} else {
						// Seppoku !
						logger.info(this.threadId + " : killing itself");
						stop();
					}
				} catch (CurrencyPairNotValidException e) {
					if (crashed_cp != null) {
						// logger.error(this.threadId + " : " + this.exchangeName + " :
						// CurrencyPairNotValidException : "
						// + crashed_cp.toString() + " Removed from list");
						// this.currencyPairs.remove(crashed_cp);
					} else {
						logger.error(this.threadId + " : " + this.exchangeName
								+ " : CurrencyPairNotValidException : UNKNOWN CURRENCY PAIR :(");
					}
				} catch (NotAvailableFromExchangeException e) {
					if (crashed_cp != null) {
						logger.error(
								this.threadId + " : " + this.exchangeName + " : NotAvailableFromExchangeException : "
										+ crashed_cp.toString() + " Removed from list");
						this.currencyPairs.remove(crashed_cp);
					} else {
						logger.error(this.threadId + " : " + this.exchangeName
								+ " : NotAvailableFromExchangeException : UNKNOWN CURRENCY PAIR :(");
					}
				} catch (UndeclaredThrowableException e) {

				} catch (NotYetImplementedForExchangeException e) {
					logger.info(this.threadId + " : killing itself, not implemented");
					stop();
				} catch (NullPointerException e) {
					logger.error(this.threadId + " : " + this.exchangeName
							+ " : NullPointerException while retrieving tickers, stopping :(");
					stop();
				}

				// logger.info(this.threadId + " : End processing tickers");

				Thread.sleep(this.refreshRate);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error(this.threadId + " : Interrupted : " + e.toString());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
