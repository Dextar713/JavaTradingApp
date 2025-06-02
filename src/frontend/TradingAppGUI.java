package frontend;

import backend.database.IDatabase;
import backend.models.Asset;
import backend.models.Portfolio;
import backend.models.PortfolioAsset;
import backend.models.User;
import backend.repositories.IAssetRepo;
import backend.repositories.IPortfolioRepo;
import backend.repositories.IUserRepo;
import backend.services.LoginService;
import backend.services.TradingService;
import exceptions.CustomExceptions;

import javax.swing.*;
import java.awt.*;
//import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TradingAppGUI extends JFrame {
    private final IUserRepo userRepo;
    private final IAssetRepo assetRepo;
    private final IDatabase db;
    private final LoginService loginService;
    private final TradingService tradingService;
    private final IPortfolioRepo portfolioRepo;
    private final JTabbedPane tabbedPane;

    public TradingAppGUI(IUserRepo userRepo, IAssetRepo assetRepo,
                         IPortfolioRepo portfolioRepo,
                         IDatabase db, LoginService loginService,
                         TradingService tradingService) {
        this.userRepo = userRepo;
        this.assetRepo = assetRepo;
        this.portfolioRepo = portfolioRepo;
        this.db = db;
        this.loginService = loginService;
        this.tradingService = tradingService;

        setTitle("Trading App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create tabbed pane
        this.tabbedPane = new JTabbedPane();

        // Add tabs
        tabbedPane.addTab("Login", createLoginPanel());
        tabbedPane.addTab("Register", createRegisterPanel());
        tabbedPane.addTab("Trade", new JPanel());
        tabbedPane.addTab("Portfolio", new JPanel());
        tabbedPane.addTab("Market Prices", new JPanel());

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            String selectedTitle = tabbedPane.getTitleAt(selectedIndex);
            switch (selectedTitle) {
                case "Trade" -> tabbedPane.setComponentAt(selectedIndex, createTradePanel());
                case "Portfolio" -> tabbedPane.setComponentAt(selectedIndex, createPortfolioPanel());
                case "Market Prices" -> tabbedPane.setComponentAt(selectedIndex, createMarketPanel());
            }
        });

        add(tabbedPane);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        panel.add(loginButton);

        // Add listener
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            boolean success = true;
            try {
                loginService.login(username, password);
            } catch (CustomExceptions.UserNotExists | CustomExceptions.WrongPassword ex) {
                success = false;
                ex.printStackTrace();
            }
            if (success) {
                JOptionPane.showMessageDialog(this, "Login successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        });

        return panel;
    }


    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Confirm Password:"));
        JPasswordField confirmPasswordField = new JPasswordField();
        panel.add(confirmPasswordField);

        JButton registerButton = new JButton("Register");
        panel.add(registerButton);

        return panel;
    }

    private JPanel createTradePanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("Select Asset:"));
        List<Asset> assets = db.getAssets();
        String[] assetNames = new String[assets.size()];
        for(int i=0; i<assetNames.length; i++) {
            assetNames[i] = assets.get(i).getName();
        }
        JComboBox<String> assetCombo = new JComboBox<>(assetNames);
        //JComboBox<String> assetCombo = new JComboBox<>(new String[] {"Bitcoin", "Ethereum", "Litecoin"});
        panel.add(assetCombo);

        panel.add(new JLabel("Amount:"));
        JTextField amountField = new JTextField();
        panel.add(amountField);

        panel.add(new JLabel("Price per unit:"));
        JTextField priceField = new JTextField();
        panel.add(priceField);

        JButton buyButton = new JButton("Buy");
        JButton sellButton = new JButton("Sell");
        buyButton.addActionListener(e -> handleTradeAction(assetCombo, amountField, priceField, true));
        sellButton.addActionListener(e -> handleTradeAction(assetCombo, amountField, priceField, false));

        panel.add(buyButton);
        panel.add(sellButton);

        return panel;
    }

    private void handleTradeAction(JComboBox<String> assetCombo, JTextField amountField,
                                   JTextField priceField, boolean isBuy) {
        try {
            String assetName = (String) assetCombo.getSelectedItem();
            Asset asset = assetRepo.getByName(assetName);
            double amount = Double.parseDouble(amountField.getText());
            double price = Double.parseDouble(priceField.getText());

            int userId = userRepo.getCurrentUser().getId();
            int assetId = asset.getId();

            if (isBuy) {
                tradingService.Buy(userId, assetId, amount, price);
            } else {
                tradingService.Sell(userId, assetId, amount, price);
            }

            JOptionPane.showMessageDialog(this,
                    (isBuy ? "Buy" : "Sell") + " order executed successfully!");

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Error: " + exception.getMessage());
        }
    }

    private JPanel createPortfolioPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Asset", "Amount", "Price Per Unit", "Total Value"};
        User curUser = userRepo.getCurrentUser();
        if(curUser == null) {
            //throw new CustomExceptions.Unauthorized("You should login");
            JOptionPane.showMessageDialog(this, "You should login");
            return panel;
        }
        Portfolio curPortfolio = curUser.getPortfolio();
        List<PortfolioAsset> userAssets = portfolioRepo.getAssets(curPortfolio.getId());
        double depositLeft = portfolioRepo.getBalance(curPortfolio.getId());
        double totalAssetValue = 0;
        int portfolioSize = userAssets.size();
        Object[][] data = new Object[portfolioSize][];
        for(int i=0; i<portfolioSize; i++) {
            double value = userAssets.get(i).getQuantity() * userAssets.get(i).getAsset().getPricePerUnit();
            data[i] = new Object[]{
                    userAssets.get(i).getAsset().getName(),
                    userAssets.get(i).getQuantity(),
                    userAssets.get(i).getAsset().getPricePerUnit(),
                    value
            };
            totalAssetValue += value;
        }
        double totalBalance = depositLeft + totalAssetValue;
        JPanel summaryPanel = new JPanel(new GridLayout(3, 1));
        summaryPanel.add(new JLabel(
                "Total Portfolio Value (Assets): $" + String.format("%.2f", totalAssetValue)));
        summaryPanel.add(new JLabel("Cash Available (USD): $" + String.format("%.2f", depositLeft)));
        summaryPanel.add(new JLabel("Total Balance: $" + String.format("%.2f", totalBalance)));
        //JTable portfolioTable = new JTable(data, columnNames);
        JTable portfolioTable = new JTable(new javax.swing.table.DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        JScrollPane scrollPane = new JScrollPane(portfolioTable);
        panel.add(summaryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMarketPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Asset", "Current Price"};
        List<Asset> allAssets = db.getAssets();
        allAssets.sort(Collections.reverseOrder());
        int numOfAssets = allAssets.size();
        Object[][] data = new Object[numOfAssets][];
        for(int i=0; i<numOfAssets; i++) {
            data[i] = new Object[]{
                    allAssets.get(i).getName(),
                    allAssets.get(i).getPricePerUnit()
            };
        }

        //JTable marketTable = new JTable(data, columnNames);
        JTable marketTable = new JTable(new javax.swing.table.DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are non-editable
            }
        });
        JScrollPane scrollPane = new JScrollPane(marketTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}

