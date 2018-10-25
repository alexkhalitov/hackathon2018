
0. Если в maven есть проблемы c SSL, используем etc/settings.xml

1. Запуск hsqldb в режиме сервер:
java -cp db/hsqldb.jar org.hsqldb.server.Server --database.0 file:C:\Users\16695078\projects\hackaton\db\mydb --dbname.0 xdb
(можно создать Application и запускать через IDEA)

2. Приложение запускается с параметром -Dconfig.file=<полный путь до файла конфигурации>