import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class Address {
    private int houseNo;
    private String street;
    private String city;

    public Address(int houseNo, String street, String city) {
        this.houseNo = houseNo;
        this.street = street;
        this.city = city;
    }

    public Address() {

    }

    public int isPhoneNoValid(String phoneNo)
    {
        int result=0;
        Pattern pattern=Pattern.compile("^['+'](91)[0-9]{10}$");
        Matcher m=pattern.matcher(phoneNo);
        if(m.find()) //&& m.group().equals(phone))
        {
            result= Integer.parseInt(phoneNo);
        }else {
            System.out.println("Invalid Mobile Number, Please Try Again...!!!");
        }
        return result;
    }

    public int getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(int houseNo) {
        this.houseNo = houseNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public void setPhoneNo(String phoneNo) {
        int c = isPhoneNoValid(phoneNo);
        if (c == 1) {
            this.setPhoneNo(phoneNo);
            System.out.println(phoneNo);
        } else {
            System.out.println("Given an invalid phone number");
        }


    }
    @Override
    public String toString() {
        return "Address{" + "houseNo=" + houseNo + ", street='" + street + '\'' + ", city='" + city + '\'' + '\'' + '}';
    }
}

