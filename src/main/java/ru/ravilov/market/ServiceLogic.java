package ru.ravilov.market;

import com.diogonunes.jcolor.AnsiFormat;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

import static com.diogonunes.jcolor.Attribute.*;

public class ServiceLogic {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ServiceLogic() throws IOException, SQLException {
        Properties properties = new Properties();
        try (final InputStream stream = Main.class.getClassLoader().getResourceAsStream("database.properties")) {
            properties.load(stream);
        }
        String url = properties.getProperty("database.url");
        String username = properties.getProperty("database.username");
        String password = properties.getProperty("database.password");
        Statement statement = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            clearTables();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Очистка таблиц
     */
    private void clearTables() {
        try (Statement statement = connection.createStatement()) {
            //statement.executeUpdate("SELECT 'TRUNCATE TABLE' + TABLE_NAME from market.INFORMATION_SCHEMA.TABLES where TABLE_NAME in ('customer', 'seller')");
            statement.executeUpdate("TRUNCATE TABLE customer ");
            statement.executeUpdate("TRUNCATE TABLE seller");
            Main.logger.info("Очистка таблиц завершена");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Добавление новых покупателей в базу данных
     * количество добавляемых людей регулируется
     * параметром
     *
     * @param countCustomer
     * @throws SQLException
     */
    public void insertIntoCustomer(int countCustomer) throws SQLException {
        //ArrayList<Customer> customers = new ArrayList<>();
        String sql = "INSERT INTO customer(cash) values(?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= countCustomer; i++) {
                int randomCash = (int) (1000 + Math.random() * 10000);
                Customer customer = new Customer(randomCash);
                ps.setInt(1, randomCash);
                ps.addBatch();
            }
            ps.executeBatch();
            Main.logger.info("Добавление покупателей завершено");
        }
    }

    /**
     * Вывод всех покупателей и
     * добавление в список
     *
     * @param customerList
     * @return
     * @throws SQLException
     */
    public List<Customer> showAllCustomer(List<Customer> customerList) throws SQLException {
        String sql = "SELECT * FROM customer";
        PreparedStatement showAllStatement = connection.prepareStatement(sql);
        ResultSet res = showAllStatement.executeQuery();
        if (res == null)
            System.out.println("Список пуст!");
        while (res.next()) {
            Integer id = res.getInt("id");
            Integer cash = res.getInt("cash");
            Customer customer = new Customer(id, cash);
            customerList.add(customer);
        }
        for (Customer c : customerList) {
            if (c.getCash() < 2000){
                AnsiFormat fWarning = new AnsiFormat(RED_TEXT());
                System.out.println("id: " + c.getId() + fWarning.format(",cash: " + c.getCash()));
            } else if(c.getCash() > 2000 && c.getCash() < 4000){
                AnsiFormat fWarning = new AnsiFormat(YELLOW_TEXT());
                System.out.println("id: " + c.getId() + fWarning.format(",cash: " + c.getCash()));
            } else if (c.getCash() > 4000){
                AnsiFormat fWarning = new AnsiFormat(GREEN_TEXT());
                System.out.println("id: " + c.getId() + fWarning.format(",cash: " + c.getCash()));
            }
        }
        return customerList;
    }

    /**
     * Добавление покупателя
     * @param cash
     */
    public void insertOneCustomer(int cash) {
        String sql = "INSERT INTO customer(cash) values(?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cash);
            ps.addBatch();
            ps.executeBatch();
            Main.logger.info("Добавление покупателя завершено");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Поиск по id
     * @param id
     * @throws SQLException
     */
    public void selectCustomerWithId(Integer id) throws SQLException {
        String sql = "SELECT id,cash FROM customer WHERE id = ?";
        PreparedStatement  preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        ResultSet res = preparedStatement.executeQuery();
        while (res.next()){
             id = res.getInt("id");
             Integer cash = res.getInt("cash");
             Customer customer = new Customer(id,cash);
             System.out.printf("Поиск по id: id: %d,cash: %d\n",customer.getId(),customer.getCash());
        }
    }

    /**
     * Поиск по сумме кошелька
     * @param cash
     * @throws SQLException
     */
    public void selectCustomerWithCash(Integer cash) throws SQLException {
        String sql = "SELECT id,cash FROM customer WHERE cash = ?";
        PreparedStatement  preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,cash);
        ResultSet res = preparedStatement.executeQuery();
        while (res.next()){
            Integer id = res.getInt("id");
            cash = res.getInt("cash");
            Customer customer = new Customer(id,cash);
            System.out.printf("Поиск по cash: id: %d,cash: %d\n",customer.getId(),customer.getCash());
        }
    }

    /**
     * Удаление покупателя по id
     * @param id
     * @throws SQLException
     */
    public void deleteCustomerWithId(Integer id) throws SQLException {
        String sql = "DELETE FROM customer WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        ResultSet res = preparedStatement.executeQuery();
        System.out.printf("Удаление id:%d выполнено\n",id);
    }

    /**
     * Обновление по id на выбранную сумму
     * @param id
     * @param cash
     * @throws SQLException
     */
    public void updateCustomerCash(Integer id,Integer cash) throws SQLException {
        String sql = "UPDATE customer set cash = ? where id = ?";
        PreparedStatement  preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,cash);
        preparedStatement.setInt(2,id);
        ResultSet res = preparedStatement.executeQuery();
        System.out.printf("Обновление id: %d на сумму %d выполнено\n",id,cash);
    }

    /**
     * Общая информация о кошельках Customer
     * @throws SQLException
     */
    public void infoOfAllCustomers() throws SQLException {
        String sql = "SELECT MIN(cash), MAX(cash), AVG(cash), COUNT(cash), SUM(cash) FROM customer";
        PreparedStatement  preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int min = resultSet.getInt(1);
        int max = resultSet.getInt(2);
        int avg = resultSet.getInt(3);
        int count = resultSet.getInt(4);
        int sum = resultSet.getInt(5);
        System.out.printf("Минимальное значение: %d, максимальное значение: %d, среднее значение: %d, количество строк: %d, общая сумма: %d\n", min,max,avg,count,sum);
    }

    //Логика продавца

    /**
     * Добавление продавцов в БД
     * @param sellerList
     * @throws SQLException
     */
    public void insertIntoSeller(List<Seller> sellerList) throws SQLException {
        //ArrayList<Customer> customers = new ArrayList<>();
        String sql = "INSERT INTO seller(itemname,price,countitem) values(?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Seller seller : sellerList) {
                ps.setString(1, seller.getItemName());
                ps.setInt(2, seller.getPrice());
                ps.setInt(3,seller.getCountItem());
                ps.addBatch();
            }
            ps.executeBatch();
            Main.logger.info("Добавление продавцов завершено");
        }
    }

    /**
     * Обновление количества товара у продавца
     * @param id
     * @param countItem
     * @throws SQLException
     */
    public void updateSellerItem(Integer id,Integer countItem) throws SQLException {
        String sql = "UPDATE seller set countitem = ? where id = ?";
        PreparedStatement  preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,countItem);
        preparedStatement.setInt(2,id);
        ResultSet res = preparedStatement.executeQuery();
        System.out.printf("Текущее количества товара: %d у продавца с id: %d \n",countItem,id);
    }


    /**
     * Покупка товара покупателем
     * @param customerId
     * @param sellerId
     * @throws SQLException
     */
    public void trade(Integer customerId,Integer sellerId) throws SQLException {
        String sqlCustomer = "SELECT id,cash FROM customer WHERE id = ?";
        String sqlSeller = "SELECT id,itemname,price,countitem FROM seller WHERE id = ?";
        PreparedStatement  preparedStatement = connection.prepareStatement(sqlCustomer);
        PreparedStatement preparedStatement2 = connection.prepareStatement(sqlSeller);
        preparedStatement.setInt(1,customerId);
        preparedStatement2.setInt(1,sellerId);
        ResultSet res = preparedStatement.executeQuery();
        ResultSet res2 = preparedStatement2.executeQuery();
        while (res.next() && res2.next()){
            customerId = res.getInt("id");
            Integer cash = res.getInt("cash");
            Customer customer = new Customer(customerId,cash);
            sellerId = res2.getInt("id");
            String sellerItemName = res2.getString("itemname");
            int price = res2.getInt("price");
            int countItem = res2.getInt("countitem");
            Seller seller = new Seller(sellerId,sellerItemName,price,countItem);
            if (customer.getCash() > seller.getPrice()){
                int newCashForCustomer = customer.getCash() - seller.getPrice();
                customer.setCash(newCashForCustomer);
                seller.setCountItem(seller.getCountItem() - 1);
                System.out.printf("Товар %s у продавца с id: %d куплен покупателем с id: %d за сумму %d\n",
                        seller.getItemName(),seller.getId(),customer.getId(),seller.getPrice());
                updateCustomerCash(customer.getId(),customer.getCash());
                updateSellerItem(seller.getId(),seller.getCountItem());
                Main.logger.info("Обмен завершен");
            }
        }
    }
}
