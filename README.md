# EventHelper

Useful tool to help event masters with theirs job

Проект EventHelper пишется как набор связанных друг с другом плагинов.
Каждый релиз представляет собой набор этих плагинов и сборку "все в одном", где все плагины проекта помещены в один jar архив.

Для работы с EventHelper вам достаточно поместить сборку "все в одном" в папку `mods` сервера.

### Скачать
Скачать последний релиз проекта можно по ссылке: https://github.com/DiaLight/EventHelper/releases/latest

Формат релиза:
`EventHelper-<plugin>-<MC Server platform>-<MC Server version>-<EventHelper version>.jar`

Сборка "все в одном" соответственно имеет следующее название:
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
* [X] Sponge
  * Зависит от: `kotlinrt`
* [X] Bukkit

##### modulelib
* [X] Sponge
  * Зависит от: `kotlinrt`
* [ ] Bukkit

##### guilib
* [X] Sponge (сложный интерфейс)
  * Зависит от: `kotlinrt`
* [X] Bukkit

##### offlinelib
Позволяет телепортировать оффлайн игроков и оставлять вместо игроков жителей.

* [X] Sponge (плохо, но работает)
  * Зависит от: `kotlinrt`
* [X] Bukkit

#### Инструменты
##### maingui
Используя `guilib`, реализует гуи, содержимое которого отражает список инструмнтов, зарегистрировнных в `toollib` и модулей, зарегистрированных в `modulelib`.

Зависит от: `toollib`, `modulelib`, `guilib`
* [X] Sponge
  * Зависит от: `kotlinrt`
* [X] Bukkit

##### teleporter
Зависит от: `toollib`, [`guilib`], [`maingui`]
* [X] Sponge
  * Зависит от: `kotlinrt`
* [X] Bukkit

##### freezer
Зависит от: `toollib`, [`guilib`], [`maingui`], [`teleporter`]
* [X] Sponge
  * Зависит от: `kotlinrt`
* [ ] Bukkit

##### teams
Зависит от: `toollib`, [`guilib`], [`maingui`], [`teleporter`]
* [X] Sponge
  * Зависит от: `kotlinrt`
* [X] Bukkit

#### Модули
##### captain
Распределение игроков по камандам при помощи выбранных капитанов

Зависит от: `modulelib`, `toollib`, `teams`, `maingui`, `freezer`, [`guilib`], [`eventhelper`]
* [x] Sponge (экспериментально. могут быть баги)
  * Зависит от: `kotlinrt`
* [ ] Bukkit

##### random(randomizer in bukkit)
Рандомное распределение игроков по командам

Зависит от: `teams`, [`guilib`], [`maingui`]
* [X] Sponge
  * Зависит от: `modulelib`, `kotlinrt`
* [X] Bukkit

##### autorespawn
Автоматический респавн игрока после смерти
Зависит от: `modulelib`, [`guilib`], [`maingui`]
* [X] Sponge
  * Зависит от: `kotlinrt`
* [ ] Bukkit

</details>

