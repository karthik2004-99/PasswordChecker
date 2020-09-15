##Password Checker

####How to Test:

Clone/Zip the project from GitHub.
Link for it is given below.

>Make sure your system has H2 Data Base installed and is added in the `PATH`
or as a library.
 
[Link](https://github.com/karthik2004-99/PasswordChecker)

Run the program!

##Project Structure

The project is divided into 4 classes.

    1.PasswordChecker.java
    2.Validate.java
    3.H2DBServer.java
    4.Hash.java
    
 * PasswordChecker.java is the main class.<br></br>
 
 * H2DBServer.java is the class where all the implementation takes place
 
 * Validate.java is used to layer the function calls between main class and H2DBServer class.
 
 * Hash.java is used for SHA256 hashing.





##DataBase Structure

Database structure is as follows:
  <br></br>
  
    | Unique Key | Username | Password[0..5] | LoggedinTime | LockedStatus |
    |------------|----------|----------------|--------------|--------------|
    |            |          |                |              |              |
    |            |          |                |              |              |

   <br></br>
>I have chosen H2 database as the database for this project.
>>The database scheme is not the ideal practices, and I should have used separate tables for user details, passwords and 
JOIN them with foreign key(username)

##Secure Storage

For secure storage of passwords in the database, I have used [SHA256 encryption](https://www.movable-type.co.uk/scripts/sha256.html#:~:text=A%20cryptographic%20hash%20(sometimes%20called,byte)%20signature%20for%20a%20text.) to Hash the passwords and store 
the hash.

##Account Lock Status

A variable is used to check for login attempts with wrong password and
if 5 wrong attempts are made the account is locked.

Boolean value of the status is stored in Database.

>No Action can be performed on Locked Account

##Password Requirements

According to the given task, the password policy that has to be maintained
is as follows:
    
    The password shall be case sensitive and should contain at least one each of the following
    characters with no space:
        a. Uppercase : A to Z
        b. Lowercase : a to z
        c. Digit : 0 to 9
        d. Non-alphanumeric : Special characters @ # $ % & * / \
        e. Password cannot be default
        f. Password length should be 8 characters


##Password Update

According to the given task, each User needs to Update the password
after every 14 days.

To cater to this need, [System Time](https://docs.oracle.com/javase/7/docs/api/java/lang/System.html) has been used.
The difference of times is taken and checked whether 14 days have passed.
