import com.mysql.cj.x.protobuf.MysqlxCursor;

import java.sql.*;
import java.util.*;
public class Bank_Account {
    public static void main(String arg[]) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MyBank", "root", "welcome@2021");
            String Existing = "";
            String Ac_type = "";
            Scanner scanObj = new Scanner(System.in);
            System.out.println("Hello !!!");
            System.out.println("Welcome to IDBC Bank !!!");
            System.out.println("How Can I Help You!!!");
            System.out.println("Are You Existing Customer ?(Yes/No)");
            Existing = scanObj.next();
            if (Existing.equalsIgnoreCase("No")) {
                System.out.println("You Need to Open A New Account \n");
                System.out.println("Please Choose Account Type \n1. Saving Account \n2. Pay Account");
                int ch1=scanObj.nextInt();
                switch (ch1)
                 {
                     case 1:
                    Saving_Account account = new Saving_Account() {
                        @Override
                        public void openAccount() throws SQLException {
                            super.openAccount();
                        }
                    };
                    account.openAccount();
                    break;
                     case 2:
                     Pay_Account account1 = new Pay_Account() {
                        @Override
                        public void openAccount() throws SQLException {
                            super.openAccount();
                        }
                    };
                    account1.openAccount();
                    break;
                     default:
                    System.out.println("Invalid Choice, Please Try Again...!!!");
                }
            } else if (Existing.equalsIgnoreCase("Yes")) {
                System.out.println("Please Choose Account Type \n1. Saving Account \n2. Pay Account");
                int ch=scanObj.nextInt();
                switch (ch) {
                    case 1:
                        System.out.println("Enter Account Number");
                        long AcNo = scanObj.nextLong();
                        int choice = 0;
                        do {
                            long y=0;
                            String query00="select AcNo from SavingAc where AcNo=?";
                            PreparedStatement ps=connection.prepareStatement(query00);
                            ps.setLong(1,AcNo);
                            ResultSet resultSet00=ps.executeQuery();
                            while (resultSet00.next()){
                                y=resultSet00.getLong("AcNo");
                            }
                            if (y==AcNo) {
                                System.out.println("\n ***IDBC ONLINE BANKING***");
                                System.out.println("1. Display Account Details \n2. Deposit Amount \n3. Withdraw Amount\n4. Money Transfer \n5. Account Statement \6. Exit ");
                                System.out.println("Enter your choice: ");
                                choice = scanObj.nextInt();
                                switch (choice) {
                                    case 1:
                                        double balance = 0;
                                        try {
                                            String query0 = "select Balance from Saving_Statement where SavingAcNo=?";
                                            PreparedStatement preparedStatement1 = connection.prepareStatement(query0);
                                            preparedStatement1.setLong(1, AcNo);
                                            ResultSet resultSet1 = preparedStatement1.executeQuery();
                                            while (resultSet1.next()) {
                                                balance = resultSet1.getDouble("Balance");
                                            }
                                            String query1 = "update SavingAc set Balance=" + balance + "where AcNo=?";
                                            PreparedStatement preparedStatement2 = connection.prepareStatement(query1);
                                            preparedStatement2.setLong(1, AcNo);
                                            int row = preparedStatement2.executeUpdate();
                                        } catch (SQLException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                        try {
                                            String query = "select * from SavingAc where AcNo=" + AcNo + ";";
                                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                                            ResultSet resultSet = preparedStatement.executeQuery();
                                            System.out.println("\n");
                                            System.out.printf("%16s %18s %18s %18s %18s ", "Customer Name", "AcNo", "Interest", "Balance", "CustomerId");
                                            while (resultSet.next()) {
                                                System.out.println("\n");
                                                System.out.printf("%16s %18s %18s %18s %18s", resultSet.getString(1), resultSet.getLong(2), resultSet.getDouble(3)
                                                        , resultSet.getDouble(4), resultSet.getInt(5));
                                                System.out.println("\n");
                                            }
                                            preparedStatement.close();
                                        } catch (Exception e) {
                                            System.out.println("Search failed! Account doesn't exist..!!" + e);
                                        }
                                        break;
                                    case 2:
                                        try {
                                            Saving_Account saving_account = new Saving_Account() {
                                                @Override
                                                public void deposit(long AcNo) throws SQLException {
                                                    super.deposit(AcNo);
                                                }
                                            };
                                            saving_account.deposit(AcNo);
                                        } catch (Exception e) {
                                            System.out.println("Please Enter Valid Account Number..!!" + e);
                                        }
                                        break;
                                    case 3:
                                        try {
                                            Saving_Account account2 = new Saving_Account() {
                                                @Override
                                                public void withdrawal(long AcNo) throws SQLException {
                                                    super.withdrawal(AcNo);
                                                }
                                            };
                                            account2.withdrawal(AcNo);
                                        } catch (Exception e) {
                                            System.out.println("Withdrawal Not Allowed,Please Check Account Balance " + e);
                                        }
                                        break;
                                    case 4:
                                        try {
                                            Saving_Account account3 = new Saving_Account() {
                                                @Override
                                                public void moneyTransfer(long AcNo) throws SQLException {
                                                    super.moneyTransfer(AcNo);
                                                }
                                            };
                                            account3.moneyTransfer(AcNo);
                                        } catch (Exception e) {
                                            System.out.println("Sorry!!!Money Transfer is Not Processed!!! Please Try Again...!!! " + e);
                                        }
                                        break;
                                    case 5:
                                        Saving_Account account4 = new Saving_Account() {
                                            @Override
                                            public void acStatement(long AcNo) throws SQLException {
                                                super.acStatement(AcNo);
                                            }
                                        };
                                        account4.acStatement(AcNo);
                                        break;
                                    case 6:
                                        System.out.println("Thank You For Banking with Us...!!!");
                                        break;
                                }
                            }else {
                                System.out.println("Account Number "+AcNo+" does not Available in Savings Account");
                                String[] b={"xyz"};
                                main(b);
                            }
                        }
                        while (choice != 6);
                        break;
                    case 2:
                        int choice1 = 0;
                        System.out.println("Enter Account Number ");
                        AcNo = scanObj.nextLong();
                        do {
                            try {
                                long x = 0;
                                String query4 = "select AcNo from PayAc where AcNo=?";
                                PreparedStatement preparedStatement4 = connection.prepareStatement(query4);
                                preparedStatement4.setLong(1, AcNo);
                                ResultSet resultSet4 = preparedStatement4.executeQuery();
                                while (resultSet4.next()) {
                                    x = resultSet4.getLong("AcNo");
                                }
                                if (x == AcNo) {
                                    System.out.println("\n ***IDBC ONLINE BANKING***");
                                    System.out.println("1. Display Pay Account Details \n2. Deposit Amount \n3. Money Transfer \n4. Pay Bill \n5. Account Statement \6. Exit ");
                                    System.out.println("Enter your choice: ");
                                    choice1 = scanObj.nextInt();
                                    switch (choice1) {
                                        case 1:
                                            double balance = 0;
                                            try {
                                                String query0 = "select Balance from Pay_Statement where PayAcNo=?";
                                                PreparedStatement preparedStatement1 = connection.prepareStatement(query0);
                                                preparedStatement1.setLong(1, AcNo);
                                                ResultSet resultSet1 = preparedStatement1.executeQuery();
                                                while (resultSet1.next()) {
                                                    balance = resultSet1.getDouble("Balance");
                                                }
                                                String query1 = "update PayAc set Balance=" + balance + "where AcNo=?";
                                                PreparedStatement preparedStatement2 = connection.prepareStatement(query1);
                                                preparedStatement2.setLong(1, AcNo);
                                                int row1 = preparedStatement2.executeUpdate();
                                            } catch (SQLException ex) {
                                                throw new RuntimeException(ex);
                                            }
                                            try {
                                                String query = "select * from PayAc where AcNo=" + AcNo + ";";
                                                PreparedStatement preparedStatement = connection.prepareStatement(query);
                                                ResultSet resultSet = preparedStatement.executeQuery();
                                                System.out.println("\n");
                                                System.out.printf("%16s %18s %18s %18s", "Customer Name", "AcNo", "CustomerId", "Balance");
                                                System.out.println("\n");
                                                while (resultSet.next()) {
                                                    System.out.printf("%16s %18s %18s %18s", resultSet.getString(1), resultSet.getLong(2), resultSet.getInt(3)
                                                            , resultSet.getDouble(4));
                                                }
                                                System.out.println("\n");
                                            } catch (Exception e) {
                                                System.out.println("Search failed! Account doesn't exist..!!" + e);
                                            }
                                            break;
                                        case 2:
                                            try {
                                                Pay_Account account = new Pay_Account() {
                                                    @Override
                                                    public void deposit(long AcNo) throws SQLException {
                                                        super.deposit(AcNo);
                                                    }
                                                };
                                                account.deposit(AcNo);
                                            } catch (Exception e) {
                                                System.out.println("Please Enter Valid Account Number..!!" + e);
                                            }
                                            break;
                                        case 3:
                                            try {
                                                Pay_Account account = new Pay_Account() {
                                                    @Override
                                                    public void moneyTransfer(long AcNo) throws SQLException {
                                                        super.moneyTransfer(AcNo);
                                                    }
                                                };
                                                account.moneyTransfer(AcNo);
                                            } catch (Exception e) {
                                                System.out.println("Insufficient Balance " + e);
                                            }
                                            break;
                                        case 4:
                                            try {
                                                Pay_Account account = new Pay_Account() {
                                                    @Override
                                                    public void BillPay(long AcNo) throws SQLException {
                                                        super.BillPay(AcNo);
                                                    }
                                                };
                                                account.BillPay(AcNo);
                                            } catch (Exception e) {
                                                System.out.println("Sorry!!! Your Transactions Can not Be Processed " + e);
                                            }
                                            break;
                                        case 5:
                                            Pay_Account account = new Pay_Account() {
                                                @Override
                                                public void acStatement(long AcNo) throws SQLException {
                                                    super.acStatement(AcNo);
                                                }
                                            };
                                            account.acStatement(AcNo);
                                            break;
                                        case 6:
                                            System.out.println("Thank You For Banking with Us...");
                                            break;
                                        default:
                                            System.out.println("Invalid Choice, Try Again...!!!");
                                    }
                                } else {
                                    System.out.println("Account Number +" + AcNo + " does not exist in Pay Account");
                                    String[] a={"abc"};
                                    main(a);
                                }
                                } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        while (choice1!=6);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}