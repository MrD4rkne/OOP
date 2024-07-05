# Simulation of Stock Market
This project was developed as an assigment during OOP course as a part of the Computer Science Bachelor's Studies at the University of Warsaw. <br>
In this project the main goal is simulating stock market with various types of investors and orders.

## Usage
- download whole folder
- compile with Java compilation tools (or with IDE like Intelij).
- run program with parameters: `path_to_file` `number_of_rounds`

### Input
Template: [example](./po.txt)
```
<investors: each R lub S>
<stock companies <name>: <starting price>>
<investor's wallet: <funds> <stocks <name>: <count>>
```

### How it works
- Each round investors (in random order) are given opportunity to make max one order.
  - They must have funds or stock to fulfil it at the time of making order.
  - Then after asking all investors orders are added to each Stock company sheet and algorithm try to complete them in order (limit - decreasing for bought, increasing for sell, then order of being issued).
 
### Investors
- `R`: random investor. They make random orders.
- `S`: SMA investor. They make orders based on SMA signal.
  - *(see: [SMA](https://www.investopedia.com/terms/s/sma.asp), I use short SMA: 5 rounds, long SMA: 10 rounds)*
### Orders
- `UN`: Unlimited. Without expiration data.
- `FK`: Fill or Kill. It must be completed fully in the round of being issued.
- `IM`: Immediate. It msut be completed at least partially in the round of being issued.
- `TR`: Till round. It is valid until (and during) specific round.

### Output
- Program prints wallets of the investors after whole simulation.
- You can see detailed logging if you change `shouldLog` in [Main](./src/main/java/pl/edu/mimuw/ms459531/main/Main.java) to true.
  - **WARNING: it may lead to simulation being really slow**

## Original task:
[Task in Polish](./task.pdf)


