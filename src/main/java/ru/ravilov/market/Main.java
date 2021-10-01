package ru.ravilov.market;

import com.diogonunes.jcolor.AnsiFormat;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

public class Main {

    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        Seller seller = new Seller("Iphone 6", 6200, 1);
        Seller seller2 = new Seller("Iphone 7", 7800, 1);
        Seller seller3 = new Seller("Iphone X", 14000, 2);
        Seller seller4= new Seller("Samsung A51", 12000, 2);
        Seller seller5 = new Seller("Samsung A52", 30000, 10);
        Seller seller6 = new Seller("Samsung A70", 14000, 6);
        Seller seller7 = new Seller("Iphone SE", 5000, 5);
        Seller seller8 = new Seller("Iphone 5", 3000, 4);
        Seller seller9 = new Seller("Iphone 8", 10000, 1);
        Seller seller10 = new Seller("Iphone XR", 12000, 2);
        ArrayList<Seller> sellers = new ArrayList<>();
        sellers.add(seller);
        sellers.add(seller2);
        sellers.add(seller3);
        sellers.add(seller4);
        sellers.add(seller5);
        sellers.add(seller6);
        sellers.add(seller7);
        sellers.add(seller8);
        sellers.add(seller9);
        sellers.add(seller10);
        ServiceLogic serviceLogic = null;
        List<Customer> customerList = new ArrayList<>();
        try {
            serviceLogic = new ServiceLogic();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            AnsiFormat yellowWord = new AnsiFormat(YELLOW_TEXT());
            AnsiFormat blueWord = new AnsiFormat(BLUE_TEXT());
            AnsiFormat redWord = new AnsiFormat(RED_TEXT());
            System.out.println(yellowWord.format("Выберите желаемое действие: \n" +
                    "1) Загрузить покупателей в БД \n" +
                    "2) Загрузить продавцов в БД \n" +
                    "3) Показать всех покупателей \n" +
                    "4) Поиск покупателя по id \n" +
                    "5) Поиск покупателя по сумме\n" +
                    "6) Обновить наличность покупателя\n" +
                    "7) Удалить покупателя по id\n" +
                    "8) Получить общую информацию о кошельках\n" +
                    "9) Совершить обмен\n" +
                    "10) Добавление покупателя\n"));

            boolean isActive = true;
            while (isActive) {
                String select = reader.readLine();
                if (select.equals("1")) {
                    System.out.print("Выберите число пользователей: ");
                    int count = Integer.parseInt(reader.readLine());
                    serviceLogic.insertIntoCustomer(count);
                } else if (select.equals("2")) {
                    serviceLogic.insertIntoSeller(sellers);
                } else if (select.equals("3")) {
                    serviceLogic.showAllCustomer(customerList);
                } else if (select.equals("4")) {
                    System.out.print(blueWord.format("Введите id пользователя: "));
                    int id = Integer.parseInt(reader.readLine());
                    serviceLogic.selectCustomerWithId(id);
                } else if (select.equals("5")) {
                    System.out.print(blueWord.format("Введите сумму: "));
                    int cash = Integer.parseInt(reader.readLine());
                    serviceLogic.selectCustomerWithCash(cash);
                } else if (select.equals("6")) {
                    System.out.print(redWord.format("Введите id: "));
                    int id = Integer.parseInt(reader.readLine());
                    System.out.print(blueWord.format("Введите сумму: "));
                    int cash = Integer.parseInt(reader.readLine());
                    serviceLogic.updateCustomerCash(id,cash);
                } else if (select.equals("7")) {
                    System.out.print(redWord.format("Введите id пользователя: "));
                    int id = Integer.parseInt(reader.readLine());
                    serviceLogic.deleteCustomerWithId(id);
                } else if (select.equals("8")) {
                    serviceLogic.infoOfAllCustomers();
                } else if (select.equals("9")){
                    System.out.println(redWord.format("Введите id покупателя: "));
                    int customerId = Integer.parseInt(reader.readLine());
                    System.out.println(yellowWord.format("Введите id продавца: "));
                    int sellerId = Integer.parseInt(reader.readLine());
                    serviceLogic.trade(customerId,sellerId);
                } else if (select.equals("10")){
                    System.out.println(blueWord.format("Введите желаемую сумму: "));
                    int cash = Integer.parseInt(reader.readLine());
                    serviceLogic.insertOneCustomer(cash);
                }
                System.out.println(blueWord.format("Продолжить выполнение программы?"));
                String c = reader.readLine();
                if (c.equalsIgnoreCase("да")) {
                    isActive = true;
                } else {
                    isActive = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}