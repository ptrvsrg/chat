# Задание 5. Сетевое программирование, сериализация, XML

## Описание
Напишите программу для общения через `Internet`. 
Программа должна состоять из двух частей: сервер и клиент. 
Сервер стартует в качестве отдельного приложения на определенном порту (задано в конфигурации). 
Клиент в виде приложения на `Swing` присоединяется к серверу по имени сервера и номеру порта.

## Минимальные возможности чата

+ Каждый участник чата имеет собственный никнейм, который указывается при присоединении к серверу.
+ Можно посмотреть список участников чата.
+ Можно послать сообщение в чат (всем участникам).
+ Клиент показывает все сообщения, которые отправили в чат с момента подключения. 
Также показывается некоторое число сообщений, отправленных до; 
список сообщений обновляется online.
+ Клиент отображает некоторые события: 
  + Подключение нового человека в чат;
  + Уход человека из чата;
  + Отключения клиента от чата (по таймауту);
+ Сервер должен логировать все события, которые происходят на его стороне.
+ Чат работает через `TCP/IP` протокол.

## Версии клиента и сервера 

### Java-объекты

Используется сериализация/десериализация Java-объектов для посылки/приема сообщений.

### XML сообщения 

Используются XML-сообщения.
Клиент и сервер должны поддерживать стандартный протокол (Это необходимо для возможности общение между клиентами, созданными разными учениками).

#### Описание протокола

Вначале XML сообщения хранятся 4 байта с длиной сообщения, т.е. сначала читаются первые 4 байта, узнается длина оставшегося сообщения (в байтах).
Затем считывается само сообщение и обрабатывается как XML документ.

Протокол взаимодействия для XML-сообщений (расширения приветствуются):

1. Регистрация

   + Client message

   ```xml
      <request name="login">
         <chat>CHAT_NAME</chat>
         <user>USER_NAME</user>
      </request>
   ```

   + Server error answer

   ```xml
      <response name="error">
        <message>REASON</message>
      </response>
   ```

   + Server success answer

   ```xml
      <response name="success">
         <session>SESSION_ID</session>
      </response>
   ```

2. Запрос списка пользователей чата

   + Client message

   ```xml
      <request name="user_list">
        <chat>CHAT_NAME</chat>
        <session>SESSION_ID</session>
      </request>
   ```

   + Server error answer

   ```xml
      <response name="error">
        <message>REASON</message>
      </response>
   ```

   + Server success answer

   ```xml
        <response name="success">
          <chat>CHAT_NAME</chat>
          <users>
            <user>
              <name>USER_1</name>
            </user>
            …
            <user>
              <name>USER_N</name>
            </user>
          </users>
        </response>
     ```

3. Отправка сообщения

   + Client message

   ```xml
     <request name="new_message">
        <chat>CHAT_NAME</chat>
        <session>SESSION_ID</session>
        <message>MESSAGE</message>
     </request>
     ```

   + Server error answer
   
   ```xml
      <response name="error">
        <message>REASON</message>
      </response>
   ```

   + Server success answer
 
   ```xml
       <response name="success">
       </response>
    ```
   
4. Отправка сообщения другим пользователем

   + Server message

   ```xml
      <event name="new_message">
         <chat>CHAT_NAME</chat>
         <name>USER_NAME</name>
         <message>MESSAGE</message>
      </event>
   ```

5. Отключение

   + Client message
   
   ```xml
      <request name="logout">
         <chat>CHAT_NAME</chat>
         <session>SESSION_ID</session>
      </request>
   ```
   
   + Server error answer
   
   ```xml
      <response name="error">
        <message>REASON</message>
      </response>
   ```
   
   + Server success answer
   
   ```xml
       <response name="success">
       </response>
   ```
   
6. Новый клиент
   
   + Server message
   
   ```xml
      <event name="login">
         <chat>CHAT_NAME</chat>
         <name>USER_NAME</name>
      </event>
   ```

7. Клиент отключился

   + Server message

   ```xml
      <event name="logout">
         <chat>CHAT_NAME</chat>
         <name>USER_NAME</name>
      </event>
   ```

## Рекомендации

+ Сервер слушает порт с помощью класса `java.net.ServerSocket`
+ Клиент подсоединяется к серверу с помощью класса `java.net.Socket`
+ XML-сообщение читать с помощью `DOM parser`:
   ```
      DocumentBuilderFactory.newInstance().newDocumentBuilder().parse()
   ```

+ Сериализация/десериализация объекта выполняется через классы `ObjectInputStream` и `ObjectOutputStream`