# ROLT - Rating of Landlords and Tenants

## 1. Obecný popis
Náš projekt slouží především k vzájemnému hodnocení pronajímatelů a nájemců. Tento projekt umožní pronajímatelům předem zkontrolovat hodnocení nájemníků, a tím snížit jejich riziko. Nájemce bude také schopen posoudit bezúhonnost pronajímatelů a na základě svého hodnocení (rating) bude moci získat nejlepší nabídku za nejlepší cenu.

## 2. Role
1. **Neautorizovaný uživatel** \
_Vyhledává pouze uživatele, nemovitostí a vidí informaci o nich (včetně recenzí)._
2. **Moderátor** \
_Sleduje stav nových uživatelů, recenzí a přidaných nemovitostí. Více informací najdete v části o funkcionalitě._
3. **Autorizovaný uživatel** \
_Přidává své nemovitosti a přidává zpětnou vazbu s hodnocením nájemců nebo přidává zpětnou vazbu s hodnocením pronajímatele. (může být pronajímatelem, nájemcem nebo pronajímatelem a nájemcem zároveň.)_
## 3. Funkcionalita
1. **Registrace** \
_Role 1_
2. **Autorizace** \
_Role 2, 3_
3. **Získávání informací o uživateli** \
_Role 1, 2, 3_
4. **Získání informací o nemovitosti** \
_Role 1, 2, 3_
5. **Přidávání recenzi uživateli** (nájemci nebo pronajímateli s označováním nemovitosti pronajímatele, pokud je zveřejněna) \
_Role 3_
6. **Zobrazení recenzí o uživateli** (včetně filtrování a stránkování) \
_Role 1, 2, 3_
7. **Přidávání nemovitosti** \
_Role 3_
8. **Změna stavu hodnocení** (ze stavu v moderování na stav zveřejněno) \
_Role 2_
9. **Změna stavu nemovitosti** (ze stavu v moderování na stav zveřejněno) \
_Role 2_
10. **Vyhledávání uživatelů** (včetně filtrování a stránkování) \
_Role 1, 2, 3_
11. **Vyhledávání nemovitosti** (včetně filtrování a stránkování) \
_Role 1, 2, 3_
 
Pokud se na něco zapomněli, bude to doplněno v dalších kontrolních bodech.

## 4. Technologie
Projekt bude používat Spring a rozhraní REST API.
