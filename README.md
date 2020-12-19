# DAO_warsztat
Warsztat systematyzujący wiedzę z zakrestu podstaw OOP i MySql

Dump workshop2.sql (MySql) zawiera przygotowaną tabelę users z jednym przykładowy wpisem.
Hasło przykładowego użytkownika jest zasolone przy użyciu jBCrypt.

Do poprawnego działania potrzebne są zewnętrzne biblioteki:
- jBCrypt 
- MySql Connetor, 

Klasa DbUtil zawiera metodę tworzącą połaczenie do bazy danych @localhost z ustalonymi parametrami (konieczne dostosowanie do lokalnych danych). 
Połączenie skonfigurowane do bazy danych 'workshop2' @localhost
