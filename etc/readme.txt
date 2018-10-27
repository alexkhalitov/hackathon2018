
1. Если в maven есть проблемы c SSL, используем etc/settings.xml

2. Приложение запускается с параметром -Dconfig.file=<полный путь до файла конфигурации>

3.Старт приложения - SpringBootStarter
4.Веб вход http://localhost:8182 (Пример контроллера RootController)
5. Для работы с БД создать инрефейс CrudRepository<Название модели, String> (см.CategoryRepository)
   далее можно в сервис, можно так работать см. сервис CategoryService
   пример работы RootController
6. БД заполняется при старте см.FillDB