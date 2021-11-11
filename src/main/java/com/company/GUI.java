package com.company;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GUI implements ActionListener{

    private static JTabbedPane tabbedPane;
    private static JLabel label;
    private static TableModel model;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::createAndShowGUI);
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("VWAP application");
        frame.setSize(1020, 650);

        GUI gui = new GUI();
        gui.addComponentToPane(frame.getContentPane());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void addComponentToPane(Container contentPane) {

        tabbedPane = new JTabbedPane();

        JPanel tab1 = new JPanel();
        JButton button = new JButton("Open");
        button.addActionListener(this);
        tab1.add(button);
        label = new JLabel("no file selected");
        tab1.add(label);

        tabbedPane.addTab("OPENFILE", tab1);

        contentPane.add(tabbedPane, BorderLayout.CENTER);

    }

    public void showTable (String fileName)
    {
        // parses CSV file to list of Trade objects
        List<Trade> trades = CSVParser.readTradesFromCSV(fileName);

        // initialise tab and table
        JPanel tab2 = new JPanel();

        JTable table = new JTable();
        model = new TableModel(trades);
        TableRowSorter myTableRowSorter = new TableRowSorter(model);
        table.setRowSorter(myTableRowSorter);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setModel(model);

        GroupLayout layout = new GroupLayout(tab2);
        tab2.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JButton inputBtn = new JButton("Input Epic");
        inputBtn.addActionListener(e -> {
            JFrame f= new JFrame();
            JPanel p = new JPanel();
            f.add(p);

            p.setLayout(new GridLayout(0, 2, 2, 2));

            // Initialising input fields
            JTextField epicField = new JTextField(15);
            JTextField isinField = new JTextField(15);
            JTextField tradeRefField = new JTextField(15);
            JTextField tradeTypeField = new JTextField(15);
            JTextField quantityField = new JTextField(15);
            JTextField priceField = new JTextField(15);

            // Initialising labels and adding fields to panel
            p.add(new JLabel("Epic:"));
            p.add(epicField);
            p.add(new JLabel("ISIN:"));
            p.add(isinField);
            p.add(new JLabel("Trade Ref:"));
            p.add(tradeRefField);
            p.add(new JLabel("Trade Type:"));
            p.add(tradeTypeField);
            p.add(new JLabel("Quantity:"));
            p.add(quantityField);
            p.add(new JLabel("Price:"));
            p.add(priceField);

            // invoke dialog box to collect data from user input and add the new entry to the table
            int option = JOptionPane.showConfirmDialog(f, p, "Please fill all the fields", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (option == JOptionPane.OK_OPTION)
            {
                Trade newTrade = new Trade( "\""+epicField.getText()+ "\"",
                        "\""+isinField.getText()+ "\"",
                        "\""+tradeRefField.getText()+ "\"",
                        "\""+tradeTypeField.getText()+ "\"",
                        Integer.parseInt(quantityField.getText()),
                        Double.parseDouble(priceField.getText()));

                model.getTrades().add(newTrade);
            }
        });

        JButton calcBtn = new JButton("Calculate VWAP");
        calcBtn.addActionListener(e -> {
            JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            // restrict file chooser to saving as a .csv or a .json file
            j.setAcceptAllFileFilterUsed(false);
            j.addChoosableFileFilter(new FileNameExtensionFilter("Comma-Separated Values (CSV)", "csv"));
            j.addChoosableFileFilter(new FileNameExtensionFilter("JavaScript Object Notation (JSON)", "json"));
            int r = j.showSaveDialog(null);

            // if the user selects a file
            if (r == JFileChooser.APPROVE_OPTION)
            {
                // save as the according extension file
                if (j.getFileFilter().getDescription().contains("CSV"))
                    saveVWAP(j.getSelectedFile().getAbsolutePath(), ".csv", table);
                else
                    saveVWAP(j.getSelectedFile().getAbsolutePath(), ".json", table);
            }
            else
                label.setText("the user cancelled the operation");
        });

        JButton graphBtn = new JButton("Graph VWAP");
        graphBtn.addActionListener(e -> {
            // go to function that graphs the VWAP
            graphVWAP(table);
        });

        // create a button panel for Input, Graph and Calculate
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(inputBtn);
        buttonPanel.add(calcBtn);
        buttonPanel.add(graphBtn);

        // create search field and button
        JTextField searchField = new JTextField("");
        searchField.setColumns(45);

        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> {
            // add selection(filtering capability to table
            String searchText = searchField.getText();
            myTableRowSorter.setRowFilter(new MyRowFilter(searchText.toUpperCase()));
        });

        // add search field and button to a search panel
        JPanel searchPanel = new JPanel();
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        // set layout for search panel, utility panel and table
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(scrollPane)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(searchPanel)
                                .addComponent(buttonPanel)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(scrollPane)
                                .addComponent(searchPanel))
                .addComponent(buttonPanel)
        );

        // finally, add the table view tab to the tabbed pane
        tabbedPane.add("TABLEVIEW", tab2);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
    }

    public LinkedList[] calculate(JTable table)
    {
        LinkedList<Trade> trades = new LinkedList<Trade>();

        // loop through table adding each entry as a trade in a list of trades
        for (int row = 0; row < table.getRowCount(); row++){
            Trade trade = new Trade();
            for (int col = 0; col < table.getColumnCount(); col++) {
                if (col == 0)
                    trade.setEpic(table.getValueAt(row,col).toString());
                else if (col == 1)
                    trade.setIsin(table.getValueAt(row,col).toString());
                else if (col == 2)
                    trade.setTradeRef(table.getValueAt(row,col).toString());
                else if (col == 3)
                    trade.setTradeType(table.getValueAt(row,col).toString());
                else if (col == 4)
                    trade.setQuantity(Integer.parseInt(table.getValueAt(row,col).toString()));
                else if (col == 5)
                    trade.setPrice(Double.parseDouble(table.getValueAt(row,col).toString()));
            }
            trade.setValue();
            trades.add(trade);
        }

        // group the list of trades into a map of maps by their epic and trade type
        Map<String, Map<String, List<Trade>>> map = trades.stream()
                .collect(
                        Collectors.groupingBy(Trade::getEpic,
                                Collectors.groupingBy(Trade::getTradeType)));

        LinkedList<String> vwapStockList = new LinkedList();
        LinkedList<String> vwapStockTypeList= new LinkedList();

        // loop through the map of maps calculating the VWAP for overall stock and stock/type
        for (Map.Entry<String, Map<String,List<Trade>>> entry : map.entrySet())
        {
            //outer loop: goes through all trades of a unique epic
            double vwapStockValue = 0; int vwapStockVolume = 0;

            for (Map.Entry<String, List<Trade>> innerEntry : entry.getValue().entrySet()) {
                //inner loop: goes through all types of trades for each stock of a unique epic
                double vwapStockTypeValue = 0; int vwapStockTypeVolume = 0;

                for (Trade tr : innerEntry.getValue()) {
                    vwapStockValue+=tr.getValue();
                    vwapStockTypeValue+=tr.getValue();
                    vwapStockVolume+=tr.getQuantity();
                    vwapStockTypeVolume+=tr.getQuantity();
                }

                // calculates the VWAP for each stock of a unique epic and adds it to Stock/Type list
                double vwapStockType = vwapStockTypeValue / vwapStockTypeVolume;
                vwapStockTypeList.add(entry.getKey()+ ","+ innerEntry.getKey()+ ","+  vwapStockType);
            }

            // calculates the VWAP for each unique stock and adds it to Stock list
            double vwapStock = vwapStockValue / vwapStockVolume;
            vwapStockList.add(entry.getKey()+ ","+ vwapStock);
        }

        // return the list of the two ways teh VWAP has been calculated
        return  new LinkedList[]{vwapStockList, vwapStockTypeList};
    }

    public void saveVWAP(String fileName, String ext, JTable table)
    {
        // calculates the VWAP from the table
        LinkedList<String>[] data = calculate(table);
        LinkedList<String> vwapStockList = data[0];
        LinkedList<String> vwapStockTypeList = data[1];

        // saves it in the corresponding file format
        if(ext.equals(".csv"))
        {
            model.writeToCSV(fileName+ "StockList", vwapStockList);
            model.writeToCSV(fileName+ "StockTypeList", vwapStockTypeList);
        }
        if (ext.equals(".json"))
        {
            model.writeToJSON(fileName+ "StockList", vwapStockList);
            model.writeToJSON(fileName+ "StockTypeList", vwapStockTypeList);
        }
    }

    public void graphVWAP (JTable table)
    {
        // calculates the VWAP from the table
        LinkedList<String>[] data = calculate(table);
        LinkedList<String> vwapStockList = data[0];
        LinkedList<String> vwapStockTypeList = data[1];

        // initialising plot, plot dataset and bar rendering object
        CategoryPlot plot = new CategoryPlot();
        DefaultCategoryDataset catDag = new DefaultCategoryDataset();
        BarRenderer baRenderer = new BarRenderer();

        // loop through VWAP Stock/Type data and add to plot dataset
        for (String s : vwapStockTypeList) {
            String[] vSTL = s.split(",");
            catDag.addValue(Double.parseDouble(vSTL[2]), vSTL[1], vSTL[0]);
        }
        // renders bar chart
        plot.setDataset(1, catDag);
        plot.setRenderer(1, baRenderer);


        catDag = new DefaultCategoryDataset();
        LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();

        Color[] paintList = new Color[]{
                Color.RED,
                Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.ORANGE,
                Color.PINK,
                Color.BLACK,
                Color.GRAY,
                Color.MAGENTA};

        // loop through VWAP for unique Stock dat and add to bar chart
        for (int i = 0; i < vwapStockList.size(); i++)
        {
            String[] vSTL = vwapStockList.get(i).split(",");
            ValueMarker vm = new ValueMarker(Double.parseDouble(vSTL[1]), paintList[i], new BasicStroke(2.0f));

            // adds label to line graph
            vm.setLabel(vSTL[0]);
            vm.setLabelAnchor(RectangleAnchor.TOP);
            vm.setLabelTextAnchor(TextAnchor.CENTER);
            vm.setLabelPaint(paintList[i]);
            plot.addRangeMarker(vm);
        }
        // renders line graph
        plot.setDataset(0, catDag);
        plot.setRenderer(0, lineRenderer);


        // Sets Title, X and Y axis labels for grap plot
        plot.setDomainAxis(new CategoryAxis("Epic"));
        plot.setRangeAxis(new NumberAxis("VWAP"));
        JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("VWAP Graph");

        ChartPanel panel = new ChartPanel(chart);
        JScrollPane scrollPane = new JScrollPane(panel);
        tabbedPane.add("GRAPHVIEW",scrollPane);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("Open")) {

            JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            // restrict file chooser to opening as a .csv file
            j.setAcceptAllFileFilterUsed(false);
            j.setDialogTitle("Select a .csv file");
            FileNameExtensionFilter restrict = new FileNameExtensionFilter("Comma-Separated Values (CSV)", "csv");
            j.addChoosableFileFilter(restrict);


            int r = j.showOpenDialog(null);

            // if the user selects a file
            if (r == JFileChooser.APPROVE_OPTION) {
                // set the label to the path of the selected file
                // and go to table display function
                label.setText(j.getSelectedFile().getAbsolutePath());
                showTable(j.getSelectedFile().getAbsolutePath());
            }
            // if the user cancelled the operation
            else
                label.setText("the user cancelled the operation");
        }
    }
}
