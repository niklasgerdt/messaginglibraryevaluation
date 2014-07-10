package route20.hft

case class Quote(stockId: String, quoteType: String, value: Double)

case class Trade(stockId: String, tradeType: String, value: Double)

case class Tweet(stockId: String, user: String, message: String)

case class News(stockId: String, agency: String, message: String)

