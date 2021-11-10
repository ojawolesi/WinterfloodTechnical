package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    public static List<Trade> readTradesFromCSV(String fileName) {

        List<Trade> trades = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");

                Trade trade = new Trade( row[0],
                        row[1],
                        row[2],
                        row[3],
                        Integer.parseInt(row[4]),
                        Double.parseDouble(row[5]));

                trades.add(trade);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return trades;
    }

}
