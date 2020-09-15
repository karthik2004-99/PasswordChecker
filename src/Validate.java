import java.util.regex.Pattern;

public class Validate {

    H2DBServer db = new H2DBServer();
    public Validate(){}
    public boolean validatePassword(String password){
        if(password.length()<8)
            return true;
        if(password.equals("password"))
            return true;

        int upperCount =0,lowerCount =0,digitCount =0,nonAlphaCount =0;
        for(int i=0;i<password.length();i++){
            if(Character.isUpperCase(password.charAt(i))){
                upperCount++;
            }if(Character.isLowerCase(password.charAt(i))){
                lowerCount++;
            }if(Character.isDigit(password.charAt(i))){
                digitCount++;
            }
        }
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        if(p.matcher(password).find()){
            nonAlphaCount++;
        }
        return upperCount <= 0 || lowerCount <= 0 || digitCount <= 0 || nonAlphaCount <= 0;
    }

    public boolean validateUserID(String user){
        return db.isUserExist(user);

    }
    public boolean validatePasswordInDB(String username, String password){
        return db.isPasswordCorrect(username,password);
    }

    public void lockAccount(String userid){
        db.lockAccount(userid);
    }

    public void insertData(String username, String password, long time, boolean locked){
        db.insertData(username,password,time,locked);
    }

    public boolean getLockStatus(String username){
        return db.getLockStatus(username);

    }
    public boolean needPasswordReset(String username){
        return db.getResetStatus(username);
    }
    public boolean updatePassword(String username, String newpassword){
        return db.updatePassword(username,newpassword);
    }

}
