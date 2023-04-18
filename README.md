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
      <command name="login">
         <name>USER_NAME</name>
         <type>CHAT_CLIENT_NAME</type>
      </command>
   ```

   + Server error answer

   ```xml
      <error>
        <message>REASON</message>
      </error>
   ```

   + Server success answer

   ```xml
      <success>
        <session>UNIQUE_SESSION_ID</session>
      </success>
   ```

2. Запрос списка пользователей чата

   + Client message

   ```xml
      <command name="list">
        <session>UNIQUE_SESSION_ID</session>
      </command>
   ```

   + Server error answer

   ```xml
      <error>
         <message>REASON</message>
      </error>
   ```

   + Server success answer

   ```xml
        <success>
           <listusers>
              <user>
                 <name>USER_1</name>
                 <type>CHAT_CLIENT_1</type>
              </user>
              …
              <user>
                 <name>USER_N</name>
                 <type>CHAT_CLIENT_N</type>
              </user>
           </listusers>
        </success>
     ```

3. Сообщение от клиента серверу

   + Client message

   ```xml
     <command name="message">
        <message>MESSAGE</message>
        <session>UNIQUE_SESSION_ID</session>
     </command>
     ```

   + Server error answer
   
   ```xml
      <error>
        <message>REASON</message>
      </error>
   ```

   + Server success answer
 
   ```xml
       <success>
       </success>
    ```
   
4. Сообщение от сервера клиенту

   + Server message

   ```xml
      <event name="message">
         <message>MESSAGE</message>
         <name>CHAT_NAME_FROM</name>
      </event>
   ```

5. Отключение

   + Client message
   
   ```xml
      <command name="logout">
         <session>UNIQUE_SESSION_ID</session>
      </command>
   ```
   
   + Server error answer
   
   ```xml
      <error>
         <message>REASON</message>
      </error>
   ```
   
   + Server success answer
   
   ```xml
      <success>
      </success>
   ```
   
6. Новый клиент
   
   + Server message
   
   ```xml
      <event name="userlogin">
         <name>USER_NAME</name>
      </event>
   ```

7. Клиент отключился

   + Server message
   
   ```xml
      <event name="userlogout">
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