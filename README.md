# EventHelper

Useful tool to help event masters with theirs job

Проект EventHelper пишется как набор связанных друг с другом плагинов.
Каждый релиз представляет собой набор этих плгаинов и собрку "все в одном", где все плагины прооекта помещены в один jar архив.

Для работы с EventHelper вам достаточно использовать сборку "все в одном" в папку `mods` сервера.

### Скачать
Скачать последний релиз проекта можно по ссылке: https://github.com/DiaLight/EventHelper/releases/latest

Формат релиза:
`EventHelper-<plugin>-<MC Server platform>-<MC Server version>-<EventHelper version>.jar`

Собрка "все в одном" соответственно имеет следующее название:
`EventHelper-all-<MC Server platform>-<MC Server version>-<EventHelper version>.jar`


### Структура проекта

Инструмент - Игровой item при помощи которого "Ивент мастер" может управлять игровыми сущностями.

Модуль - Код, реализующий игровую условность. Его можно включать и выключать в gui.

Гуи - Интерфейс открытого сундука, содержимое которого контролируется плагином.

#### Библиотеки
##### kotlinrt
Включает в себя kotlin runtime
* [X] Реализовано

##### toollib
Зависит от: `kotlinrt`
* [X] Реализовано

##### modulelib
Зависит от: `kotlinrt`
* [ ] Реализовано

##### guilib
Зависит от: `kotlinrt`
* [X] Реализовано

#### Инструменты
##### eventhelper
Используя `guilib`, реализует гуи, содержимое которого отражает список инструмнтов, зарегистрировнных в `toollib` и модулей, зарегистрированных в `modulelib`.

Зависит от: `kotlinrt`, `toollib`, `modulelib`, `guilib`
* [ ] Реализовано
  * [X] гуи для отображения и получения интрументов (View)
  * [ ] гуи для отображения и управления модулями (View)

##### teleporter
Зависит от: `kotlinrt`, `toollib`, [`guilib`], [`eventhelper`]
* [X] Реализовано
  * [X] структура данных выделенных игроков (Model)
  * [X] функцинал выделения/телепортирования и события телепортирования и вобора игроков (Control)
  * [X] гуи для выделенных игроков (View)

##### freezer
Зависит от: `kotlinrt`, `toollib`, [`guilib`], [`eventhelper`], [`teleporter`]
* [X] Реализовано
  * [X] структура данных замороженных игроков (Model)
  * [X] функцинал и события замораживания игроков (Control)
  * [X] гуи для заморженных игроков (View)

##### teams
Зависит от: `kotlinrt`, `toollib`, [`guilib`], [`eventhelper`], [`teleporter`]
* [X] Реализовано
  * [X] структура данных команд (Model)
  * [X] функцинал и события добавления/удаления из команды (Control)
  * [X] гуи для игроков в командах (View)

#### Модули
##### captain
Распределение игроков по камандам при помощи выбранных капитанов
Зависит от: `kotlinrt`, `modulelib`, `teams`, [`guilib`], [`eventhelper`]
* [ ] Реализовано

##### random
Рандомное распределение игроков по командам
Зависит от: `kotlinrt`, `modulelib`, `teams`, [`guilib`], [`eventhelper`]
* [ ] Реализовано
