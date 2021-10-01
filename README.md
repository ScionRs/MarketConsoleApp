### MarketConsoleApp  
Простое консольное приложение для взаимодействия с Базой данных  
Основной стек технологий:  
- Java 11 :speech_balloon:
- MariaDB :cd: 
- Log4J :pencil2:
- JColor :art:  
- Maven :m:

#### Функционал: 
1. Добавление покупателей
2. Добавление продавцов
3. Просмотр всех покупателей
4. Поиск покупателя по id
5. Поиск покупателя по сумме
6. Обновить наличность покупателя
7. Удалить покупателя
8. Получить общую информацию о кошельках(MIN,MAX,AVERAGE)
9. Совершить обмен
 10. Добавление покупателя
#### Запуск:
1. Скачать проект
2. Настроить соединение с БД(database.properties)
3. Выполнить sql-запросы:  
~~~
CREATE TABLE customer(
id INT AUTO_INCREMENT PRIMARY KEY,
cash INT
);  
~~~
~~~
CREATE TABLE seller(
id INT AUTO_INCREMENT PRIMARY KEY,
itemname VARCHAR(255),
price INT,
countitem INT
);
~~~
4.Запустить
