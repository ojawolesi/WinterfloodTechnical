# Winterflood Take-Home Assessment (VWAP)

## Objective 1
Read a .csv file and calculate the Value Weighted Average Price (VWAP) for:
* Each unique stock
* Each unique stock / trade type combination

The given .csv file [market_trades](https://github.com/ojawolesi/WinterfloodTechnical/blob/main/market_trades.csv) has the following format:  

| epic | isin | trade ref | trade type | quantity | price |
|------|------|-----------|------------|----------|-------|
|CXRB|GK8838181522|GK8838181522-O-013|Ordinary|2|0.7205|
|CXRB|GK8838181522|GK8838181522-O-02|Ordinary|491|0.576|
|...|...|...|...|...|...|


## Objective 2
Allow users to save the VWAP calculations in 2 separate files. The VWAP calculations were saved in the following format for the unique stock and the unique stock / trade type combination.

| epic | VWAP |  
|------|-------| 
|EXMR|0.18806396976625700| 
|CXRB|0.6769488220931830|  
|...|...|...|...|...|...|  

| epic | trade type | VWAP |
|------|------|-----------|
|EXMR|Ordinary|0.1901579094882740|
|EXMR|Large| 0.171|
|...|...|...|

## Objective 3
Allow users the chance to save the VWAP calculations in a file format other than the first (.csv). I chose a .json file, as this would be suitable for web APIs and automation.

| [.csv or .json saving option]() |
|------|
![]()      |

[Zurich Tonhalle (1957)]() | 
:-------------------------:|:
![Zurich Tonhalle poster](https://github.com/ojawolesi/ManifestoTechnicalChallenge/blob/master/task1/Extra-konzert%20on%20Flickr%20-%20Photo%20Sharing!.jpg)  |


Objective 4
Provide a simple interface to be able to
Allow selection(filtering) and input of an Epic
Display the overall VWAP and the VWAP per trade type for that stock in an innovative way


Objective 5
What other data/calculations might be of interest apart from VWAP?


Objective 6
comment on any aspects of the data that strike you
