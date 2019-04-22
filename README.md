# EventHelper

Useful tool to help event masters with theirs job

Проект EventHelper пишется как набор связанных друг с другом плагинов.
Каждый релиз представляет собой набор этих плгаинов и собрку "все в одном", где все плагины проекта помещены в один jar архив.

Для работы с EventHelper вам достаточно поместить сборку "все в одном" в папку `mods` сервера.

### Скачать
Скачать последний релиз проекта можно по ссылке: https://github.com/DiaLight/EventHelper/releases/latest

Формат релиза:
`EventHelper-<plugin>-<MC Server platform>-<MC Server version>-<EventHelper version>.jar`

Собрка "все в одном" соответственно имеет следующее название:
`EventHelper-all-<MC Server platform>-<MC Server version>-<EventHelper version>.jar`

<details>
  <summary>Структура проекта</summary>

Инструмент - Игровой item при помощи которого "Ивент мастер" может управлять игровыми сущностями.

Модуль - Код, реализующий игровую условность. Его можно включать и выключать в gui.

Гуи - Интерфейс открытого сундука, содержимое которого контролируется плагином.

#### Библиотеки
##### kotlinrt
Включает в себя kotlin runtime
* [X] Sponge

##### toollib
Зависит от: `kotlinrt`
* [X] Sponge
* [ ] Bukkit

##### modulelib
Зависит от: `kotlinrt`
* [X] Sponge
* [ ] Bukkit

##### guilib
Зависит от: `kotlinrt`
* [X] Sponge (сложный интерфейс)
* [ ] Bukkit

##### offlinelib
Позволяет телепортировать оффлайн игроков и оставлять вместо игроков жителей.

Зависит от: `kotlinrt`
* [X] Sponge (плохо, но работает)
* [ ] Bukkit

#### Инструменты
##### ehgui
Используя `guilib`, реализует гуи, содержимое которого отражает список инструмнтов, зарегистрировнных в `toollib` и модулей, зарегистрированных в `modulelib`.

Зависит от: `kotlinrt`, `toollib`, `modulelib`, `guilib`
* [X] Sponge
  * [X] гуи для отображения и получения интрументов (View)
  * [X] гуи для отображения и управления модулями (View)
* [ ] Bukkit

##### teleporter
Зависит от: `kotlinrt`, `toollib`, [`guilib`], [`eventhelper`]
* [X] Sponge
  * [X] структура данных выделенных игроков (Model)
  * [X] функцинал выделения/телепортирования и события телепортирования и вобора игроков (Control)
  * [X] гуи для выделенных игроков (View)
* [ ] Bukkit

##### freezer
Зависит от: `kotlinrt`, `toollib`, [`guilib`], [`eventhelper`], [`teleporter`]
* [X] Sponge
  * [X] структура данных замороженных игроков (Model)
  * [X] функцинал и события замораживания игроков (Control)
  * [X] гуи для заморженных игроков (View)
* [ ] Bukkit

##### teams
Зависит от: `kotlinrt`, `toollib`, [`guilib`], [`eventhelper`], [`teleporter`]
* [X] Sponge
  * [X] структура данных команд (Model)
  * [X] функцинал и события добавления/удаления из команды (Control)
  * [X] гуи для игроков в командах (View)
* [ ] Bukkit

#### Модули
##### captain
Распределение игроков по камандам при помощи выбранных капитанов
Зависит от: `kotlinrt`, `modulelib`, `toollib`, `teams`, `teleporter`, `freezer`, [`guilib`], [`eventhelper`]
* [x] Sponge (экспериментально. могут быть баги)
* [ ] Bukkit

##### random
Рандомное распределение игроков по командам
Зависит от: `kotlinrt`, `modulelib`, `teams`, [`guilib`], [`eventhelper`]
* [X] Sponge
* [ ] Bukkit

##### autorespawn
Автоматический респавн игрока после смерти
Зависит от: `kotlinrt`, `modulelib`, [`guilib`], [`eventhelper`]
* [X] Sponge
* [ ] Bukkit

</details>

