import java.util.ArrayList;
import java.util.Iterator;

import org.knowm.xchange.currency.CurrencyPair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import org.apache.commons.cli.*;

import exchanges.factories.EntryPoint;
import exchanges.factories.EntryPoint.Exchanges;
import exchanges.factories.ExchangesFactory;
import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.ProductSubscription.ProductSubscriptionBuilder;
import info.bitrich.xchangestream.core.StreamingExchange;
import tasks.OrderBookPublisherStreamingTask;
import tasks.TickerPublisherStreamingTask;
import utils.Config;
import utils.Constants;


public class Main {

	private static Logger logger = LoggerFactory.getLogger("Console");

	static void displayCurrencyPairs(Exchanges xch, ArrayList<CurrencyPair> cp) {
		System.out.println (xch + " ;\n");
		for (CurrencyPair currencyPair : cp) {
			System.out.print(currencyPair + ", ");
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		logger.trace("STARTUP");

		Options options = new Options();

        Option list = new Option("l", "list", false, "list currencies");
        list.setRequired(false);
        options.addOption(list);
		
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("XChange app ...", options);
            System.exit(1);
        }
        
        Boolean listCmd = cmd.hasOption("list");
        
		ArrayList<Thread> thds = new ArrayList<Thread>();

		// Entry point to the exchanges : XChange and XChange-stream
		EntryPoint ep = new EntryPoint();

		// Bus used to store share data : JeroMQ
		ZContext ctx = new ZContext();

		// bus configuration :
		String port = Config.getInstance().get(Constants.port);
		Socket clients = ctx.createSocket(ZMQ.XPUB);
		clients.bind("tcp://*:" + port);
		Socket workers = ctx.createSocket(ZMQ.XSUB);
		workers.bind("inproc://workers");

		String scp = Config.getInstance().get(Constants.currency_pairs);
		ArrayList<CurrencyPair> cp = new ArrayList<>();
		if (scp != null) {
			for(String pair : scp.split(",")) {
	            cp.add(new CurrencyPair(pair));
			}
		}
		if(cp.size() == 0)
			cp.add(new CurrencyPair("BTC/USDT"));

		/*
		 * BINANCE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.binance_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BINANCE, ep.getExchange(Exchanges.BINANCE).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Binance_cp = cp;
                String bscp = Config.getInstance().get(Constants.binance_currency_pairs);
                if(bscp != null) {
                        Binance_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Binance_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Binance_cp = ep.getExchange(Exchanges.BINANCE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()) {
                        CurrencyPair p = pair.next();
                        if(!Binance_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                int nbConnection = 1;

                if (1 == nbConnection) {
                    StreamingExchange streamingExchange = ep.getStreamingExchange(Exchanges.BINANCE).getStreamingExchange();
		            ProductSubscriptionBuilder subscriptionBuilder = ProductSubscription.create();

                    TickerPublisherStreamingTask tickerPublisherStreamingTask = null;
		            OrderBookPublisherStreamingTask orderBookPublisherStreamingTask = null;

                    //Create a ticker from Binance
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.binance_ticker_enabled))) {
                    	tickerPublisherStreamingTask = ExchangesFactory.getBinanceFactory().create_runnable_ticker_feeders(
                                ep, ctx, cp, Exchanges.BINANCE, subscriptionBuilder);
                    }

                    //Create an orderbook from Binance
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.binance_orderbook_enabled))) {
                    	orderBookPublisherStreamingTask = ExchangesFactory.getBinanceFactory().create_runnable_orderbook_feeders(
                                ep, ctx, cp, subscriptionBuilder);
                    }

                    ProductSubscription subscription = subscriptionBuilder.build();
		            streamingExchange.connect(subscription).blockingAwait();

                    boolean bRun = false;
		            if (tickerPublisherStreamingTask != null) {
		            	tickerPublisherStreamingTask.SetstreamingExchange(streamingExchange);
		            	tickerPublisherStreamingTask.run();
		            	bRun = true;
		            }

                    // Does not work  :(
		            if (orderBookPublisherStreamingTask != null && !bRun) {
		            	orderBookPublisherStreamingTask.SetstreamingExchange(streamingExchange);
		            	orderBookPublisherStreamingTask.run();
		            }


                } else {
                    //Create a ticker from Binance
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.binance_ticker_enabled))) {
                        thds.addAll(ExchangesFactory.getBinanceFactory().create_ticker_feeders(ep, ctx, cp));
                    }

                    //Create an orderbook from Binance
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.binance_orderbook_enabled))) {
                        thds.addAll(ExchangesFactory.getBinanceFactory().create_orderbook_feeders(ep, ctx, cp));
                    }
                }
			}
        }

		/*
		 * BITFINEX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitfinex_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITFINEX, ep.getExchange(Exchanges.BITFINEX).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Bitfinex_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitfinex_currency_pairs);
                if(bscp != null) {
                        Bitfinex_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitfinex_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitfinex_cp = ep.getExchange(Exchanges.BITFINEX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()) {
                        CurrencyPair p = pair.next();
                        if(!Bitfinex_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                int nbConnection = 1;

                if (1 == nbConnection) {
                    StreamingExchange streamingExchange = ep.getStreamingExchange(Exchanges.BITFINEX).getStreamingExchange();
		            ProductSubscriptionBuilder subscriptionBuilder = ProductSubscription.create();

                    TickerPublisherStreamingTask tickerPublisherStreamingTask = null;
		            OrderBookPublisherStreamingTask orderBookPublisherStreamingTask = null;

                    //Create a ticker from Bitfinex
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitfinex_ticker_enabled))) {
                    	tickerPublisherStreamingTask = ExchangesFactory.getBitfinexFactory().create_runnable_ticker_feeders(
                                ep, ctx, cp, Exchanges.BITFINEX, subscriptionBuilder);
                    }

                    //Create an orderbook from Bitfinex
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitfinex_orderbook_enabled))) {
                    	orderBookPublisherStreamingTask = ExchangesFactory.getBitfinexFactory().create_runnable_orderbook_feeders(
                                ep, ctx, cp, subscriptionBuilder);
                    }

                    ProductSubscription subscription = subscriptionBuilder.build();
		            streamingExchange.connect(subscription).blockingAwait();

                    boolean bRun = false;
		            if (tickerPublisherStreamingTask != null) {
		            	tickerPublisherStreamingTask.SetstreamingExchange(streamingExchange);
		            	tickerPublisherStreamingTask.run();
		            	bRun = true;
		            }

                    // Does not work  :(
		            if (orderBookPublisherStreamingTask != null && !bRun) {
		            	orderBookPublisherStreamingTask.SetstreamingExchange(streamingExchange);
		            	orderBookPublisherStreamingTask.run();
		            }


                } else {
                    //Create a ticker from Bitfinex
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitfinex_ticker_enabled))) {
                        thds.addAll(ExchangesFactory.getBitfinexFactory().create_ticker_feeders(ep, ctx, cp));
                    }

                    //Create an orderbook from Bitfinex
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitfinex_orderbook_enabled))) {
                        thds.addAll(ExchangesFactory.getBitfinexFactory().create_orderbook_feeders(ep, ctx, cp));
                    }
                }
			}
        }

		/*
		 * BITSTAMP
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitstamp_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITSTAMP, ep.getExchange(Exchanges.BITSTAMP).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Bitstamp_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitstamp_currency_pairs);
                if(bscp != null) {
                        Bitstamp_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitstamp_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitstamp_cp = ep.getExchange(Exchanges.BITSTAMP).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()) {
                        CurrencyPair p = pair.next();
                        if(!Bitstamp_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                int nbConnection = 1;

                if (1 == nbConnection) {
                    StreamingExchange streamingExchange = ep.getStreamingExchange(Exchanges.BITSTAMP).getStreamingExchange();
		            ProductSubscriptionBuilder subscriptionBuilder = ProductSubscription.create();

                    TickerPublisherStreamingTask tickerPublisherStreamingTask = null;
		            OrderBookPublisherStreamingTask orderBookPublisherStreamingTask = null;

                    //Create a ticker from Bitstamp
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitstamp_ticker_enabled))) {
                    	tickerPublisherStreamingTask = ExchangesFactory.getBitstampFactory().create_runnable_ticker_feeders(
                                ep, ctx, cp, Exchanges.BITSTAMP, subscriptionBuilder);
                    }

                    //Create an orderbook from Bitstamp
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitstamp_orderbook_enabled))) {
                    	orderBookPublisherStreamingTask = ExchangesFactory.getBitstampFactory().create_runnable_orderbook_feeders(
                                ep, ctx, cp, subscriptionBuilder);
                    }

                    ProductSubscription subscription = subscriptionBuilder.build();
		            streamingExchange.connect(subscription).blockingAwait();

                    boolean bRun = false;
		            if (tickerPublisherStreamingTask != null) {
		            	tickerPublisherStreamingTask.SetstreamingExchange(streamingExchange);
		            	tickerPublisherStreamingTask.run();
		            	bRun = true;
		            }

                    // Does not work  :(
		            if (orderBookPublisherStreamingTask != null && !bRun) {
		            	orderBookPublisherStreamingTask.SetstreamingExchange(streamingExchange);
		            	orderBookPublisherStreamingTask.run();
		            }


                } else {
                    //Create a ticker from Bitstamp
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitstamp_ticker_enabled))) {
                        thds.addAll(ExchangesFactory.getBitstampFactory().create_ticker_feeders(ep, ctx, cp));
                    }

                    //Create an orderbook from Bitstamp
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitstamp_orderbook_enabled))) {
                        thds.addAll(ExchangesFactory.getBitstampFactory().create_orderbook_feeders(ep, ctx, cp));
                    }
                }
			}
        }

		/*
		 * BITTREX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bittrex_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITTREX, ep.getExchange(Exchanges.BITTREX).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Bittrex_cp = cp;
                String bscp = Config.getInstance().get(Constants.bittrex_currency_pairs);
                if(bscp != null) {
                        Bittrex_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")){
                            Bittrex_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bittrex_cp = ep.getExchange(Exchanges.BITTREX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bittrex_cp.contains(p)){
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bittrex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bittrex_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBittrexFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create a orderbook from Bittrex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bittrex_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBittrexFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * CCEX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.ccex_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.CCEX, ep.getExchange(Exchanges.CCEX).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Ccex_cp = cp;
                String bscp = Config.getInstance().get(Constants.ccex_currency_pairs);
                if(bscp != null) {
                        Ccex_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")){
                            Ccex_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Ccex_cp = ep.getExchange(Exchanges.CCEX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Ccex_cp.contains(p)){
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Ccex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.ccex_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCcexFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create a orderbook from Ccex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.ccex_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCcexFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINMARKETCAP
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmarketcap_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINMARKETCAP, ep.getExchange(Exchanges.COINMARKETCAP).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Coinmarketcap_cp = cp;
                String bscp = Config.getInstance().get(Constants.coinmarketcap_currency_pairs);
                if(bscp != null) {
                        Coinmarketcap_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")){
                            Coinmarketcap_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coinmarketcap_cp = ep.getExchange(Exchanges.COINMARKETCAP).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coinmarketcap_cp.contains(p)){
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Coinmarketcap
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmarketcap_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinmarketcapFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create a orderbook from Coinmarketcap
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmarketcap_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinmarketcapFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * KRAKEN
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.kraken_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.KRAKEN, ep.getExchange(Exchanges.KRAKEN).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Kraken_cp = cp;
                String bscp = Config.getInstance().get(Constants.kraken_currency_pairs);
                if(bscp != null) {
                        Kraken_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")){
                            Kraken_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Kraken_cp = ep.getExchange(Exchanges.KRAKEN).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Kraken_cp.contains(p)){
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Kraken
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.kraken_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getKrakenFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create a orderbook from Kraken
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.kraken_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getKrakenFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * KUCOIN
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.kucoin_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.KUCOIN, ep.getExchange(Exchanges.KUCOIN).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Kucoin_cp = cp;
                String bscp = Config.getInstance().get(Constants.kucoin_currency_pairs);
                if(bscp != null) {
                        Kucoin_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")){
                            Kucoin_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Kucoin_cp = ep.getExchange(Exchanges.KUCOIN).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Kucoin_cp.contains(p)){
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Kucoin
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.kucoin_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getKucoinFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create a orderbook from Kucoin
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.kucoin_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getKucoinFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * OKCOIN
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.okcoin_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.OKCOIN, ep.getExchange(Exchanges.OKCOIN).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Okcoin_cp = cp;
                String bscp = Config.getInstance().get(Constants.okcoin_currency_pairs);
                if(bscp != null) {
                        Okcoin_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Okcoin_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Okcoin_cp = ep.getExchange(Exchanges.OKCOIN).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()) {
                        CurrencyPair p = pair.next();
                        if(!Okcoin_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                int nbConnection = 1;

                if (1 == nbConnection) {
                    StreamingExchange streamingExchange = ep.getStreamingExchange(Exchanges.OKCOIN).getStreamingExchange();
		            ProductSubscriptionBuilder subscriptionBuilder = ProductSubscription.create();

                    TickerPublisherStreamingTask tickerPublisherStreamingTask = null;
		            OrderBookPublisherStreamingTask orderBookPublisherStreamingTask = null;

                    //Create a ticker from Okcoin
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.okcoin_ticker_enabled))) {
                    	tickerPublisherStreamingTask = ExchangesFactory.getOkcoinFactory().create_runnable_ticker_feeders(
                                ep, ctx, cp, Exchanges.OKCOIN, subscriptionBuilder);
                    }

                    //Create an orderbook from Okcoin
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.okcoin_orderbook_enabled))) {
                    	orderBookPublisherStreamingTask = ExchangesFactory.getOkcoinFactory().create_runnable_orderbook_feeders(
                                ep, ctx, cp, subscriptionBuilder);
                    }

                    ProductSubscription subscription = subscriptionBuilder.build();
		            streamingExchange.connect(subscription).blockingAwait();

                    boolean bRun = false;
		            if (tickerPublisherStreamingTask != null) {
		            	tickerPublisherStreamingTask.SetstreamingExchange(streamingExchange);
		            	tickerPublisherStreamingTask.run();
		            	bRun = true;
		            }

                    // Does not work  :(
		            if (orderBookPublisherStreamingTask != null && !bRun) {
		            	orderBookPublisherStreamingTask.SetstreamingExchange(streamingExchange);
		            	orderBookPublisherStreamingTask.run();
		            }


                } else {
                    //Create a ticker from Okcoin
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.okcoin_ticker_enabled))) {
                        thds.addAll(ExchangesFactory.getOkcoinFactory().create_ticker_feeders(ep, ctx, cp));
                    }

                    //Create an orderbook from Okcoin
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.okcoin_orderbook_enabled))) {
                        thds.addAll(ExchangesFactory.getOkcoinFactory().create_orderbook_feeders(ep, ctx, cp));
                    }
                }
			}
        }

		/*
		 * POLONIEX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.poloniex_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.POLONIEX, ep.getExchange(Exchanges.POLONIEX).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Poloniex_cp = cp;
                String bscp = Config.getInstance().get(Constants.poloniex_currency_pairs);
                if(bscp != null) {
                        Poloniex_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Poloniex_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Poloniex_cp = ep.getExchange(Exchanges.POLONIEX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()) {
                        CurrencyPair p = pair.next();
                        if(!Poloniex_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                int nbConnection = 1;

                if (1 == nbConnection) {
                    StreamingExchange streamingExchange = ep.getStreamingExchange(Exchanges.POLONIEX).getStreamingExchange();
		            ProductSubscriptionBuilder subscriptionBuilder = ProductSubscription.create();

                    TickerPublisherStreamingTask tickerPublisherStreamingTask = null;
		            OrderBookPublisherStreamingTask orderBookPublisherStreamingTask = null;

                    //Create a ticker from Poloniex
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.poloniex_ticker_enabled))) {
                    	tickerPublisherStreamingTask = ExchangesFactory.getPoloniexFactory().create_runnable_ticker_feeders(
                                ep, ctx, cp, Exchanges.POLONIEX, subscriptionBuilder);
                    }

                    //Create an orderbook from Poloniex
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.poloniex_orderbook_enabled))) {
                    	orderBookPublisherStreamingTask = ExchangesFactory.getPoloniexFactory().create_runnable_orderbook_feeders(
                                ep, ctx, cp, subscriptionBuilder);
                    }

                    ProductSubscription subscription = subscriptionBuilder.build();
		            streamingExchange.connect(subscription).blockingAwait();

                    boolean bRun = false;
		            if (tickerPublisherStreamingTask != null) {
		            	tickerPublisherStreamingTask.SetstreamingExchange(streamingExchange);
		            	tickerPublisherStreamingTask.run();
		            	bRun = true;
		            }

                    // Does not work  :(
		            if (orderBookPublisherStreamingTask != null && !bRun) {
		            	orderBookPublisherStreamingTask.SetstreamingExchange(streamingExchange);
		            	orderBookPublisherStreamingTask.run();
		            }


                } else {
                    //Create a ticker from Poloniex
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.poloniex_ticker_enabled))) {
                        thds.addAll(ExchangesFactory.getPoloniexFactory().create_ticker_feeders(ep, ctx, cp));
                    }

                    //Create an orderbook from Poloniex
                    if (Boolean.parseBoolean(Config.getInstance().get(Constants.poloniex_orderbook_enabled))) {
                        thds.addAll(ExchangesFactory.getPoloniexFactory().create_orderbook_feeders(ep, ctx, cp));
                    }
                }
			}
        }

		// Connect work threads to client threads via a queue
		ZMQ.proxy(clients, workers, null);

		// Infinite loop
		for (Thread thd : thds) {
			thd.join();
		}

		workers.close();
		clients.close();
		ctx.destroy();

		logger.trace("EXIT");
	}
}
