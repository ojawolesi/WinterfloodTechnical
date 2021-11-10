package com.company;

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.util.List;

public class TableModel extends AbstractTableModel {

    List<Trade> trades;
    String[] headerList = new String[]{"Epic", "ISIN", "Trade Ref", "Trade Type", "Quantity", "Price"};

    public TableModel(List list) {
        trades = list;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public int getRowCount() {
        return trades.size();
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public void writeToCSV (String fileName, List<String> VWAP)
    {
        File file;
        try {
            String path = fileName+ ".csv";
            file = new File(path);
            FileWriter fw = new FileWriter(file.getPath(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            boolean header = false;
            int num = VWAP.get(0).split(",").length;
            for (String str: VWAP)
            {

                if (num == 3 && !header)
                {
                    header = true;
                    bw.write("\"epic\",\"trade type\",\"VWAP\"\n");
                }
                else if (num == 2 && !header)
                {
                    header = true;
                    bw.write("\"epic\",\"VWAP\"\n");
                }
                bw.write(str+ "\n");
            }

            bw.close();
            fw.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void writeToJSON (String fileName, List<String> VWAP)
    {
        File file;
        try {
            String path = fileName+ ".json";
            file = new File(path);
            FileWriter fw = new FileWriter(file.getPath(), true);
            BufferedWriter bw = new BufferedWriter(fw);

            for (String str: VWAP)
            {
                String [] elem = str.split(",");

                if (elem.length == 3)
                {
                    bw.write("{\"epic=\": " + elem[0] +
                            ", \"trade_type\": " + elem[1] +
                            ", \"vwap\": " + elem[2] +
                            '}');
                }
                if (elem.length == 2)
                {
                    bw.write("{\"epic=\": " + elem[0] +
                            ", \"vwap\": " + elem[1] +
                            '}');
                }
            }

            bw.close();
            fw.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        Trade trade = trades.get(row);

        switch (column) {

            case 0:
                return trades.get(row).getEpic();
            case 1:
                return trades.get(row).getIsin();
            case 2:
                return trades.get(row).getTradeRef();
            case 3:
                return trades.get(row).getTradeType();
            case 4:
                return trades.get(row).getQuantity();
            case 5:
                return trades.get(row).getPrice();

            default:
                return "";
        }

    }

    //This method will be used to display the name of columns
    public String getColumnName(int col)
    {
        return headerList[col];
    }
}