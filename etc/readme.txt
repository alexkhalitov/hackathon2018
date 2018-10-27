
0. Если в maven есть проблемы c SSL, используем etc/settings.xml

1. Запуск hsqldb в режиме сервер:
java -cp db/hsqldb.jar org.hsqldb.server.Server --database.0 file:C:\Users\16695078\projects\hackaton\db\mydb --dbname.0 xdb
java -cp db/hsqldb.jar org.hsqldb.server.Server --database.0 file:home/artem/IdeaProjects/hackaton/db/mydb --dbname.0 xdb

(можно создать Application и запускать через IDEA)

2. Приложение запускается с параметром -Dconfig.file=<полный путь до файла конфигурации>

3.Старт приложения - SpringBootStarter
4.Веб вход http://localhost:8182 (Пример контроллера RootController)
5. Для работы с БД создать инрефейс CrudRepository<Название модели, String> (см.CategoryRepository)
   далее можно в сервис, можно так работать см. сервис CategoryService
   пример работы RootController
6. БД заполняется при старте см.FillDB