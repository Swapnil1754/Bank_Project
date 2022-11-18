import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class Saving_Account {
    private long acNo;
    private String Name;
    private String Ac_type;
    private double balance;
    private String DOB;
    private long Adhar;
    private long mobileNo;
    private double ROI;
    private long number;
    private Address correspondence, permanent;

    public Saving_Account(long acNo, String name, String ac_type, double balance, String DOB, long Adhar, long mobileNo, double ROI, long number, Address correspondence, Address permanent, Scanner scanObj) {
        this.acNo = acNo;
        Name = name;
        Ac_type = ac_type;
        this.balance = balance;
        this.DOB = DOB;
        this.Adhar = Adhar;
        this.mobileNo = mobileNo;
        this.ROI = ROI;
        this.number = number;
        this.correspondence = correspondence;
        this.permanent = permanent;
        this.scanObj = scanObj;
    }

    Scanner scanObj = new Scanner(System.in);

    public Saving_Account() {

    }


    //method to open new account
    public void openAccount() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MyBank", "root", "welcome@2021");
        int CustomerId = 0;
        System.out.println("Please Fill Saving Account Opening Form");
        System.out.println("Enter Your Full Name: ");
        String FullName = scanObj.nextLine();
        Date date = new Date();
        System.out.println("Enter Valid Phone Number(+918982433590/+918380993380)");
        String x = scanObj.next();
        Pattern pattern = Pattern.compile("^[+](91)[0-9]{10}$");
        Matcher m = pattern.matcher(x);
        if (m.find() && m.group().equals(x)) {
            mobileNo = Long.parseLong(x);
            System.out.println("Enter Valid Email");
            String Email = scanObj.next();
            System.out.println("Enter Your Date of Birth(YYYY/MM/DD)");
            DOB = scanObj.next();
            System.out.println("Enter Your PAN Number");
            String PAN = scanObj.next();
            System.out.println("Enter Your Adhar No. :");
            Adhar = scanObj.nextLong();
            System.out.println("Enter Your City");
            String City = scanObj.next();
            try {
                String query = "insert into Customers(CustomerId,FullName,Mobile,Email,DOB,PAN,Adhar,City)values(?,?,?,?,?,?,?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, CustomerId);
                preparedStatement.setString(2, FullName.trim());
                preparedStatement.setString(3, x);
                preparedStatement.setString(4, Email);
                preparedStatement.setString(5, DOB);
                preparedStatement.setString(6, PAN);
                preparedStatement.setLong(7, Adhar);
                preparedStatement.setString(8, City);
                int row = preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException ex) {
                System.out.println("You Have Entered data which is duplication of Previous records, Please Try Again...!!!/n"+ex);
            }
            try {
                Random rand = new Random();
                long AcNo1 = (rand.nextInt(1000000) + 1000000000l) * (rand.nextInt(900) + 100);
                String query1 = "insert into SavingAc(CustomerName,AcNo)values(?,?);";
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                preparedStatement1.setString(1, FullName.trim());
                preparedStatement1.setLong(2, AcNo1);
                int row1 = preparedStatement1.executeUpdate();
                System.out.println("Congratulations...!!! Your Saving Account Created Successfully...!!!");
                System.out.println("Your Account Number is : " + AcNo1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                System.out.println("Error-------" + e);
            } finally {
                connection.close();
            }
        }else {
            System.out.println("Please Enter Mobile Number in Given Format only");
        }
    }

    //method to deposit money
    public void deposit(long AcNo) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MyBank", "root", "welcome@2021");
        double amount;
        System.out.println("Enter the amount you want to deposit: ");
        amount = scanObj.nextDouble();
        if (amount > 0) {
            try {
                String query = "update SavingAc set Balance=Balance+" + amount + "where AcNo=" + AcNo + ";";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                int row = preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                balance = 0;
                Date Transaction_Date = new Date();
                String query2 = "select Balance from Saving_Statement where SavingAcNo=?";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                preparedStatement2.setLong(1, AcNo);
                ResultSet resultSet = preparedStatement2.executeQuery();
                while (resultSet.next()) {
                    balance = resultSet.getDouble("Balance");
                }
                String query1 = "insert into Saving_Statement(SavingAcNo,Deposit,Balance)values(?,?,?);";
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                preparedStatement1.setLong(1, AcNo);
                preparedStatement1.setDouble(2, amount);
                preparedStatement1.setDouble(3, balance + amount);
                int row1 = preparedStatement1.executeUpdate();
                System.out.println("Amount Deposited Successfully...!!!");
                preparedStatement1.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                connection.close();
            }
        }else {
            System.out.println("Enter Valid Amount");
        }
    }

    public void withdrawal(long AcNo) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MyBank", "root", "welcome@2021");
        double amount = 0;

        System.out.println("Enter the amount you want to withdraw: ");
        amount = scanObj.nextDouble();
        if (amount > 0) {
            try {
                double x = 0;
                String query00 = "select Balance from SavingAc where AcNo=?";
                PreparedStatement preparedStatement00 = connection.prepareStatement(query00);
                preparedStatement00.setLong(1, AcNo);
                ResultSet resultSet00 = preparedStatement00.executeQuery();
                while (resultSet00.next()) {
                    x = resultSet00.getDouble("Balance");
                }
                if (x > amount) {
                    try {
                        String query = "update SavingAc set Balance=Balance-" + amount + "where AcNo=" + AcNo + " and Balance > " + amount + ";";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        int row = preparedStatement.executeUpdate();
                        preparedStatement.close();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        balance = 0;
                        String query = "select Balance from Saving_Statement where SavingAcNo=?";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setLong(1, AcNo);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            balance = resultSet.getDouble("Balance");
                        }
                        String query1 = "insert into Saving_Statement(SavingAcNo,Withdrawal,Balance)values(?,?,?);";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                        preparedStatement1.setLong(1, AcNo);
                        preparedStatement1.setDouble(2, amount);
                        preparedStatement1.setDouble(3, balance - amount);
                        int row1 = preparedStatement1.executeUpdate();
                        preparedStatement1.close();
                        System.out.println("\n");
                        System.out.println("Amount of " + amount + " Withdrawn from Your Account");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        System.out.println("Your balance is less than Withdrawal Amount " + "\tTransaction failed...!!");
                    } finally {
                        connection.close();
                    }
                } else {
                    System.out.println("Withdrawal Can't be Proceeded Please Check Account Balance...!!!");
                }
            } catch (SQLException | RuntimeException e) {
                throw new RuntimeException(e);
            }
        }else {
            System.out.println("Invalid Amount...Try Again...!!!");
        }
    }

        public void moneyTransfer(long AcNo) throws SQLException {
            double amount;
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MyBank", "root", "welcome@2021");
            System.out.println("Enter Account Number");
            AcNo = scanObj.nextLong();
            System.out.println("Enter Beneficiary Account Number");
            long AcNo1 = scanObj.nextLong();
            System.out.println("Enter Amount You want to Transfer: ");
            amount = scanObj.nextDouble();
            if (amount > 0) {
                try {
                    double x = 0;
                    long b = 0;
                    String query00 = "select Balance from SavingAc where AcNo=?";
                    PreparedStatement preparedStatement00 = connection.prepareStatement(query00);
                    preparedStatement00.setLong(1, AcNo);
                    ResultSet resultSet00 = preparedStatement00.executeQuery();
                    while (resultSet00.next()) {
                        x = resultSet00.getDouble("Balance");
                    }
                    if (x > amount) {
                        try {
                            String querry00 = "select AcNo from SavingAc where AcNo=?";
                            PreparedStatement preparedStatement0000 = connection.prepareStatement(querry00);
                            preparedStatement0000.setLong(1, AcNo1);
                            ResultSet rs = preparedStatement0000.executeQuery();
                            while (rs.next()) {
                                b = rs.getLong("AcNo");
                            }
                            if (b == AcNo1) {

                                try {
                                    String query = "update SavingAc set Balance=Balance-" + amount + "where AcNo=" + AcNo + " and Balance > " + amount + ";";
                                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                                    int row = preparedStatement.executeUpdate();
                                    preparedStatement.close();
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                } catch (Exception e) {
                                    System.out.println("Error 1" + e);
                                }
                                try {
                                    balance = 0;
                                    String query = "select Balance from Saving_Statement where SavingAcNo=?";
                                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.setLong(1, AcNo);
                                    ResultSet resultSet = preparedStatement.executeQuery();
                                    while (resultSet.next()) {
                                        balance = resultSet.getDouble("Balance");
                                    }
                                    String query2 = "insert into Saving_Statement(SavingAcNo,TransferredFrom,TransferredTo,Withdrawal,Balance)values(?,?,?,?,?);";
                                    PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                                    preparedStatement2.setLong(1, AcNo);
                                    preparedStatement2.setLong(2, AcNo);
                                    preparedStatement2.setLong(3, AcNo1);
                                    preparedStatement2.setDouble(4, amount);
                                    preparedStatement2.setDouble(5, balance - amount);
                                    int row = preparedStatement2.executeUpdate();
                                    preparedStatement2.close();
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                } catch (Exception e) {
                                    System.out.println("Error 3" + e);
                                }
                                try {
                                    b = 0;
                                    String queryy = "select AcNo from SavingAc where AcNo=?";
                                    PreparedStatement preparedStatement000 = connection.prepareStatement(queryy);
                                    preparedStatement000.setLong(1, AcNo1);
                                    ResultSet resultSet000 = preparedStatement000.executeQuery();
                                    while (resultSet000.next()) {
                                        b = resultSet000.getLong("AcNo");
                                    }
                                    if (b == AcNo1) {
                                        try {
                                            String query1 = "update SavingAc set Balance=Balance+" + amount + "where AcNo=" + AcNo1 + ";";
                                            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                                            int row1 = preparedStatement1.executeUpdate();
                                            preparedStatement1.close();
                                        } catch (SQLException ex) {
                                            throw new RuntimeException(ex);
                                        } catch (Exception e) {
                                            System.out.println("Error 2" + e);
                                        }
                                        try {
                                            balance = 0;
                                            String query = "select Balance from Saving_Statement where SavingAcNo=?";
                                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                                            preparedStatement.setLong(1, AcNo1);
                                            ResultSet resultSet = preparedStatement.executeQuery();
                                            while (resultSet.next()) {
                                                balance = resultSet.getDouble("Balance");
                                            }
                                            String query3 = "insert into Saving_Statement(SavingAcNo,TransferredFrom,TransferredTo,Deposit,Balance)values(?,?,?,?,?);";
                                            PreparedStatement preparedStatement3 = connection.prepareStatement(query3);
                                            preparedStatement3.setLong(1, AcNo1);
                                            preparedStatement3.setLong(2, AcNo);
                                            preparedStatement3.setLong(3, AcNo1);
                                            preparedStatement3.setDouble(4, amount);
                                            preparedStatement3.setDouble(5, balance + amount);
                                            int row = preparedStatement3.executeUpdate();
                                            preparedStatement3.close();
                                            System.out.println("Your Money Transferred Successfully...!!!");
                                        } catch (Exception e) {
                                            System.out.println("Sorry!!!Your Transactions Can Not be Proceed at this time..." + e);
                                            System.out.println("\n");
                                        } finally {
                                            connection.close();
                                        }
                                    } else {
                                        System.out.println("Invalid Account Number Please Try Again...!!!");
                                    }
                                } catch (SQLException | RuntimeException e) {
                                    throw new RuntimeException(e);
                                }

                            } else {
                                System.out.println("Money Transfer Can't be Proceeded Please Check Beneficiary Account Number...!!!");
                            }
                        } catch (SQLException | RuntimeException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                System.out.println("Invalid Amount, Please Try Again...!!!");
            }
        }
    public void acStatement(long AcNo) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MyBank", "root", "welcome@2021");
        try {
            String query = "select * from Saving_Statement where SavingAcNo=" + AcNo + ";";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("\n");
            System.out.printf("%18s %20s %18s %18s %18s %18s %18s %18s", "TransactionId", "TransactionDate", "SavingAcNo","TransferredFrom","TransferredTo", "Deposit", "Withdrawal", "Balance");
            System.out.println("\n");
            while (resultSet.next()) {
                System.out.printf("%18s %20s %18s %18s %18s %18s %18s %18s", resultSet.getInt(1), resultSet.getString(2), resultSet.getLong(3),
                        resultSet.getLong(4), resultSet.getLong(5), resultSet.getDouble(6),resultSet.getDouble(7),resultSet.getDouble(8));
                System.out.println("\n");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("Invalid Account Number");
        } finally {
            connection.close();
        }
    }
}
