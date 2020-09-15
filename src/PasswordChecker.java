/*
Program : PasswordChecker
Objectives Met :
        1.Length of password checked
        2.Password Case sensivity along with required condition checked
        3.User password update every 14 days checked
        4.Password comparision with previous 5 checked
        5.UserID locked after 5 attempts checked
        6.User cannot set default "password" as password checked
        7.Secure Storage and Access
            1. SHA265 Hashing has been used for passwords
            2. H2 database is used for storing the userID, passwords, loggedTime , lockedStatus
*/
import java.security.NoSuchAlgorithmException;
import java.util.*;
public class PasswordChecker {

    static String WELCOME ="Welcome to the Password Checker!\n\"New or Existing User?\n" +
            "Type 'new' for New User or 'existing' for Existing User";
    static String WELCOME2 ="Welcome to Password Checker\nPlease Enter UserID and Password";
    static String PASSWORDREQ = "Password must contain min 8 letters and must have atleast one digit, non-alphanumeric, one lowercase,one uppercase letters!";
    static String ENTERUSERID = "Enter userID";
    static String USEREXIST = "UserID exist!\nEnter another Username";
    static String USERDOESNTEXIST = "UserID doesn't exist!";
    static String ENTERPASS = "Enter password";
    static String PASSMEET ="Password meets requirements";
    static String PASSDOESNT ="Password doesnt meet requirements\nEnter new password";
    static String PASSWRONG = "Wrong Password. Try Again!";
    static String ACCOUNTLOCKED = "Account Locked!";
    static String UPDATEMSG ="Its been more than 14 Days since the password was last reset!,Please enter new password";
    static String SUCCESS = "Log In Successful!";
    static String INVALID ="Invalid Argument";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner sc = new Scanner(System.in);
        System.out.println(WELCOME);
        String input = sc.next().toLowerCase();
        H2DBServer db = new H2DBServer();
        Validate validate = new Validate();
        Hash hash = new Hash();
        db.createTable();                               // creates DataBase Table DETAILS
        switch (input) {
                case "new":
                    createUser(sc, validate, hash);
                    break;
                case "existing":
                    validateUser(sc, validate, hash);
                    break;
                default: {
                    System.out.println(INVALID);
                }
            }


    }

    public static void createUser(Scanner sc, Validate validate,Hash hash) throws NoSuchAlgorithmException {
        System.out.println(WELCOME2+"\n"+PASSWORDREQ);
        System.out.println(ENTERUSERID);
        String userid = sc.next();
        while(validate.validateUserID(userid)){
            System.out.println(USEREXIST);
            userid = sc.next();
        }
        System.out.println(ENTERPASS);
        String pass = sc.next();
        while(validate.validatePassword(pass)){
                System.out.println(PASSDOESNT);
                pass = sc.next();
            }
            System.out.println(PASSMEET);

        // System time is taken to measure the 14 day deadline.
        long currentTime = System.currentTimeMillis();

        //hash password before inserting in DATABASE
        String hashedpassword = hash.returnHash(pass);

        //inserts new user data into database
        validate.insertData(userid,hashedpassword,currentTime,false);


    }

    public static void validateUser(Scanner sc,Validate validate,Hash hash) throws NoSuchAlgorithmException {
        System.out.println(ENTERUSERID);
        String userid = sc.next();
        System.out.println(ENTERPASS);
        String password = sc.next();
        int countPasswordAttempts =0;
        while(!validate.validateUserID(userid)){ // validating Username to check if its present in DB
            System.out.println(USERDOESNTEXIST);
            userid = sc.next();
            System.out.println(ENTERPASS);
            password = sc.next();
        }
        boolean lockAccount = validate.getLockStatus(userid);
        if(!lockAccount) {
            String hashedpassword = hash.returnHash(password);
            while (!validate.validatePasswordInDB(userid, hashedpassword)) { //Checking if the password entered matches with password in DB
                System.out.println(PASSWRONG);
                countPasswordAttempts++;
                if (countPasswordAttempts == 5) {
                    lockAccount = true;
                    break;
                }
                password = sc.next();
                hashedpassword = hash.returnHash(password);
            }
        }
        if(lockAccount){        // 5 wrong attempts -> Account Lock
            System.out.println(ACCOUNTLOCKED);
            validate.lockAccount(userid);
            return;
        }
        if(validate.needPasswordReset(userid)){  // to check if password needs to be reset
             System.out.println(UPDATEMSG);
             String newpassword = sc.next();
             while(validate.validatePassword(newpassword)){
                 System.out.println(PASSDOESNT);
                 newpassword = sc.next();
             }
             String hashedpassword = hash.returnHash(newpassword);
             while(!validate.updatePassword(userid,hashedpassword)){
                  newpassword = sc.next();
                  hashedpassword = hash.returnHash(newpassword);
             }
        }
        System.out.println(SUCCESS);

    }



}
