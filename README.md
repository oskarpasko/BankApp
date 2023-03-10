# BankApp
Banking application project for OOP classes at the University.

# Table of contents
- [Errors](#Errors)
  - [Error 1](#Error-1)
  - [Error 12](#Error-12)
  - [Error 123](#Error-123)
- [Authors](#Authors)
- [Technicals](#Technicals)
- [License](#License)

## Errors
List of errors which You can see in application.

### Error 1
This error is showing when application have problem with connection during log in to app. </br>

Download and add JDBC connector to project. </br>
Download MySQL connector from [here!](https://dev.mysql.com/downloads/connector/j/)</br>

Adding MySQL connector: </br>

```bash
  File -> Project Structure -> Modules
```
Click on '+' button. Then choose JAR option and then choose mysql-connector file from folder where You downloaded. </br>
After that check connector in box and click 'Apply'. </br>

If Your problem is not solved try to next step below. </br>

Check if Your database is online or if Your connection is invalid! </br>
To check connection you should have login and password to database and URL. </br>
</br>
Example connection to database 'schema' with user with login 'root' and password 'rootroot'
```Java
  Connection connection = (Connection) DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/schema",
                    "root", 
                    "rootroot");
```

### Error 12
This error is showing when application have problem with connection after log in to app. </br>
Check if Your database is online or if Your connection is invalid! </br>
To check connection you should have login and password to database and URL. </br>
</br>
Example connection to database 'schema' with user with login 'root' and password 'rootroot'
```Java
  Connection connection = (Connection) DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/schema",
                    "root", 
                    "rootroot");
```

### Error 123
This error is showing when data which You want use does not exist. </br>
You should check data in Your Database.

## Authors

- [@eltwor](https://www.github.com/eltwor)
- [@oskarpasko](https://www.github.com/oskarpasko)

## Technicals
Version: 1.0 </br>
Languages: Java, MySQL </br>
Software environment: IntelliJ IDEA, MySQL Workbench </br>
SDK: 18.0.2 </br>
Operating Systems: Windows, macOS </br>

## License

[MIT](https://choosealicense.com/licenses/mit/)
